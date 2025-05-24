import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from '../../dashboard/sidebar/sidebar.component';
import { UniversitySidebarComponent } from '../university-sidebar/university-sidebar.component';
import { ProfReportsService } from '../../services/prof-reports.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProfSidebarComponent } from '../../prof-sidebar/prof-sidebar.component';

@Component({
  selector: 'app-prof-reports',
  standalone: true,
  imports: [ProfSidebarComponent,CommonModule,FormsModule],
  templateUrl: './prof-reports.component.html',
  styleUrl: './prof-reports.component.css'
})
export class ProfReportsComponent implements OnInit {
  // Data properties
  reportData: any = {
    totalDisk: 0,
    totalVcpu: 0,
    totalVram: 0,
    imageUsage: {}
  };

  reservationData : any = {
    reservations:0,
    pending:0,
    approved:0,
    canceled:0
  }
  
  // Filter properties
  selectedMonth: number = new Date().getMonth() + 1; // Current month (1-12)
  selectedYear: number = new Date().getFullYear(); // Current year
  
  // Month names for dropdown
  months = [
    { value: 1, name: 'January' },
    { value: 2, name: 'February' },
    { value: 3, name: 'March' },
    { value: 4, name: 'April' },
    { value: 5, name: 'May' },
    { value: 6, name: 'June' },
    { value: 7, name: 'July' },
    { value: 8, name: 'August' },
    { value: 9, name: 'September' },
    { value: 10, name: 'October' },
    { value: 11, name: 'November' },
    { value: 12, name: 'December' }
  ];
  
  // Years for dropdown (current year and 2 previous years)
  years = this.generateYears();
  
  // Loading state
  isLoading: boolean = false;
  error: string | null = null;

  constructor(
    private profReportsService: ProfReportsService,  ) {}

  ngOnInit(): void {
    this.loadReportData();
    this.loadReservationsData();
  }

  // Generate array of years (current year and 2 previous years)
  generateYears(): number[] {
    const currentYear = new Date().getFullYear();
    return [currentYear, currentYear - 1, currentYear - 2];
  }

  // Get month name from month number
  getMonthName(monthNumber: number): string {
    const month = this.months.find(m => m.value === monthNumber);
    return month ? month.name : '';
  }

  // Load report data based on selected filters
  loadReportData(): void {
    this.isLoading = true;
    this.error = null;
    
    const token = localStorage.getItem("token2"); // Get token from auth service
    
    this.profReportsService.getProfessorReports(token, this.selectedMonth, this.selectedYear)
      .subscribe({
        next: (data) => {
          this.reportData = data;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error fetching report data:', err);
          this.error = 'Failed to load report data. Please try again.';
          this.isLoading = false;
        }
      });
  }

  loadReservationsData(){

    this.isLoading = true;
    this.error = null;
    
    const token = localStorage.getItem("token2"); // Get token from auth service
    
    this.profReportsService.getReservationsStats(token)
      .subscribe({
        next: (data) => {
          this.reservationData = data;
          console.log(this.reservationData)
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error fetching report data:', err);
          this.error = 'Failed to load report data. Please try again.';
          this.isLoading = false;
        }
      });

  }

  // Handle month change
  onMonthChange(event: any): void {
    this.selectedMonth = parseInt(event.target.value, 10);
    this.loadReportData();
  }

  // Handle year change
  onYearChange(event: any): void {
    this.selectedYear = parseInt(event.target.value, 10);
    this.loadReportData();
  }

  // Get entries from imageUsage object for template iteration
  getImageUsageEntries(): {name: string, percentage: number}[] {
    if (!this.reportData.imageUsage) return [];
    
    return Object.entries(this.reportData.imageUsage).map(([name, percentage]: [string, any]) => {
      return { name, percentage: percentage as number };
    });
  }
}