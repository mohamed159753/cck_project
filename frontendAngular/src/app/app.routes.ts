import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfRevComponent } from './components/prof-rev/prof-rev.component';
import { AdminLoginComponent } from './components/admin-login/admin-login.component';
import { ProfDashboardComponent } from './components/prof-dashboard/prof-dashboard.component';

export const appRoutes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },  // Default redirect to /login
  { path: 'login', component: LoginComponent },
  { path: 'register', component:RegisterComponent},
  {path : 'rev', component:ProfRevComponent},
  {path : 'admin/login', component:AdminLoginComponent},
  {path : 'prof-dashboard', component:ProfDashboardComponent}
            // /login route for LoginComponent
  // Add other routes here as needed
];
