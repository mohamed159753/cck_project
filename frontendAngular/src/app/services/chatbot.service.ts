import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {

  private apiUrl = 'http://localhost:5002';

  constructor(private http : HttpClient) { }

  send_user_input(message:String) : Observable<any>{
    const payload = { message }
    return this.http.post<any>(`${this.apiUrl}/chatbot`,payload)
  }

  getFlavors() : Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/fetchFlavors`)

  }

  getImages() : Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/fetchImages`)

  }

  createVM(payload:any) : Observable<any>{
    
    return this.http.post<any>(`${this.apiUrl}/createVM`,payload)
  }
}
