import { Component } from '@angular/core';
import { AuthSerivce } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

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

  

  universityCredentials = {
    projectId: '',
    username: '',
    password: ''
  };

  loginType: 'professor' | 'university' = 'professor';
  projects: any[] = [];
  isLoadingProjects = false;

  constructor(private authService: AuthSerivce , private router : Router) {}

  

  ngOnInit() {
    this.loadProjects();
  }

  loadProjects() {
    this.isLoadingProjects = true;
    this.authService.getProjects().subscribe(
      (response: any) => {
        this.projects = response.projects || []; 
        console.log(this.projects);
        this.isLoadingProjects = false;
      },
      (error) => {
        console.error('Failed to load projects', error);
        this.isLoadingProjects = false;
      }
    );
  }

  
  

  setLoginType(type: 'professor' | 'university') {
    this.loginType = type;
  }

 

  onUniversityLogin() {
    console.log('University admin login attempt:', this.universityCredentials);

    this.authService.uniLogin(this.universityCredentials).subscribe((response)=>{
      console.log(response)
    })
  }




  onSubmit() {
    const credentials = { username: this.username, password: this.password };
    this.authService.login(credentials).subscribe(
      (response) => {
        console.log(response);
        this.router.navigate(['/rev']);
      },
      (error) => {
        console.error(error);
      }
    );
  }
}
