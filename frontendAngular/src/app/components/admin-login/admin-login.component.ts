import { Component } from '@angular/core';
import { AuthSerivce } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

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

  constructor(private authService : AuthSerivce){}

  onSubmit(){

    const credentials = { username: this.username, password: this.password }

    this.authService.adminLogin(credentials).subscribe((response)=>{
      console.log(response)
    })

  }

}
