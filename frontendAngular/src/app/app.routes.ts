import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register/register.component';
import { ProfRevComponent } from './components/prof-rev/prof-rev.component';
import { AdminLoginComponent } from './components/admin-login/admin-login.component';
import { ProfDashboardComponent } from './components/prof-dashboard/prof-dashboard.component';
import { LoginComponent } from './components/login/login.component'; // Adjust the path to your LoginComponent
import { DashboardComponent } from './dashboard/dashboard.component';
import { PlansComponent } from './components/plans/plans.component';
import { UniversityBillingComponent } from './components/university-billing/university-billing.component';
import { UniversityReportsComponent } from './components/university-reports/university-reports.component';
import { ProfRequestsComponent } from './components/prof-requests/prof-requests.component';
import { ProfReportsComponent } from './components/prof-reports/prof-reports.component';
import { UniversityBillingDetailsComponent } from './components/university-billing-details/university-billing-details.component';
import { ManageReservationsComponent } from './dashboard/pages/manage-reservations/manage-reservations.component';
import { CckManageReservationsComponent } from './components/cck-manage-reservations/cck-manage-reservations.component';
import { UniversityProfessorsComponent } from './components/university-professors/university-professors.component';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { EcsListComponent } from './components/ecs-list/ecs-list.component';






export const appRoutes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'register', component:RegisterComponent},
  { path: 'login', component: LoginComponent },



  {path: 'prof', 
    component: DashboardComponent,
    children: [
      { path:'reports',component:ProfReportsComponent},
      { path:'reservations', component:ProfRequestsComponent},
      { path : 'request', component:ProfRevComponent},
      { path: 'metrics', component:ProfDashboardComponent},
      { path: 'ecs-list', component:EcsListComponent},
      { path:"me", component:EditProfileComponent},
      { path:"me/change-password", component:ChangePasswordComponent}
    ]
    
  },
   {path: 'university', 
    component: DashboardComponent,
    children: [
      { path: 'reservations', component:ManageReservationsComponent},
      { path: 'billing', component:UniversityBillingComponent},
      { path: 'reports', component:UniversityReportsComponent},
      { path: 'professors', component:UniversityProfessorsComponent},
      { path: 'billing-details/:id', component:UniversityBillingDetailsComponent},
      { path: 'plans',component: PlansComponent },

    ]
    
  },
  {path:'cck',
    component:DashboardComponent,
    children:[

      { path: 'reports', loadComponent: () => import('./dashboard/pages/reports/reports.component').then(m => m.ReportsComponent ) },
      { path: 'billing', loadComponent: () => import('./dashboard/pages/bills-management/bills-management.component').then(m => m.BillsManagementComponent) },
      { path: 'reservations', component:CckManageReservationsComponent },
      { path: 'billing-details/:id', component:UniversityBillingDetailsComponent},
      { path: 'login',component:AdminLoginComponent}


    ]
  }
  


  
];
