import { Component, OnInit } from '@angular/core';
import { ProfMetricsService } from '../../services/prof-metrics.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
   standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './prof-dashboard.component.html',
  styleUrls: ['./prof-dashboard.component.css']
})
export class ProfDashboardComponent implements OnInit {
  
  metrics: any = {};
  loading = true;

  constructor(private profMetricsService: ProfMetricsService) {}

  ngOnInit(): void {
    this.fetchMetrics();
    setInterval(() => this.fetchMetrics(), 10000); // auto-refresh every 10s
  }

  fetchMetrics() {
    this.profMetricsService.getMetrics().subscribe({
      next: (data) => {
        this.metrics = data;
        this.loading = false;
      },
      error: (err) => console.error('Error fetching metrics', err)
    });
  }
}
