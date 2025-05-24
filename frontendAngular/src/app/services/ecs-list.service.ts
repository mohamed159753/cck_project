import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EcsListService {

  private apiUrl = 'http://localhost:8080/api/resources';
  private baseUrl = 'http://localhost:5002'


  constructor(private http: HttpClient) {}

  getActivePaygEcs(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/active-payg`);
  }

  getActiveEcs(): Observable<any[]> {
    const token = localStorage.getItem("token2")
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    
    return this.http.get<any[]>(`${this.apiUrl}/active`,{headers});
  }

  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };


  startEcs(ecsId: string): Observable<any> {
    const payload = { ecs_id: ecsId };
    return this.http.post(`${this.baseUrl}/start-ecs`, payload, this.httpOptions);
  }

  stopEcs(ecsId: string): Observable<any> {
    const payload = { ecs_id: ecsId };
    return this.http.post(`${this.baseUrl}/stop-ecs`, payload, this.httpOptions);
  }
}
