import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register/register.component';
import { ProfRevComponent } from './components/prof-rev/prof-rev.component';


            // /login route for LoginComponent
  // Add other routes here as needed
import { LoginComponent } from './login/login.component'; // Adjust the path to your LoginComponent
import { DashboardComponent } from './dashboard/dashboard.component';



export const appRoutes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'register', component:RegisterComponent},
  {path : 'rev', component:ProfRevComponent},
  { path: 'login', component: LoginComponent },
  { 
    path: 'dashboard', 
    component: DashboardComponent,
    children: [
      { path: 'reports', loadComponent: () => import('./dashboard/pages/reports/reports.component').then(m => m.ReportsComponent ) },
      { path: 'manage-reservations', loadComponent: () => import('./dashboard/pages/manage-reservations/manage-reservations.component').then(m => m.ManageReservationsComponent) },
      { path: 'bills-management', loadComponent: () => import('./dashboard/pages/bills-management/bills-management.component').then(m => m.BillsManagementComponent) },
    ]
  },
  
];
