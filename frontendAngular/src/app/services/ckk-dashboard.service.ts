import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

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
export class CkkDashboardService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getDashboardStatistics(): Observable<DashboardResponse> {
    return this.http.get<DashboardResponse>(`${this.apiUrl}/admin/dashboard`);
  }

  getUniDashboardStatistics(id: string): Observable<DashboardResponse> {
    return this.http.get<DashboardResponse>(`${this.apiUrl}/admin/dashboard/university/${id}`);
  }

    getUni(): Observable<any>{
    return this.http.get<any>(`${this.apiUrl}/universities`)
  }
}
