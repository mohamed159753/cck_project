import { Component, OnInit } from '@angular/core';
import { UniversitySidebarComponent } from "../university-sidebar/university-sidebar.component";
import { HttpClient } from '@angular/common/http';
import { NgClass, NgFor, NgIf, DatePipe } from '@angular/common';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Invoice, InvoiceService, MonthData } from '../../services/invoice-service.service';

@Component({
  selector: 'app-university-billing',
  standalone: true,
  imports: [UniversitySidebarComponent, NgClass, NgFor, FormsModule, NgIf, DatePipe,ReactiveFormsModule],
  templateUrl: './university-billing.component.html',
  styleUrl: './university-billing.component.css'
})



export class UniversityBillingComponent implements OnInit {
  invoices: Invoice[] = [];
  filteredInvoices: Invoice[] = [];
  loading = false;
  error = '';

  
  
  // Current university ID (would come from authentication service in a real app)
  universityId: any;
  
  // Form controls
  monthControl = new FormControl('');
  
  // Available months for filtering (would be dynamic in real app)
  availableMonths: MonthData[] = [];
  
  displayedColumns: string[] = ['month', 'fixedAmount', 'paygTotal', 'totalAmount', 'status', 'dueDate', 'actions'];

  constructor(private invoiceService: InvoiceService, private router:Router) {
    // Set university ID from current logged-in user
    // In a real app, this would come from an auth service
    this.universityId = localStorage.getItem("universityId");
  }

  ngOnInit(): void {
    this.generateMonthOptions();
    this.loadInvoices();
    
    // Set up filter change listener
    this.monthControl.valueChanges.subscribe(month => {
      this.filterInvoices(month);
    });
  }

  loadInvoices() {
    this.loading = true;
    this.invoiceService.getInvoicesForUniversity(this.universityId)
      .subscribe({
        next: (data) => {
          this.invoices = data;
          this.filteredInvoices = [...this.invoices];
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Failed to load invoices. Please try again later.';
          console.error('Error loading invoices:', err);
          this.loading = false;
        }
      });
  }

  filterInvoices(month: string | null) {
    if (!month) {
      this.filteredInvoices = [...this.invoices];
      return;
    }
    
    this.filteredInvoices = this.invoices.filter(invoice => invoice.month === month);
  }

generateMonthOptions() {
  this.availableMonths = []; // reset if needed

  const today = new Date();

  for (let i = 0; i < 12; i++) {
    const monthDate = new Date(today.getFullYear(), today.getMonth() - i, 1);

    const year = monthDate.getFullYear();
    const month = String(monthDate.getMonth() + 1).padStart(2, '0'); // 1-based month

    const monthValue = `${year}-${month}`;

    const monthViewValue = new Intl.DateTimeFormat('en-US', { month: 'long', year: 'numeric' }).format(monthDate);

    this.availableMonths.push({
      value: monthValue,
      viewValue: monthViewValue
    });
  }
}
  
  downloadInvoice(invoice: Invoice) {
    // In a real app, you might want to download a specific invoice
    // This is a simplified version that uses the export endpoint
    this.invoiceService.downloadInvoicesCsv(invoice.month)
      .subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `invoice-${invoice.month}.csv`;
          document.body.appendChild(a);
          a.click();
          window.URL.revokeObjectURL(url);
          document.body.removeChild(a);
        },
        error: (err) => {
          console.error('Error downloading invoice:', err);
          this.error = 'Failed to download invoice.';
        }
      });
  }
  
  viewInvoiceDetails(invoice: Invoice) {
    // In a real app, this would navigate to a detail view
    console.log('View invoice details:', invoice);
    this.router.navigate([`university/billing-details/${invoice.invoiceId}`])
  }
}


