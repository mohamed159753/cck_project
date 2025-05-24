import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UpdateProfessorService {

  private apiUrl = 'http://localhost:8080/api/professors'; // update with your backend URL

  constructor(private http: HttpClient) {}

  getOwnProfile(token:any): Observable<any> {

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    
    return this.http.get<any>(`${this.apiUrl}/me`, {headers, withCredentials: true },);
  }

  updateOwnProfile(professor: any,token:any): Observable<any> {
    
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put<any>(`${this.apiUrl}/me`, professor, {headers,withCredentials: true});
  }
  
  changePassword(oldPassword: string, newPassword: string) {
    const token = localStorage.getItem('token2');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    return this.http.put(`${this.apiUrl}/change-password`, {
      currentPassword: oldPassword,
      newPassword: newPassword,
    }, { headers });
  }
}
