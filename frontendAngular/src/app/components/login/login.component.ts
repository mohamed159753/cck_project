import { Component } from '@angular/core';
import { AuthSerivce } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

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

  constructor(private authService: AuthSerivce,  private http: HttpClient) {}

  // This is called when the user clicks the Login button
  onSubmit() {
    const credentials = { username: this.username, password: this.password };
    // Send the credentials to the server using AuthService
    this.authService.login(credentials).subscribe(
      (response) => {
        console.log(response);// If successful, log the response (like a token).
      },
      (error) => {
        console.error(error);// If something goes wrong, log the error.
      }
    );
  }

}
