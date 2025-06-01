import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CckManageReservationsServiceService {

  private apiUrl = 'http://localhost:8080/api';
  
    constructor(private http: HttpClient) { }
  
    getReservations(startDate?: Date, endDate?: Date): Observable<any> {
      let params = new HttpParams();
  
      if (startDate) {
        params = params.set('startDate', startDate.toISOString());
      }
  
      if (endDate) {
        params = params.set('endDate', endDate.toISOString());
      }
  
      return this.http.get<any>(`${this.apiUrl}/admin/reservations`)
        .pipe(catchError(this.handleError));
    }
  
  updateReservationStatus(reservationId: number, status: string,adminId:any): Observable<any> {
      // Backend expects a request body with a status field as per the controller
      return this.http.put<any>(`${this.apiUrl}/reservations/${reservationId}/status`, {
        status: status,adminId:adminId
      });
    }
  
    private handleError(error: HttpErrorResponse) {
      let errorMessage = '';
      if (error.error instanceof ErrorEvent) {
        // Client-side error
        errorMessage = `Error: ${error.error.message}`;
      } else {
        // Server-side error
        errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
      }
      console.error(errorMessage);
      return throwError(() => new Error(errorMessage));
    }
  }
  