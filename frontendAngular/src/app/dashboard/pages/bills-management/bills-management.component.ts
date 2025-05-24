import { Component, AfterViewInit, OnInit } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgFor, NgIf, CommonModule } from '@angular/common';
import { SidebarComponent } from '../../sidebar/sidebar.component';
import { InvoiceService } from '../../../services/invoice-service.service';

@Component({
  selector: 'app-bills-management',
  standalone: true,
  imports: [SidebarComponent, FormsModule, ReactiveFormsModule, NgFor, NgIf, CommonModule],
  templateUrl: './bills-management.component.html',
  styleUrl: './bills-management.component.css'
})
export class BillsManagementComponent implements AfterViewInit, OnInit {
  invoices: any[] = [];
  dataSource: any[] = []; // used for current page
  loading = false;
  error = '';

  // Reactive form controls
  monthControl = new FormControl('');
  universityControl = new FormControl('');

  availableMonths: { value: string; viewValue: string }[] = [];
  universities: { universityId: string; universityName: string }[] = [];

  // Summary
  summaryData = {
    totalInvoices: 0,
    totalAmount: 0,
    paidAmount: 0,
    pendingAmount: 0,
    overdueAmount: 0
  };

  // Pagination
  currentPage = 1;
  pageSize = 10;
  totalPages = 1;
  totalPagesArray: number[] = [];

  constructor(private invoiceService: InvoiceService) {}

  ngAfterViewInit(): void {}

  ngOnInit(): void {
    this.generateMonthOptions();
    this.loadAllInvoices();

    // React to filter changes
    this.monthControl.valueChanges.subscribe(() => this.applyFilters());
    this.universityControl.valueChanges.subscribe(() => this.applyFilters());
  }

  generateMonthOptions() {
    const today = new Date();
    for (let i = 0; i < 12; i++) {
      const monthDate = new Date(today.getFullYear(), today.getMonth() - i, 1);
      const year = monthDate.getFullYear();
      const month = String(monthDate.getMonth() + 1).padStart(2, '0');
      this.availableMonths.push({
        value: `${year}-${month}`,
        viewValue: new Intl.DateTimeFormat('en-US', { month: 'long', year: 'numeric' }).format(monthDate)
      });
    }
  }

  loadAllInvoices() {
    this.loading = true;
    this.invoiceService.getAllInvoices().subscribe({
      next: (data: any[]) => {
        this.invoices = data;
        this.extractUniversities(data);
        this.updateSummaryData(data);
        this.setupPagination();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load invoices.';
        console.error(err);
        this.loading = false;
      }
    });
  }

  extractUniversities(invoices: any[]) {
    const unique = new Map();
    invoices.forEach(inv => {
      if (inv.university && !unique.has(inv.university.universityId)) {
        unique.set(inv.university.universityId, inv.university);
      }
    });
    this.universities = Array.from(unique.values());
  }

  updateSummaryData(invoices: any[]) {
    this.summaryData = {
      totalInvoices: invoices.length,
      totalAmount: 0,
      paidAmount: 0,
      pendingAmount: 0,
      overdueAmount: 0
    };

    invoices.forEach(inv => {
      const amount = +inv.totalAmount || 0;
      this.summaryData.totalAmount += amount;
      switch ((inv.status || '').toLowerCase()) {
        case 'paid':
          this.summaryData.paidAmount += amount;
          break;
        case 'pending':
          this.summaryData.pendingAmount += amount;
          break;
        case 'overdue':
          this.summaryData.overdueAmount += amount;
          break;
      }
    });
  }

  applyFilters() {
    const month = this.monthControl.value;
    const universityId = this.universityControl.value;

    this.loading = true;
    this.error = '';

    const handler = (data: any[]) => {
      this.invoices = data;
      this.updateSummaryData(data);
      this.setupPagination();
      this.loading = false;
    };

    if (month && universityId) {
      this.invoiceService.getInvoicesByUniversityAndMonth(universityId, month).subscribe({ next: handler, error: () => this.handleError() });
    } else if (month) {
      this.invoiceService.getInvoicesByMonth(month).subscribe({ next: handler, error: () => this.handleError() });
    } else if (universityId) {
      this.invoiceService.getInvoicesForUniversity(universityId).subscribe({ next: handler, error: () => this.handleError() });
    } else {
      this.loadAllInvoices();
    }
  }

  resetFilters() {
    this.monthControl.setValue('');
    this.universityControl.setValue('');
  }

  handleError() {
    this.error = 'Failed to apply filter.';
    this.loading = false;
  }

  setupPagination() {
    this.currentPage = 1;
    this.totalPages = Math.ceil(this.invoices.length / this.pageSize);
    this.totalPagesArray = Array.from({ length: this.totalPages }, (_, i) => i + 1);
    this.updateDataSource();
  }

  updateDataSource() {
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.dataSource = this.invoices.slice(start, end);
  }

  changePage(page: number) {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.updateDataSource();
  }

  exportToCSV() {
    const currentMonth = this.monthControl.value || '';
    this.invoiceService.downloadInvoicesCsv(currentMonth).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `invoices-${currentMonth || 'all'}.csv`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: () => {
        this.error = 'Failed to download CSV.';
      }
    });
  }

  viewInvoiceDetails(invoice: any) {
    console.log('Invoice details:', invoice);
    // Add dialog or routing logic here
  }

  updateInvoiceStatus(invoice: any, newStatus: string) {
    console.log(`Change status of invoice ${invoice.invoiceId} to ${newStatus}`);
    // Add API call here if needed
  }
}
