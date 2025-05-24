import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UniversitySidebarComponent } from '../university-sidebar/university-sidebar.component';
import { UniversityReportsServiceService } from '../../services/university-reports-service.service';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-university-dashboard',
  standalone: true,
  imports: [CommonModule, UniversitySidebarComponent, FormsModule],
  templateUrl: './university-reports.component.html',
  styleUrls: ['./university-reports.component.css']
})
export class UniversityReportsComponent implements AfterViewInit, OnInit {
  @ViewChild('barChart') barChartRef!: ElementRef<HTMLCanvasElement>;
  chart!: Chart;

  selectedResource: 'vcpu' | 'ram' | 'storage' = 'vcpu';
  data: any = {};
  topData:any;
  isLoading = true;
  loadError = false;
  universityId:any;

  reservationsData = {
    total: 0,
    approved: 0,
    pending: 0
  };

  quotaData = {
    total: 0,
    current: 0,
    remaining: 0
  };

  months = [
    { name: 'January', value: 1, key: 'jan' },
    { name: 'February', value: 2, key: 'feb' },
    { name: 'March', value: 3, key: 'mar' },
    { name: 'April', value: 4, key: 'apr' },
    { name: 'May', value: 5, key: 'may' },
    { name: 'June', value: 6, key: 'jun' },
    { name: 'July', value: 7, key: 'jul' },
    { name: 'August', value: 8, key: 'aug' },
    { name: 'September', value: 9, key: 'sep' },
    { name: 'October', value: 10, key: 'oct' },
    { name: 'November', value: 11, key: 'nov' },
    { name: 'December', value: 12, key: 'dec' }
  ];

  years=[
    {name:'2025', value:2025}
  ]

 
  selectedMonth = new Date().getMonth() + 1;
  selectedYear = new Date().getFullYear();

  usageStatistics: any = {};
  currentUsage = 0;
  usageMessage = '';
  usageColor = '';

  constructor(private reportService: UniversityReportsServiceService) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  ngAfterViewInit(): void {
    // Wait for canvas to exist, then create chart once data is available
    setTimeout(() => {
      if (this.data && Object.keys(this.data).length) {
        this.createChart();
      }
    }, 500);
  }

  loadDashboardData(): void {
    this.isLoading = true;
    this.loadError = false;
    this.universityId = localStorage.getItem("universityId")
    this.reportService.getUniDashboardStatistics(this.universityId).subscribe({
      next: (data: any) => {
        this.data = data;

        this.reservationsData.total = data.reservations;
        this.reservationsData.approved = data.approved;
        this.reservationsData.pending = data.pending;

        this.usageStatistics = data.usageStatistics;

        this.updateQuotaDisplay();
        this.updateUsageAlert();
        this.createChart();

        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.loadError = true;
      }
    });

    this.reportService.getTopInstitutes(this.universityId,this.selectedMonth, this.selectedYear).
    subscribe((topData:any)=>{
      this.topData = topData;
      console.log(this.selectedMonth);
      console.log(this.selectedYear);
      console.log('TopInstitutes: ',topData);
    })
  }

  onMonthChange(event: Event): void {
    const value = parseInt((event.target as HTMLSelectElement).value, 10);
    this.selectedMonth = value;
    this.updateQuotaDisplay();
    this.updateUsageAlert();
    this.refreshTopInstitutesData(); // Add this call to refresh data when month changes
}

onYearChange(event: Event): void {
  const value = parseInt((event.target as HTMLSelectElement).value, 10);
  this.selectedYear = value;
  this.updateQuotaDisplay();
  this.updateUsageAlert();
  this.refreshTopInstitutesData(); // Add this call to refresh data when year changes
}

refreshTopInstitutesData(): void {
  this.reportService.getTopInstitutes(this.universityId, this.selectedMonth, this.selectedYear)
    .subscribe({
      next: (topData: any) => {
        this.topData = topData;
        console.log('Month:', this.selectedMonth);
        console.log('Year:', this.selectedYear);
        console.log('TopInstitutes:', topData);
      },
      error: (err) => {
        console.error('Error fetching top institutes:', err);
      }
    });
}

  onResourceTypeChange(event: Event): void {
    this.selectedResource = (event.target as HTMLSelectElement).value as 'vcpu' | 'ram' | 'storage';
    this.updateQuotaDisplay();
    this.updateUsageAlert();
    this.updateChart();
  }

  updateQuotaDisplay(): void {
    const monthKey = this.months.find(m => m.value === this.selectedMonth)?.key || '';
    const stat = this.usageStatistics[monthKey] || { vcpu: 0, ram: 0, storage: 0 };

    const resourceMap = {
      vcpu: { used: this.data.UsedVcpu, total: this.data.TotalVcpuQuotaReserved },
      ram: { used: this.data.UsedRam, total: this.data.TotalRamQuotaReserved },
      storage: { used: this.data.UsedStorage, total: this.data.TotalStorageQuotaReserved }
    };

    const selected = resourceMap[this.selectedResource];
    this.quotaData.total = selected.total;
    this.quotaData.current = selected.used;
    this.quotaData.remaining = selected.total - selected.used;

    this.currentUsage = selected.total > 0 ? Math.round((selected.used / selected.total) * 100) : 0;
  }

updateUsageAlert(): void {
  if (this.currentUsage >= 80) {
    this.usageMessage = 'High Usage';
    this.usageColor = 'alert-danger'; // <-- Bootstrap red
  } else if (this.currentUsage >= 50) {
    this.usageMessage = 'Moderate Usage';
    this.usageColor = 'alert-warning'; // <-- Bootstrap yellow
  } else {
    this.usageMessage = 'Low Usage';
    this.usageColor = 'alert-success'; // <-- Bootstrap green
  }
}

  getUsageData(): Record<string, number> {
    const result: Record<string, number> = {};
    this.months.forEach(month => {
      const usage = this.usageStatistics[month.key];
      result[month.key] = usage ?  (usage[this.selectedResource] /this.quotaData.total) *100 || 0 : 0;
      
    });
    console.log(result)
    return result;
  }

  createChart(): void {
    if (!this.barChartRef) return;

    const ctx = this.barChartRef.nativeElement.getContext('2d');
    if (!ctx) return;

    const usageData = this.getUsageData();
    const labels = this.months.map(m => m.name);

    if (this.chart) {
      this.chart.destroy();
    }

    this.chart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: `${this.selectedResource.toUpperCase()} Usage (%)`,
          data: Object.values(usageData),
          backgroundColor: 'rgba(54, 162, 235, 0.6)',
          borderColor: 'rgba(54, 162, 235, 1)',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: true
          },
          tooltip: {
            mode: 'index',
            intersect: false
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            max: 100,
            title: {
              display: true,
              text: 'Usage %'
            }
          }
        }
      }
    });
  }

  updateChart(): void {
    if (!this.chart) {
      this.createChart();
      return;
    }

    const usageData = this.getUsageData();
    this.chart.data.datasets[0].label = `${this.selectedResource.toUpperCase()} Usage (%)`;
    this.chart.data.datasets[0].data = Object.values(usageData);
    this.chart.update();
  }

  calculatePercentage(value: number, total: number): number {
    if (!total) return 0;
    return Math.min((value / total) * 100, 100);
  }
    
}
