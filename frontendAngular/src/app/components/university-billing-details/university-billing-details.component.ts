import { Component, OnInit } from '@angular/core';
import { UniversitySidebarComponent } from "../university-sidebar/university-sidebar.component";
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule, NgClass, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InvoiceService } from '../../services/invoice-service.service';

interface Invoice {
    invoiceId: number;
    totalAmount: number;
    issueDate: string;
    dueDate: string;
    status: 'Paid' | 'Unpaid';
}

@Component({
  selector: 'app-university-billing-details',
  standalone: true,
  imports: [UniversitySidebarComponent,NgClass,NgIf,CommonModule,FormsModule],
  templateUrl: './university-billing-details.component.html',
  styleUrl: './university-billing-details.component.css'
})
export class UniversityBillingDetailsComponent implements OnInit{

  invoice: Invoice | null = null;
  invoiceId: number | null = null;
  
  constructor(
    private invoiceService: InvoiceService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.invoiceId = +id; // Convert to number ===Number(id);
        this.loadInvoiceDetails();
      }
    });
  }
  
  loadInvoiceDetails(): void {
    if (this.invoiceId) {
      this.invoiceService.getInvoiceById(this.invoiceId).subscribe({
        next: (data) => {
          this.invoice = {
            invoiceId: data.invoiceId,  // Consistent field name
            totalAmount: data.totalAmount,  // Consistent field name
            issueDate: data.issueDate,
            dueDate: data.dueDate,
            status: data.status,
          };
  
          console.log('Loaded invoice with month/year:', this.invoice);
        },
        error: (err) => {
          console.error('Failed to load invoice:', err);
        }
      });
    }
  }

  payNow(): void {
    // Implementation for payment processing
    alert('Redirecting to payment gateway...');
    // After successful payment, you would update the invoice status
    // and possibly redirect back to the billing list
  }

  downloadPDF(): void {
    // Implementation for downloading the invoice as PDF
    alert('Downloading invoice as PDF...');
  }

  goBack(): void {
    this.router.navigate(['/universityBilling']);
  }
}
