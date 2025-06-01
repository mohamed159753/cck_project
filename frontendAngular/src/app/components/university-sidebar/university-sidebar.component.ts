import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-university-sidebar',
  standalone: true,
  imports: [CommonModule,RouterLink],
  templateUrl: './university-sidebar.component.html',
  styleUrl: './university-sidebar.component.css'
})
export class UniversitySidebarComponent {

    constructor(private router: Router) {}


  isMobileMenuOpen = false;

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;

  }

  logout() {
    localStorage.removeItem('TOKEN');
    localStorage.removeItem('adminId');
    localStorage.removeItem('universityId');
    localStorage.removeItem('universityName');
    this.router.navigate(['/login']);
  }

}
