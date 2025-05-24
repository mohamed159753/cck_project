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

  constructor(private authService:AuthSerivce){}


  ProfessorCredentials = {
    projectId: '',
    institut : '',
    username: '',
    email:'',
    cin:'',
    password: ''
  };

  projects: any[] = [];
  isLoadingProjects = false;

  ngOnInit() {
    this.loadProjects();
  }

  loadProjects() {
    this.isLoadingProjects = true;
    this.authService.getUni().subscribe(
      (response: any) => {
        this.projects = response || []; 
        console.log(this.projects);
        this.isLoadingProjects = false;
      },
      (error) => {
        console.error('Failed to load projects', error);
        this.isLoadingProjects = false;
      }
    );
  }


  onSubmit(){
    console.log(this.ProfessorCredentials.projectId)
    this.authService.register({
      projectId:this.ProfessorCredentials.projectId,
       username:this.ProfessorCredentials.username, 
       email:this.ProfessorCredentials.email,
       cin:this.ProfessorCredentials.cin, 
       password:this.ProfessorCredentials.password,
        institut:this.ProfessorCredentials.institut}).subscribe((response)=>{
      console.log(response)
    })
  }


}
