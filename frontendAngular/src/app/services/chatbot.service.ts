import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


export interface UnavailableTimeSlot {
  start: string; // ISO date-time
  end: string;
}
@Injectable({
  providedIn: 'root'
})


export class ChatbotService {

  

  private apiUrl = 'http://localhost:5002';
  private baseUrl = 'http://localhost:8080';

  constructor(private http : HttpClient) { }

  send_user_input(message:any) : Observable<any>{
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

    const token = localStorage.getItem("token2"); // or however you store it


        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    
    return this.http.post<any>(`${this.apiUrl}/createVM`,payload, { headers })
  }

  getUniverstiy(token:any) : Observable<any>{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get('http://localhost:8080/api/professors/university', { headers });  }

    getUniverstiyId(token:any) : Observable<any>{
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      return this.http.get('http://localhost:8080/api/professors/university_id', { headers });  }


    saveReservationToSpring(payload: any) {
      const token = localStorage.getItem("token2"); // or however you store it
      const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);
    
      return this.http.post(
        `${this.baseUrl}/api/reservations/professor`,  // Assuming your endpoint is /api/reservations
        payload,
        { headers }
      );
    }

    saveReservationToSpringPayg(payload: any) {
      const token = localStorage.getItem("token2"); // or however you store it
      const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);
    
      return this.http.post(
        `${this.baseUrl}/api/reservations/professor/payg`,  // Assuming your endpoint is /api/reservations
        payload,
        { headers }
      );
    }

    getUnavailableTimeSlots(universityId: string, vcpu: number, ram: number, storage: number, from: string, to: string) {
  return this.http.get<UnavailableTimeSlot[]>(`${this.baseUrl}/api/reservations/universities/${universityId}/unavailable-times`, {
    params: {
      vcpu: vcpu.toString(),
      ram: ram.toString(),
      storage: storage.toString(),
      from,
      to
    }
  });
  
}

checkAvailability(universityId: string, vcpu: number, ram: number, storage: number, from: string, to: string) {
  const params = new HttpParams()
    .set('vcpu', vcpu.toString())
    .set('ram', ram.toString())
    .set('storage', storage.toString())
    .set('from', from)
    .set('to', to);

  const token = localStorage.getItem('token2');
  const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

  return this.http.get(`${this.baseUrl}/api/reservations/universities/${universityId}/availability-check`, 
    { headers, params });
}

updateReservationWithVmId(payload: { reservationId: number, vm_id: string }) {
  return this.http.put(`${this.baseUrl}/api/reservations/${payload.reservationId}/vm`, { vm_id: payload.vm_id });
}
}


 
