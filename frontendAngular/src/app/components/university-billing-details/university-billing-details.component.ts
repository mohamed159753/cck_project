import { Component, OnInit } from '@angular/core';
import { UniversitySidebarComponent } from "../university-sidebar/university-sidebar.component";
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule, NgClass, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InvoiceService } from '../../services/invoice-service.service';
import { SidebarComponent } from "../../dashboard/sidebar/sidebar.component";

 interface Invoice {
  invoiceId: number;
  month: string;
  fixedAmount: number;
  paygTotal: number;
  totalAmount: number;
  status: string;
  issueDate: string;
  dueDate: string;
  university: {
    universityId: string;
    universityName: string;
  }

}

@Component({
  selector: 'app-university-billing-details',
  standalone: true,
  imports: [
    UniversitySidebarComponent,
    NgClass,
    NgIf,
    CommonModule,
    FormsModule,
    SidebarComponent
],
  templateUrl: './university-billing-details.component.html',
  styleUrl: './university-billing-details.component.css'
})
export class UniversityBillingDetailsComponent implements OnInit {

  invoice: any;
  invoiceId: number | null = null;
  loading = true;
   isUni = localStorage.getItem("universityName")

  constructor(
    private invoiceService: InvoiceService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.invoiceId = +id;
        this.loadInvoiceDetails();
      }
    });
  }

  loadInvoiceDetails(): void {
    if (this.invoiceId) {
      this.loading = true;
      this.invoiceService.getInvoiceById(this.invoiceId).subscribe({
        next: (data) => {
          this.invoice = data;
          this.loading = false;
        },
        error: (err: any) => {
          console.error('Failed to load invoice:', err);
          this.loading = false;
        }
      });
    }
  }

  payNow(): void {
    alert('Redirecting to payment gateway...');
  }

  downloadPDF(): void {
  if (!this.invoiceId) return;

  this.invoiceService.downloadInvoicePdf(this.invoiceId).subscribe({
    next: (blob:any) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `invoice-${this.invoice?.month}.pdf`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    },
    error: (err:any) => {
      console.error('PDF download failed', err);
      alert('Failed to download PDF.');
    }
  });
}

  goBack(): void {
    if (localStorage.getItem("universityName")){
            this.router.navigate(['/university/billing']);

    }
    else{
            this.router.navigate(['/cck/billing']);

    }
  }
}
