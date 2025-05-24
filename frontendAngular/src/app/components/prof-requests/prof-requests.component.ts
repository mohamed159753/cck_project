import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UniversitySidebarComponent } from '../university-sidebar/university-sidebar.component';
import { ProfRequestsService } from '../../services/prof-requests.service';
import { ProfSidebarComponent } from '../../prof-sidebar/prof-sidebar.component';

@Component({
  selector: 'app-prof-requests',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, ProfSidebarComponent],
  templateUrl: './prof-requests.component.html',
  styleUrls: ['./prof-requests.component.css']
})
export class ProfRequestsComponent implements OnInit {

  reservations: any[] = [];
  filteredReservations: any[] = [];
  startDate: string = '';
  endDate: string = '';

  constructor(private requestService: ProfRequestsService) {}

  ngOnInit(): void {
    this.getRequests();
  }

  getRequests() {
    const token = localStorage.getItem("token2");
    if (!token) return;
    console.log(token)
    this.requestService.getProfessorReservations(token).subscribe({
      next: (response) => {
        this.reservations = response;
        this.filteredReservations = [...this.reservations];
      },
      error: (err) => {
        console.error("Failed to fetch reservations", err);
      }
    });
  }

  formatRam(ram: number): string {
    return ram ? `${ram / 1024}GB` : 'N/A';
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleString();
  }

  filterByDate(): void {
    if (!this.startDate && !this.endDate) {
      this.filteredReservations = [...this.reservations];
      return;
    }

    const start = this.startDate ? new Date(this.startDate) : new Date(0); // If no start date, use epoch
    const end = this.endDate ? new Date(this.endDate) : new Date(8640000000000000); // If no end date, use max date

    this.filteredReservations = this.reservations.filter(res => {
      const reservationDate = new Date(res.startTime);
      return reservationDate >= start && reservationDate <= end;
    });
  }

  resetFilters(): void {
    this.startDate = '';
    this.endDate = '';
    this.filteredReservations = [...this.reservations];
  }
}