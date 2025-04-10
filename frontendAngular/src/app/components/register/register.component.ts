import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthSerivce } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports:[CommonModule,FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  constructor(private authSerice:AuthSerivce){}

  username =''
  email = ''
  password = ''

  onSubmit(){
    this.authSerice.register({username:this.username, email:this.email, password:this.password}).subscribe((response)=>{
      console.log(response)
    })
  }


}
