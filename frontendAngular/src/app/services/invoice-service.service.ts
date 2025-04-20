import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Invoice {
    invoiceId: number;
    totalAmount: number;
    issueDate: string;
    dueDate: string;
    status: 'Paid' | 'Unpaid';
}

export const BASE_URL = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class InvoiceService {
  private baseUrl = `${BASE_URL}/invoices`; // Adjusted to use constant
  
  constructor(private http: HttpClient) {}

  getAllInvoices(): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(this.baseUrl);
  }

  getInvoiceById(id: number) {  // Change to number type for consistency
    return this.http.get<Invoice>(`${this.baseUrl}/${id}`);
  }
}
