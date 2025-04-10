import { Component } from '@angular/core';
import { LoginService } from '../services/login.service'; // Import the service
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone:true,
  imports:[CommonModule,FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor(private loginService: LoginService) {}

  onSubmit() {
    const credentials = { username: this.username, password: this.password };
    this.loginService.login(credentials).subscribe(
      (response) => {
        console.log(response);
      },
      (error) => {
        console.error( error);
      }
    );
  }
}
