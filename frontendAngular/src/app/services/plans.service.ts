import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PlansService {

  private apiUrl = 'http://localhost:8080/api'; // Change this to your backend URL


  constructor(private http : HttpClient) { }

  getPlans(){
    return this.http.get<any>(`${this.apiUrl}/plans`);
  }

  uniregister(id:any ,creds: any): Observable<any> {
      
      return this.http.post<any>(`${this.apiUrl}/universities/${id}`, creds);
    }



  


}
