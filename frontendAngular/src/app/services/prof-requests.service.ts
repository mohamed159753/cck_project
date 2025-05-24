import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfRequestsService {

  private apiUrl = 'http://localhost:8080/api'; // Change this to your backend URL

  constructor(private http: HttpClient) {}
  
    getProfessorReservations(token: any): Observable<any> {
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      return this.http.get<any>(`${this.apiUrl}/reservations/professor/reservations`, { headers });
    }
}
