import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UniversityStats } from '../models/university-stats';
import { InstituteConsumption } from '../models/institute-consumption';
import { ProfessorReservation } from '../models/professor-reservation';

@Injectable({
  providedIn: 'root'
})
export class UniversityReportsServiceService {

  private apiUrl = 'http://localhost:8080/api/dashboard';

  constructor(private http: HttpClient) { }

  getDashboardStats(universityId: number, month: number, year: number): Observable<UniversityStats> {
    return this.http.get<UniversityStats>(`${this.apiUrl}/stats/${universityId}/${month}/${year}`);
    //return this.http.get<UniversityStats>(`${this.apiUrl}/stats/${universityId}/6/${year}`);
  }

  getTopInstitutes(universityId: number, month: number, year: number): Observable<InstituteConsumption[]> {
    return this.http.get<InstituteConsumption[]>(`${this.apiUrl}/institutes/${universityId}/${month}/${year}`);
  }

  getTopProfessors(universityId: number, month: number, year: number): Observable<ProfessorReservation[]> {
    return this.http.get<ProfessorReservation[]>(`${this.apiUrl}/professors/${universityId}/${month}/${year}`);
  }
}
