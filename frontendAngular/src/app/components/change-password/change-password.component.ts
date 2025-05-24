import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UpdateProfessorService } from '../../services/update-professor.service';
import { ProfSidebarComponent } from "../../prof-sidebar/prof-sidebar.component";

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, ProfSidebarComponent],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css'
})
export class ChangePasswordComponent {

   changePasswordForm: FormGroup;
  message = '';
  error = '';

  constructor(private fb: FormBuilder, private updatePassword: UpdateProfessorService) {
    this.changePasswordForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onSubmit() {
    if (this.changePasswordForm.invalid) return;

    const { oldPassword, newPassword } = this.changePasswordForm.value;

    this.updatePassword.changePassword(oldPassword, newPassword).subscribe({
      next: () => {
        this.message = 'Password changed successfully!';
        this.error = '';
        this.changePasswordForm.reset();
      },
      error: (err :any) => {
        console.log(err)
        this.error = err.error.message || 'Failed to change password';
        this.message = '';
      },
    });
  }



}
