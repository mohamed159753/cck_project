import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UpdateProfessorService } from '../../services/update-professor.service';
import { CommonModule } from '@angular/common';
import { ProfSidebarComponent } from "../../prof-sidebar/prof-sidebar.component";

@Component({
  selector: 'app-edit-profile',
  standalone:true,
  imports: [ReactiveFormsModule, CommonModule, ProfSidebarComponent],
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {
  profileForm!: FormGroup;
  loading = false;
  success = false;
  error = '';

  constructor(
    private professorService: UpdateProfessorService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
  this.profileForm = this.fb.group({
    username: [{ value: '', disabled: true }, Validators.required],
    email: ['', [Validators.required, Validators.email]],
    institute: [{ value: '', disabled: true }],
    password: [''],
    universityId: [{ value: '', disabled: true }]
  });

  this.loadProfile();
}

loadProfile(): void {
  this.loading = true;
  const token = localStorage.getItem("token2");
  this.professorService.getOwnProfile(token).subscribe({
    next: (prof) => {
      console.log(prof)
      this.profileForm.patchValue({
        username: prof.username,
        email: prof.email,
        institute: prof.institute,
        universityId: prof.university?.universityName || ''
      });
      this.loading = false;
    },
    error: () => {
      this.error = 'Failed to load profile.';
      this.loading = false;
    }
  });
}

onSubmit(): void {
  if (this.profileForm.invalid) return;

  const formData = this.profileForm.getRawValue(); // Get disabled fields too
  const updatedProfessor: any = {
    email: formData.email,
  };

  const token = localStorage.getItem("token2");
  this.professorService.updateOwnProfile(updatedProfessor, token).subscribe({
    next: () => {
      this.success = true;
      this.error = '';
      // New: Show message telling user to confirm email change
      alert('If you changed your email, please check the new email inbox and click the confirmation link to finalize the update.');
    },
    error: () => {
      this.success = false;
      this.error = 'Failed to update profile.';
    }
  });
}

}
