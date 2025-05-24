import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-university-sidebar',
  standalone: true,
  imports: [CommonModule,RouterLink],
  templateUrl: './university-sidebar.component.html',
  styleUrl: './university-sidebar.component.css'
})
export class UniversitySidebarComponent {

  isMobileMenuOpen = false;

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;

  }

}
