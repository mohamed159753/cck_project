import { Component, OnInit, OnDestroy } from '@angular/core';
import { ProfMetricsService } from '../../services/prof-metrics.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UniversitySidebarComponent } from '../university-sidebar/university-sidebar.component';
import { Subscription, interval } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { ProfSidebarComponent } from '../../prof-sidebar/prof-sidebar.component';

// Define interfaces for type safety
interface NetworkMetrics {
  sent_kbps: number;
  recv_kbps: number;
}

interface HostMetrics {
  cpu_usage: number;
  memory_usage_percent: number;
  memory_used_MB: number;
  memory_available_MB: number;
  memory_total_MB: number;
  network?: NetworkMetrics;
}

interface MetricHistory {
  cpu: number[];
  memory: number[];
  network_sent: number[];
  network_recv: number[];
  timestamp: Date[];
}

interface MetricItem {
  key: string;
  value: HostMetrics;
  show: boolean;
  history: MetricHistory;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [ProfSidebarComponent, CommonModule, FormsModule],
  templateUrl: './prof-dashboard.component.html',
  styleUrls: ['./prof-dashboard.component.css']
})
export class ProfDashboardComponent implements OnInit, OnDestroy {
  
  metricsArray: MetricItem[] = [];
  
  loading = true;
  refreshInterval = 10; // seconds
  timeLeft = this.refreshInterval;
  refreshSubscription?: Subscription;
  countdownSubscription?: Subscription;
  selectedView: 'card' | 'table' | 'grid' = 'card';

  constructor(private profMetricsService: ProfMetricsService) {}

  ngOnInit(): void {
    this.fetchMetrics();
    
    // Setup auto-refresh
    this.refreshSubscription = interval(this.refreshInterval * 1000).pipe(
      switchMap(() => {
        this.timeLeft = this.refreshInterval;
        return this.profMetricsService.getMetrics();
      })
    ).subscribe({
      next: (data) => this.updateMetrics(data),
      error: (err) => console.error('Error fetching metrics', err)
    });
    
    // Countdown timer
    this.countdownSubscription = interval(1000).subscribe(() => {
      if (this.timeLeft > 0) {
        this.timeLeft--;
      }
    });
  }

  ngOnDestroy(): void {
    this.refreshSubscription?.unsubscribe();
    this.countdownSubscription?.unsubscribe();
  }

  fetchMetrics() {
    this.profMetricsService.getMetrics().subscribe({
      next: (data: Record<string, HostMetrics>) => {
        this.metricsArray = Object.entries(data).map(([key, value]) => ({
          key,
          value,
          show: true, // Default to expanded
          history: {
            cpu: [value.cpu_usage],
            memory: [value.memory_usage_percent],
            network_sent: [value.network?.sent_kbps ?? 0],
            network_recv: [value.network?.recv_kbps ?? 0],
            timestamp: [new Date()]
          }
        }));
        this.loading = false;
      },
      error: (err) => console.error('Error fetching metrics', err)
    });
  }
  
  updateMetrics(data: Record<string, HostMetrics>) {
    Object.entries(data).forEach(([key, value]) => {
      const existingMetric = this.metricsArray.find(m => m.key === key);
      
      if (existingMetric) {
        // Update current value
        existingMetric.value = value;
        
        // Update history (keep last 30 data points)
        existingMetric.history.cpu.push(value.cpu_usage);
        existingMetric.history.memory.push(value.memory_usage_percent);
        existingMetric.history.network_sent.push(value.network?.sent_kbps ?? 0);
        existingMetric.history.network_recv.push(value.network?.recv_kbps ?? 0);
        existingMetric.history.timestamp.push(new Date());
        
        // Trim history to keep last 30 points
        if (existingMetric.history.cpu.length > 30) {
          existingMetric.history.cpu.shift();
          existingMetric.history.memory.shift();
          existingMetric.history.network_sent.shift();
          existingMetric.history.network_recv.shift();
          existingMetric.history.timestamp.shift();
        }
      } else {
        // Add new host
        this.metricsArray.push({
          key,
          value,
          show: true,
          history: {
            cpu: [value.cpu_usage],
            memory: [value.memory_usage_percent],
            network_sent: [value.network?.sent_kbps ?? 0],
            network_recv: [value.network?.recv_kbps ?? 0],
            timestamp: [new Date()]
          }
        });
      }
    });
  }

  getStatusClass(value: number): string {
    if (value <= 50) return 'success';
    if (value <= 80) return 'warning';
    return 'danger';
  }
  
  toggleAll(showState: boolean) {
    this.metricsArray.forEach(metric => metric.show = showState);
  }
  
  refreshNow() {
    this.timeLeft = this.refreshInterval;
    this.fetchMetrics();
  }
  
  // Helper method to get array of timestamps for chart labels
  // Add Math property for use in templates
  Math = Math;
  
  getTimeLabels(host: MetricItem): string[] {
    return host.history.timestamp.map((time: Date) => 
      time.getHours().toString().padStart(2, '0') + ':' + 
      time.getMinutes().toString().padStart(2, '0') + ':' + 
      time.getSeconds().toString().padStart(2, '0')
    );
  }
}