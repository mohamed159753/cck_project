// reports.component.ts - Fixed version
import { Component, AfterViewInit, ViewChild, ElementRef, OnInit, OnDestroy } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { CkkDashboardService } from '../../../services/ckk-dashboard.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../../sidebar/sidebar.component';

Chart.register(...registerables);

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [FormsModule, CommonModule, SidebarComponent],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements AfterViewInit, OnInit, OnDestroy {
  @ViewChild('barChart') barChartRef!: ElementRef<HTMLCanvasElement>;
  chart!: Chart;

  // Filter Options
  universities = ['All'];
  resources = ['CPU', 'RAM', 'Storage'];
  selectedUniversity = 'All';
  selectedResource = 'CPU';

  // Dashboard Data
  dashboardData: any;
  loading = true;

  // Alert variables
  alertMessage = '';
  alertColor = '';

  // Chart initialization flag
  private chartInitialized = false;

  constructor(private cckDashboardService: CkkDashboardService) {}

  ngOnInit() {
    this.getUni();
    this.loadDashboardData();
  }

  ngAfterViewInit() {
    // Initialize chart after view is ready
    setTimeout(() => {
      this.initializeChart();
    }, 500);
  }

  ngOnDestroy() {
    if (this.chart) {
      this.chart.destroy();
    }
  }

  getUni() {
    this.cckDashboardService.getUni().subscribe((res) => {
      console.log('Universities:', res);
      this.universities = ['All', ...res.map((uni: any) => uni.id)];
    });
  }

  loadDashboardData() {
    this.loading = true;
    
    const dashboardObservable = this.selectedUniversity === 'All'
      ? this.cckDashboardService.getDashboardStatistics()
      : this.cckDashboardService.getUniDashboardStatistics(this.selectedUniversity);

    dashboardObservable.subscribe({
      next: (data) => {
        console.log('Dashboard data:', data);
        this.dashboardData = data;
        this.loading = false;

        // Update chart and alerts smoothly without recreating everything
        this.updateChartData();
        this.updateAlerts();
      },
      error: (err) => {
        console.error('Error loading dashboard data:', err);
        this.loading = false;
      }
    });
  }

  getFilteredData() {
    if (!this.dashboardData) return null;

    return {
      reservations: this.dashboardData.reservations || 0,
      pending: this.dashboardData.pending || 0,
      approved: this.dashboardData.approved || 0,
      totalQuota: this.getResourceTotalQuota(),
      currentUse: this.getCurrentResourceUsePercentage(),
      toBeUsed: this.getToBeUsedPercentage()
    };
  }

  getResourceTotalQuota(): number {
    if (!this.dashboardData) return 0;
    
    switch(this.selectedResource) {
      case 'CPU': return this.dashboardData.TotalVcpuQuotaReserved || 0;
      case 'RAM': return this.dashboardData.TotalRamQuotaReserved || 0;
      case 'Storage': return this.dashboardData.TotalStorageQuotaReserved || 0;
      default: return 0;
    }
  }

  getCurrentResourceUsePercentage(): number {
    if (!this.dashboardData) return 0;
    
    switch(this.selectedResource) {
      case 'CPU': return this.dashboardData.vcpuUsePercent || 0;
      case 'RAM': return this.dashboardData.ramUsePercent || 0;
      case 'Storage': return this.dashboardData.storageUsePercent || 0;
      default: return 0;
    }
  }

  getToBeUsedPercentage(): number {
    const totalQuota = this.getResourceTotalQuota();
    const currentUse = this.getCurrentResourceUsePercentage();
    return Math.max(0, 100 - currentUse);
  }

  getChartData(): number[] {
    if (!this.dashboardData?.usageStatistics) {
      console.warn('No usage statistics available');
      return new Array(12).fill(0);
    }

    const resourceMap: { [key: string]: string } = {
      'CPU': 'vcpu',
      'RAM': 'ram',
      'Storage': 'storage'
    };

    const resourceKey = resourceMap[this.selectedResource];
    const months = ['jan', 'feb', 'mar', 'apr', 'may', 'jun', 'jul', 'aug', 'sep', 'oct', 'nov', 'dec'];
    
    const chartData = months.map(month => {
      const monthData = this.dashboardData.usageStatistics[month];
      if (!monthData) return 0;
      
      const value = monthData[resourceKey] || 0;
      return value;
    });

    console.log('Chart data:', chartData);
    return chartData;
  }

  initializeChart() {
    if (!this.barChartRef?.nativeElement) {
      console.error('Canvas element not available');
      return;
    }

    const ctx = this.barChartRef.nativeElement.getContext('2d');
    if (!ctx) {
      console.error('Could not get canvas context');
      return;
    }

    // Destroy existing chart if it exists
    if (this.chart) {
      this.chart.destroy();
    }

    const chartData = this.getChartData();

    this.chart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
        datasets: [{
          label: `${this.selectedResource} Usage`,
          data: chartData,
          backgroundColor: 'rgba(54, 162, 235, 0.6)',
          borderColor: 'rgba(54, 162, 235, 1)',
          borderWidth: 2,
          borderRadius: 4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        animation: {
          duration: 750,
          easing: 'easeInOutQuart'
        },
        plugins: {
          legend: {
            display: true,
            position: 'top'
          },
          tooltip: {
            mode: 'index',
            intersect: false,
            callbacks: {
              label: (context) => {
                return `${context.dataset.label}: ${context.parsed.y}`;
              }
            }
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            title: {
              display: true,
              text: 'Usage Amount'
            },
            grid: {
              color: 'rgba(0, 0, 0, 0.1)'
            }
          },
          x: {
            title: {
              display: true,
              text: 'Month'
            },
            grid: {
              display: false
            }
          }
        }
      }
    });

    this.chartInitialized = true;
  }

  updateChartData() {
    if (!this.chart || !this.chartInitialized) {
      // If chart isn't ready, try to initialize it
      setTimeout(() => {
        this.initializeChart();
      }, 100);
      return;
    }

    const chartData = this.getChartData();
    
    // Smooth update of existing chart
    this.chart.data.datasets[0].data = chartData;
    this.chart.data.datasets[0].label = `${this.selectedResource} Usage`;
    this.chart.update('active'); // Use 'active' animation mode for smoother transitions
  }

  updateAlerts() {
    const data = this.getFilteredData();
    if (!data) return;

    const usagePercent = data.currentUse;

    if (usagePercent > 80) {
      this.alertMessage = `High ${this.selectedResource} usage: ${usagePercent.toFixed(1)}%`;
      this.alertColor = 'high-usage';
    } else if (usagePercent < 30) {
      this.alertMessage = `Low ${this.selectedResource} usage: ${usagePercent.toFixed(1)}%`;
      this.alertColor = 'low-usage';
    } else {
      this.alertMessage = `Normal ${this.selectedResource} usage: ${usagePercent.toFixed(1)}%`;
      this.alertColor = 'medium-usage';
    }
  }

  onFilterChange() {
    console.log('Filter changed - University:', this.selectedUniversity, 'Resource:', this.selectedResource);
    
    // Prevent default form submission behavior
    event?.preventDefault();
    
    // Smooth transition - no page refresh
    this.loadDashboardData();
  }

  getResourceUsageForUniversity(uni: any): number {
    if (!uni) return 0;
    
    switch(this.selectedResource) {
      case 'CPU': return uni.cpuUsage || 0;
      case 'RAM': return uni.ramUsage || 0;
      case 'Storage': return uni.storageUsage || 0;
      default: return 0;
    }
  }

  dismissAlert() {
    this.alertMessage = '';
  }
}