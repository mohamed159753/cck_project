import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-prof-sidebar',
  standalone: true,
  imports: [CommonModule,FormsModule,RouterLink],
  templateUrl: './prof-sidebar.component.html',
  styleUrl: './prof-sidebar.component.css'
})
export class ProfSidebarComponent {

  isMobileMenuOpen = false;

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;

  }

}
