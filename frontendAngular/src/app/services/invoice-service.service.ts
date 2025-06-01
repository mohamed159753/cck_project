import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Invoice {
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
  };
  entries?: BillingEntry[];
}

export interface BillingEntry {
  id: number;
  description: string;
  amount: number;
  date: string;
}

export interface MonthData {
  value: string;
  viewValue: string;
}

export const BASE_URL = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class InvoiceService {
  private apiUrl = 'http://localhost:8080/api/invoices';

  constructor(private http: HttpClient) { }

  /**
   * Get all invoices
   */
  getAllInvoices(): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(this.apiUrl);
  }

  /**
   * Get invoices for a specific university
   * @param universityId The ID of the university
   */
  getInvoicesForUniversity(universityId: string): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(`${this.apiUrl}/university/${universityId}`);
  }

  /**
   * Get invoices for a specific month
   * @param month The month in YYYY-MM format
   */
  getInvoicesByMonth(month: string): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(`${this.apiUrl}/month/${month}`);
  }

  /**
   * Get invoices for a specific university and month
   * @param universityId The ID of the university
   * @param month The month in YYYY-MM format
   */
  getInvoicesByUniversityAndMonth(universityId: string, month: string): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(`${this.apiUrl}/university/${universityId}/month/${month}`);
  }

  /**
   * Download invoices for a specific month as CSV
   * @param month The month in YYYY-MM format
   */
  downloadInvoicesCsv(month: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export/${month}`, { 
      responseType: 'blob'
    });
  }

  getInvoiceById(id:any){
    return this.http.get(`${this.apiUrl}/${id}`)
  }

  downloadInvoicePdf(invoiceId: number): Observable<Blob> {
  return this.http.get(`${this.apiUrl}/${invoiceId}/download/pdf`, {
    responseType: 'blob'
  });

 
}

 updateInvoiceStatus(invoiceId:any,status:String){
    return this.http.put<any>(`${this.apiUrl}/${invoiceId}`,status)
  }
}
