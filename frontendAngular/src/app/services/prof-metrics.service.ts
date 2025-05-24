import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProfMetricsService {
  private apiUrl = 'http://localhost:5002/metrics'; // replace with ngrok URL if public

  constructor(private http: HttpClient) {}

  getMetrics(): Observable<any> {
    const token = localStorage.getItem("token2"); // or however you store it
    const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);
    return this.http.get<any>(this.apiUrl, { headers });
  }
}
