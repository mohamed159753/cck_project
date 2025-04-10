import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthSerivce {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  login(creds: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, creds);
  }

  register(creds: any): Observable<any>{
    return this.http.post<any>(`${this.apiUrl}/register`, creds)
  }
}
