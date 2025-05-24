import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfReportsService {

  private apiUrl = 'http://localhost:8080/api'; // Change this to your backend URL
  
    constructor(private http: HttpClient) {}
    
      getProfessorReports(token: any,month:any,year:any): Observable<any> {
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
        return this.http.get<any>(`${this.apiUrl}/reservations/report/professor?month=${month}&year=${year}`, { headers });
      }

      getReservationsStats(token:any): Observable<any>{

        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
        return this.http.get<any>(`${this.apiUrl}/reservations/professor/count`, { headers });
      }
}
