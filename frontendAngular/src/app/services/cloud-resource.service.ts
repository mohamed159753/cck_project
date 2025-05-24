import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BASE_URL } from './invoice-service.service';

@Injectable({
  providedIn: 'root'
})
export class CloudResourceService {
  private baseUrl = `${BASE_URL}/resources`

  constructor(private http: HttpClient) {}

  getAllResources(): Observable<any> {
    return this.http.get<any>(this.baseUrl);
  }

  getResourcesByType(type: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/type/${type}`);
  }
}