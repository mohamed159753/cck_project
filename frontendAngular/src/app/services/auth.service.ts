import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthSerivce {
  private apiUrl = 'http://localhost:8080/api';

  private baseUrl = 'http://localhost:5002'

  constructor(private http: HttpClient) {}

 registerAdmin(email: string, universityId: number) {
  return this.http.post<any>(`${this.apiUrl}/uni-login`, {
    email,
    universityId
  });
}

registerCckAdmin(email: string) {
  return this.http.post<any>(`${this.apiUrl}/cck-login`, email);
}

  login(creds: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, creds);
  }// Send the credentials to the backend

  register(creds: any): Observable<any>{
    return this.http.post<any>(`${this.apiUrl}/register`, creds)
  }

  getAdminToken(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/getAdminToken`).pipe(
      tap((response: { token: string; }) => {
        if (response && response.token) {

          const tokenData = {
            token: response.token,
            timestamp: Date.now() 
          };

          localStorage.setItem('adminToken', JSON.stringify(tokenData)); 
        }
      })
    );
  }

  getStoredAdminToken(): string | null {
    const tokenData = JSON.parse(localStorage.getItem('adminToken') || 'null');
    if (tokenData) {
      const tokenAge = Date.now() - tokenData.timestamp;
      const oneDay = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
      if (tokenAge < oneDay) {
        return tokenData.token;  // Return token if it is within 24 hours
      } else {
        this.clearAdminToken();
        return null;
      }
    }
    return null;
  }

  // Optional: A method to remove the token from localStorage
  clearAdminToken(): void {
    localStorage.removeItem('adminToken');
  }

  getProjects(): Observable<any> {
    
    return this.http.get<any>(`${this.baseUrl}/fetchProjects`)
  }

  getUni(): Observable<any>{
    return this.http.get<any>(`${this.apiUrl}/universities`)
  }

  getUniById(id:any): Observable<any>{
    return this.http.get<any>(`${this.apiUrl}/universities/${id}`)
  }

  adminLogin(creds: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/adminLogin`, creds);
  }

  uniLogin(creds: any): Observable<any> {
    
    return this.http.post<any>(`${this.baseUrl}/uniLogin`, creds);
  }

  




  
}
