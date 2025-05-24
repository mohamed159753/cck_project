import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UniversityManageReservationsService } from '../../../services/university-manage-reservations.service';
import { DatePipe, CommonModule } from '@angular/common';
import { UniversitySidebarComponent } from '../../../components/university-sidebar/university-sidebar.component';

interface Resource {
  type: string;
  pricePerHour: number;
}

interface Professor {
  username: string;
  cin: string;
  institute: string;
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
}

@Component({
  selector: 'app-manage-reservations',
  standalone: true,
  imports: [CommonModule, DatePipe, UniversitySidebarComponent],
  templateUrl: './manage-reservations.component.html',
  styleUrls: ['./manage-reservations.component.css'],
  providers: [DatePipe]
})
export class ManageReservationsComponent implements OnInit {
  reservations: Reservation[] = [];
  expandedReservationId: number | null = null;
  approvalStatus = ApprovalStatus; // Make the enum available in the template

  constructor(
    private manageReservations: UniversityManageReservationsService,
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
    const universityId = localStorage.getItem('universityId');
    if (!universityId) return;

    this.manageReservations.getReservations(universityId).subscribe(
      data => {
        this.reservations = data;
      },
      error => console.error('Error loading reservations:', error)
    );
  }

  toggleDetails(reservationId: number): void {
    if (this.expandedReservationId === reservationId) {
      this.expandedReservationId = null;
    } else {
      this.expandedReservationId = reservationId;
    }
  }

  approveReservation(reservation: Reservation): void {
    // Check for PENDING_UNIVERSITY status specifically
    if (reservation.status !== ApprovalStatus.PENDING_UNIVERSITY) {
      console.warn('Cannot approve a reservation that is not pending university approval');
      return;
    }

    const originalStatus = reservation.status;
    reservation.status = 'Processing...';

    console.log(`Approving reservation ${reservation.id} with status ${ApprovalStatus.APPROVED_UNIVERSITY}`);
    
    // Send the correct enum value as expected by the backend
    this.manageReservations.updateReservationStatus(reservation.id, ApprovalStatus.APPROVED_UNIVERSITY).subscribe(
      (response) => {
        console.log('Approval response:', response);
        reservation.status = ApprovalStatus.APPROVED_UNIVERSITY;
        console.log(`Reservation #${reservation.id} has been approved successfully`);
        this.cdRef.detectChanges();
        this.loadReservations();
      },
      error => {
        console.error('Error approving reservation:', error);
        console.error('Status:', error.status);
        console.error('Message:', error.message);
        console.error('Error details:', error.error);
        reservation.status = originalStatus;
      }
    );
  }

  declineReservation(reservation: Reservation): void {
    // Check for PENDING_UNIVERSITY status specifically
    if (reservation.status !== ApprovalStatus.PENDING_UNIVERSITY) {
      console.warn('Cannot decline a reservation that is not pending university approval');
      return;
    }

    const originalStatus = reservation.status;
    reservation.status = 'Processing...';

    console.log(`Declining reservation ${reservation.id} with status ${ApprovalStatus.REJECTED_UNIVERSITY}`);
    
    // Send the correct enum value as expected by the backend
    this.manageReservations.updateReservationStatus(reservation.id, ApprovalStatus.REJECTED_UNIVERSITY).subscribe(
      (response) => {
        console.log('Decline response:', response);
        reservation.status = ApprovalStatus.REJECTED_UNIVERSITY;
        console.log(`Reservation #${reservation.id} has been declined successfully`);
        this.cdRef.detectChanges();
        this.loadReservations();
      },
      error => {
        console.error('Error declining reservation:', error);
        console.error('Status:', error.status);
        console.error('Message:', error.message);
        console.error('Error details:', error.error);
        reservation.status = originalStatus;
      }
    );
  }

  getStatusClass(status: string): string {
    switch (status) {
      case ApprovalStatus.PENDING_UNIVERSITY:
        return 'bg-warning text-dark';
      case ApprovalStatus.APPROVED_UNIVERSITY:
        return 'bg-success text-white';
      case ApprovalStatus.REJECTED_UNIVERSITY:
        return 'bg-danger text-white';
      case ApprovalStatus.PENDING_CCK:
        return 'bg-warning text-dark';
      case ApprovalStatus.APPROVED_CCK:
        return 'bg-success text-white';
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

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return this.datePipe.transform(date, 'dd/MM/yyyy HH:mm:ss') || dateString;
  }
}