import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UniversityStats } from '../models/university-stats';
import { InstituteConsumption } from '../models/institute-consumption';
import { ProfessorReservation } from '../models/professor-reservation';


interface DashboardResponse {
  approved: number;
  reservations: number;
  ramQuota: number;
  storageQuota: number;
  usageStatistics: {
    [month: string]: {
      vcpu: number;
      storage: number;
      ram: number;
    };
  };
  pending: number;
  toBe: number;
  currentUse: number;
  vcpuQuota: number;
  totalQuota: number;
  topUniversities: {
    cpuUsage: number;
    name: string;
  }[];
}
@Injectable({
  providedIn: 'root'
})
export class UniversityReportsServiceService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

getUniDashboardStatistics(id: string): Observable<DashboardResponse> {
    return this.http.get<DashboardResponse>(`${this.apiUrl}/admin/dashboard/university/${id}`);
  }

  getTopInstitutes(universityId: number, month: number, year: number): Observable<InstituteConsumption[]> {
    return this.http.get<InstituteConsumption[]>(`${this.apiUrl}/dashboard/institutes/${universityId}/${month}/${year}`);
  }

  getTopProfessors(universityId: number, month: number, year: number): Observable<ProfessorReservation[]> {
    return this.http.get<ProfessorReservation[]>(`${this.apiUrl}/dashboard/professors/${universityId}/${month}/${year}`);
  }
}
