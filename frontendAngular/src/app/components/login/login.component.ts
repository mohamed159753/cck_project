import { Component } from '@angular/core';
import { AuthSerivce } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
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

  

  universityCredentials = {
    projectId: '',
    username: '',
    password: ''
  };

  professorCredentials = {
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
  
    this.authService.uniLogin(this.universityCredentials).subscribe((response: any) => {
      const universityId = response.university.id;
      const universityName = response.university.name;
      const email = response.university.email;
  
      // Save auth token and university ID for later use (e.g., saving later in plans page)
      localStorage.setItem('TOKEN', response.TOKEN);
      localStorage.setItem('universityId', universityId);
      localStorage.setItem('universityName', universityName);

      this.authService.registerAdmin(email, universityId).subscribe((res) => {
         localStorage.setItem('adminId',res.id);
      });
  
      // Step 1: Check if university already exists
      this.authService.getUniById(universityId).subscribe({
        next: (res: any) => {
          // University already exists → go to dashboard
          this.router.navigate(['/university/reports']);
        },
        error: (err) => {
          if (err.status === 404) {
            // University not found → redirect to plans page
            this.router.navigate(['/university/plans']);
          } else {
            console.error('Unexpected error checking university:', err);
          }
        }
      });
    });
  }




  // This is called when the user clicks the Login button
  onSubmit() {
    const credentials = { 
      username: this.professorCredentials.username,
      password: this.professorCredentials.password
    };
    // Send the credentials to the server using AuthService
    this.authService.login(credentials).subscribe(
      (response) => {
        console.log(response);
        if(response.status =='success'){
          localStorage.setItem('token2', response.token);
          this.router.navigate(['/prof/request']);
        }
        
      },
      (error) => {
        if (error.status === 401) {
          alert(error.error.errorMessage);
        } else {
          console.error('Unexpected error:', error);
        }
      }
    );
  }

}
