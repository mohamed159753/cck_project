import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DatePipe, CommonModule } from '@angular/common';
import { SidebarComponent } from '../../dashboard/sidebar/sidebar.component';
import { CckManageReservationsServiceService } from '../../services/cck-manage-reservations-service.service';
import { FormsModule } from '@angular/forms';

interface Resource {
  type: string;
  pricePerHour: number;
  vcpu: number;
  ram: number;
  storage: number;
  image: string;
}

interface Professor {
  username: string;
  cin: string;
  institute: string;
  email: string;
}

interface University {
  id: string;
  name: string;
}

// Update to match backend status enum values
enum ApprovalStatus {
  PENDING_UNIVERSITY = 'PENDING_UNIVERSITY',
  APPROVED_UNIVERSITY = 'APPROVED_UNIVERSITY',
  REJECTED_UNIVERSITY = 'REJECTED_UNIVERSITY',
  PENDING_CCK = 'PENDING_CCK',
  APPROVED_CCK = 'APPROVED_CCK',
  REJECTED_CCK = 'REJECTED_CCK'
}

interface Reservation {
  id: number;
  status: string;
  startTime: string;
  endTime: string;
  resource: Resource;
  professor: Professor;
  university: University;
}

@Component({
  selector: 'app-cck-manage-reservations',
  standalone: true,
  imports: [CommonModule, DatePipe, SidebarComponent, FormsModule],
  templateUrl: './cck-manage-reservations.component.html',
  styleUrls: ['./cck-manage-reservations.component.css'],
  providers: [DatePipe]
})
export class CckManageReservationsComponent implements OnInit {
  reservations: Reservation[] = [];
  expandedReservationId: number | null = null;
  approvalStatus = ApprovalStatus; // Make the enum available in the template
  filteredReservations: Reservation[] = [];
  selectedFilter: string = 'all';

  adminId = localStorage.getItem("adminId");

  constructor(
    private manageReservationsService: CckManageReservationsServiceService,
    private datePipe: DatePipe,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadReservations();
    this.initSidebarToggle();
  }

  private initSidebarToggle(): void {
    setTimeout(() => {
      const toggleBtn = document.getElementById('toggleSidebar');
      const sidebar = document.getElementById('sidebar');

      if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', function () {
          sidebar.classList.toggle('expanded');
        });
      }
    }, 0);
  }

  loadReservations(): void {
    this.manageReservationsService.getReservations().subscribe({
      next: (data) => {
        console.log('Raw reservations data:', data); // Debug log
        this.reservations = data;
        this.applyFilter(this.selectedFilter);
        console.log('Filtered reservations:', this.filteredReservations); // Debug log
      },
      error: (error) => {
        console.error('Error loading reservations:', error);
      }
    });
  }

  applyFilter(filterType: string): void {
    console.log('Applying filter:', filterType); // Debug log
    this.selectedFilter = filterType;
    
    // Log all unique statuses to help debug
    const uniqueStatuses = [...new Set(this.reservations.map(r => r.status))];
    console.log('Available statuses in data:', uniqueStatuses);
    
    switch (filterType) {
      case 'pending':
        this.filteredReservations = this.reservations.filter(r => {
          const matches = r.status === ApprovalStatus.PENDING_CCK;
          console.log(`Reservation ${r.id} status: ${r.status}, matches pending: ${matches}`);
          return matches;
        });
        break;
      case 'approved':
        this.filteredReservations = this.reservations.filter(r => {
          const matches = r.status === ApprovalStatus.APPROVED_CCK;
          console.log(`Reservation ${r.id} status: ${r.status}, matches approved: ${matches}`);
          return matches;
        });
        break;
      case 'rejected':
        this.filteredReservations = this.reservations.filter(r => {
          const matches = r.status === ApprovalStatus.REJECTED_CCK;
          console.log(`Reservation ${r.id} status: ${r.status}, matches rejected: ${matches}`);
          return matches;
        });
        break;
      case 'university-approved':
        this.filteredReservations = this.reservations.filter(r => {
          const matches = r.status === ApprovalStatus.APPROVED_UNIVERSITY;
          console.log(`Reservation ${r.id} status: ${r.status}, matches university-approved: ${matches}`);
          return matches;
        });
        break;
      case 'university-pending':
        this.filteredReservations = this.reservations.filter(r => {
          const matches = r.status === ApprovalStatus.PENDING_UNIVERSITY;
          console.log(`Reservation ${r.id} status: ${r.status}, matches university-pending: ${matches}`);
          return matches;
        });
        break;
      case 'university-rejected':
        this.filteredReservations = this.reservations.filter(r => {
          const matches = r.status === ApprovalStatus.REJECTED_UNIVERSITY;
          console.log(`Reservation ${r.id} status: ${r.status}, matches university-rejected: ${matches}`);
          return matches;
        });
        break;
      case 'all':
      default:
        this.filteredReservations = [...this.reservations]; // Create a new array reference
        break;
    }

    console.log(`Filter '${filterType}' applied. Results: ${this.filteredReservations.length} reservations`);
    
    // Force change detection
    this.cdRef.detectChanges();
  }

  // Method to be called from template
  onFilterChange(filterType: string): void {
    this.applyFilter(filterType);
  }

  toggleDetails(reservationId: number): void {
    if (this.expandedReservationId === reservationId) {
      this.expandedReservationId = null;
    } else {
      this.expandedReservationId = reservationId;
    }
  }

  approveReservation(reservation: Reservation): void {
    // CCK can only approve reservations that are already approved by university
    if (reservation.status !== ApprovalStatus.APPROVED_UNIVERSITY && 
        reservation.status !== ApprovalStatus.PENDING_CCK) {
      console.warn('CCK can only approve reservations that are approved by university or pending CCK approval');
      return;
    }

    const originalStatus = reservation.status;
    reservation.status = 'Processing...';

    console.log(`CCK approving reservation ${reservation.id} with status ${ApprovalStatus.APPROVED_CCK}`);
    this.manageReservationsService.updateReservationStatus(reservation.id, ApprovalStatus.APPROVED_CCK, this.adminId).subscribe({
      next: (response) => {
        console.log('Approval response:', response);
        reservation.status = ApprovalStatus.APPROVED_CCK;
        console.log(`Reservation #${reservation.id} has been approved by CCK successfully`);
        this.cdRef.detectChanges();
        // Reload and reapply current filter
        this.loadReservations();
      },
      error: (error) => {
        console.error('Error approving reservation:', error);
        console.error('Status:', error.status);
        console.error('Message:', error.message);
        console.error('Error details:', error.error);
        reservation.status = originalStatus;
        this.cdRef.detectChanges();
      }
    });
  }

  rejectReservation(reservation: Reservation): void {
    // CCK can only reject reservations that are already approved by university
    if (reservation.status !== ApprovalStatus.APPROVED_UNIVERSITY && 
        reservation.status !== ApprovalStatus.PENDING_CCK) {
      console.warn('CCK can only reject reservations that are approved by university or pending CCK approval');
      return;
    }

    const originalStatus = reservation.status;
    reservation.status = 'Processing...';

    console.log(`CCK rejecting reservation ${reservation.id} with status ${ApprovalStatus.REJECTED_CCK}`);
    
    this.manageReservationsService.updateReservationStatus(reservation.id, ApprovalStatus.REJECTED_CCK, this.adminId).subscribe({
      next: (response) => {
        console.log('Rejection response:', response);
        reservation.status = ApprovalStatus.REJECTED_CCK;
        console.log(`Reservation #${reservation.id} has been rejected by CCK successfully`);
        this.cdRef.detectChanges();
        // Reload and reapply current filter
        this.loadReservations();
      },
      error: (error) => {
        console.error('Error rejecting reservation:', error);
        console.error('Status:', error.status);
        console.error('Message:', error.message);
        console.error('Error details:', error.error);
        reservation.status = originalStatus;
        this.cdRef.detectChanges();
      }
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case ApprovalStatus.PENDING_UNIVERSITY:
        return 'bg-warning text-dark';
      case ApprovalStatus.APPROVED_UNIVERSITY:
        return 'bg-primary text-white';  // Use primary color for university approved
      case ApprovalStatus.REJECTED_UNIVERSITY:
        return 'bg-danger text-white';
      case ApprovalStatus.PENDING_CCK:
        return 'bg-info text-white';
      case ApprovalStatus.APPROVED_CCK:
        return 'bg-success text-white';  // Use success color for CCK approved
      case ApprovalStatus.REJECTED_CCK:
        return 'bg-danger text-white';
      case 'EXPIRED':
        return 'bg-secondary text-white';
      case 'Processing...':
        return 'bg-primary text-white';
      default:
        return 'bg-light text-dark';
    }
  }

  getStatusDisplay(status: string): string {
    switch (status) {
      case ApprovalStatus.PENDING_UNIVERSITY:
        return 'Pending University';
      case ApprovalStatus.APPROVED_UNIVERSITY:
        return 'University Approved';
      case ApprovalStatus.REJECTED_UNIVERSITY:
        return 'University Rejected';
      case ApprovalStatus.PENDING_CCK:
        return 'Pending CCK';
      case ApprovalStatus.APPROVED_CCK:
        return 'CCK Approved';
      case ApprovalStatus.REJECTED_CCK:
        return 'CCK Rejected';
      case 'EXPIRED':
        return 'Expired';
      case 'Processing...':
        return 'Processing...';
      default:
        return status;
    }
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return this.datePipe.transform(date, 'dd/MM/yyyy HH:mm:ss') || dateString;
  }

  calculateDuration(startTime: string, endTime: string): string {
    const start = new Date(startTime);
    const end = new Date(endTime);
    const diffMs = end.getTime() - start.getTime();
    const diffHrs = Math.floor(diffMs / (1000 * 60 * 60));
    const diffMins = Math.floor((diffMs % (1000 * 60 * 60)) / (1000 * 60));
    
    return `${diffHrs} hr${diffHrs !== 1 ? 's' : ''} ${diffMins} min${diffMins !== 1 ? 's' : ''}`;
  }

  calculateEstimatedCost(reservation: Reservation): number {
    if (!reservation.resource.pricePerHour) {
      return 0;
    }
    
    const start = new Date(reservation.startTime);
    const end = new Date(reservation.endTime);
    const diffMs = end.getTime() - start.getTime();
    const diffHrs = diffMs / (1000 * 60 * 60);
    
    return reservation.resource.pricePerHour * diffHrs;
  }

  // Helper method to get filter counts
  getFilterCount(filterType: string): number {
    switch (filterType) {
      case 'pending':
        return this.reservations.filter(r => r.status === ApprovalStatus.PENDING_CCK).length;
      case 'approved':
        return this.reservations.filter(r => r.status === ApprovalStatus.APPROVED_CCK).length;
      case 'rejected':
        return this.reservations.filter(r => r.status === ApprovalStatus.REJECTED_CCK).length;
      case 'university-approved':
        return this.reservations.filter(r => r.status === ApprovalStatus.APPROVED_UNIVERSITY).length;
      case 'university-pending':
        return this.reservations.filter(r => r.status === ApprovalStatus.PENDING_UNIVERSITY).length;
      case 'university-rejected':
        return this.reservations.filter(r => r.status === ApprovalStatus.REJECTED_UNIVERSITY).length;
      case 'all':
      default:
        return this.reservations.length;
    }
  }
}