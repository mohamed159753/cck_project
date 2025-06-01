import { Component } from '@angular/core';
import { AuthSerivce } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-login',
  standalone: true,
  imports:[CommonModule,FormsModule],
  templateUrl: './admin-login.component.html',
  styleUrl: './admin-login.component.css'
})
export class AdminLoginComponent {

  username: string = '';
  password: string = '';

  constructor(private authService : AuthSerivce, private router:Router){}

  onSubmit() {
    const credentials = { username: this.username, password: this.password };

    this.authService.adminLogin(credentials).subscribe({
      next: (response) => {
        const email = response.user_info.token.user.name;
        localStorage.setItem('TOKEN', response.TOKEN);

        this.authService.registerCckAdmin(email).subscribe({
          next: (res: any) => {
            localStorage.setItem('adminId', res.id);
            this.router.navigate(['/cck/reports']);
          },
          error: (err) => {
            console.error('Registration error', err);
            // Redirect to plans page or show error
            this.router.navigate(['/cck/plans']);
          }
        });
      },
      error: (err) => {
        console.error('Login error', err);
        // Optional: show error message to user
      }
    });
  }
}