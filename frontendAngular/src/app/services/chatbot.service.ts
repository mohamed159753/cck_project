import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {

  private apiUrl = 'http://localhost:5001';
  constructor(private http : HttpClient) { }

  send_user_input(message:String) : Observable<any>{
    const payload = { message }
    return this.http.post<any>(`${this.apiUrl}/chatbot`,payload)
  }
}
