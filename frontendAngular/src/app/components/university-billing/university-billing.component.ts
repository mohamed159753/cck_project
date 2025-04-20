import { Component, OnInit } from '@angular/core';
import { UniversitySidebarComponent } from "../university-sidebar/university-sidebar.component";
import { HttpClient } from '@angular/common/http';
import { NgClass, NgFor, NgIf ,DatePipe} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router'; // Import Router
import { Invoice, InvoiceService } from '../../services/invoice-service.service';




@Component({
  selector: 'app-university-billing',
  standalone: true,
  imports: [UniversitySidebarComponent, NgClass, NgFor, FormsModule,NgIf,DatePipe],
  templateUrl: './university-billing.component.html',
  styleUrl: './university-billing.component.css'
})

export class UniversityBillingComponent implements OnInit {
  allInvoices: Invoice[] = [];
  invoices: Invoice[] = [];
  statusFilter: string = 'All';
  selectedYear: string = '2025';
  years: string[] = ['2023', '2024', '2025'];

  constructor(
    private invoiceService: InvoiceService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.invoiceService.getAllInvoices().subscribe(data => {
      this.allInvoices = data;
      this.applyFilters();
      console.log("All Invoices:", this.allInvoices);
    });
  }

  

  applyFilters(): void {
    this.invoices = this.allInvoices.filter(invoice => {
      const invoiceDate = new Date(invoice.issueDate);
      const invoiceYear = invoiceDate.getFullYear().toString();  // safer and cleaner
      const matchesYear = invoiceYear === this.selectedYear;
      const matchesStatus = this.statusFilter === 'All' || invoice.status === this.statusFilter;
      return matchesYear && matchesStatus;
    });
  }
  

  onYearChange(year: string): void {
    this.selectedYear = year;
    this.applyFilters();
  }

  onStatusChange(status: string): void {
    this.statusFilter = status;
    this.applyFilters();
  }

  viewDetails(invoice: Invoice): void {
    this.router.navigate(['/universityBillingDetails', invoice.invoiceId]);
  }
}