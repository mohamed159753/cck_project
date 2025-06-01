import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-prof-sidebar',
  standalone: true,
  imports: [CommonModule,FormsModule,RouterLink],
  templateUrl: './prof-sidebar.component.html',
  styleUrl: './prof-sidebar.component.css'
})
export class ProfSidebarComponent {

    constructor(private router: Router) {}


  isMobileMenuOpen = false;

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;

  }

  logout() {
    localStorage.removeItem('token2');
    this.router.navigate(['/login']); // or route by role
  }

}
