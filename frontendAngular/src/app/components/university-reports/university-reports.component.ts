import { Component, OnInit } from '@angular/core';
import { NgFor} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UniversitySidebarComponent } from "../university-sidebar/university-sidebar.component";
import { UniversityStats } from '../../models/university-stats';
import { InstituteConsumption } from '../../models/institute-consumption';
import { ProfessorReservation } from '../../models/professor-reservation';
import { UniversityReportsServiceService } from '../../services/university-reports-service.service';



@Component({
  selector: 'app-university-reports',
  standalone: true,
  imports: [FormsModule, UniversitySidebarComponent, NgFor],
  templateUrl: './university-reports.component.html',
  styleUrl: './university-reports.component.css'
})
export class UniversityReportsComponent implements OnInit {

  stats: UniversityStats = {
    totalReservations: 0,
    totalInstitutes: 0,
    totalProfessors: 0
  };
  
  institutes: InstituteConsumption[] = [];
  professors: ProfessorReservation[] = [];
  
  selectedMonth: number = new Date().getMonth() + 1; // Default to current month
  selectedYear: number = new Date().getFullYear();
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
  
  // Replace with actual university ID from auth service or session storage
  universityId: number = 1;

  constructor(private dashboardService: UniversityReportsServiceService) { }

  ngOnInit(): void {
    console.log('Component initialized');
    this.loadDashboardData();
    
  }

  loadDashboardData(): void {
    console.log(`Loading data for University ID: ${this.universityId}, Month: ${this.selectedMonth}, Year: ${this.selectedYear}`);//for debugging

    this.dashboardService.getDashboardStats(this.universityId, this.selectedMonth, this.selectedYear)
      .subscribe(stats => {
        this.stats = stats;
        console.log('Stats received:', stats);//for debugging
        
      });

    this.dashboardService.getTopInstitutes(this.universityId, this.selectedMonth, this.selectedYear)
      .subscribe(institutes => {
        this.institutes = institutes;
        console.log('Institutes received:', institutes);
      });

    this.dashboardService.getTopProfessors(this.universityId, this.selectedMonth, this.selectedYear)
      .subscribe(professors => {
        this.professors = professors;
        console.log('Professors received:', professors);
      });
  }

  onMonthChange(event: any): void {
    this.selectedMonth = +event.target.value;
    this.loadDashboardData();
  }

  formatDate(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
  }
}