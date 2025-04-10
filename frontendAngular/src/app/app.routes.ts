import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfRevComponent } from './components/prof-rev/prof-rev.component';

export const appRoutes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },  // Default redirect to /login
  { path: 'login', component: LoginComponent },
  { path: 'register', component:RegisterComponent},
  {path : 'rev', component:ProfRevComponent}
            // /login route for LoginComponent
  // Add other routes here as needed
];
