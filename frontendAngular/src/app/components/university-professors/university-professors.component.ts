import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UniversitySidebarComponent } from "../university-sidebar/university-sidebar.component";

interface Professor {
  id?: number;
  username: string;
  email: string;
  institute: string;
  password?: string;
}

@Component({
  selector: 'app-university-professors',
  standalone:true,
  imports: [CommonModule, FormsModule, UniversitySidebarComponent],
  templateUrl: './university-professors.component.html',
  styleUrls: ['./university-professors.component.css']
})
export class UniversityProfessorsComponent implements OnInit {
  universityId: any;
  professors: Professor[] = [];
  newProfessor: Professor = { username: '', email: '', institute: '' };
  editingProfessorId: number | null = null;

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    this.universityId = localStorage.getItem("universityId");
    this.loadProfessors();
  }

  loadProfessors(): void {
  const url = `http://localhost:8080/api/admin/universities/${this.universityId}/professors`;

  this.http.get<Professor[]>(url).subscribe({
    next: (response) => {
      this.professors = response;
    },
    error: (err) => {
      console.error('Error loading professors:', err);
      alert('Failed to load professors. See console for details.');
    }
  });
}

  createProfessor() {
    this.http.post<Professor>(`http://localhost:8080/api/admin/universities/${this.universityId}/professors`, this.newProfessor)
      .subscribe(() => {
        this.newProfessor = { username: '', email: '', institute: '' };
        this.loadProfessors();
      });
  }

  updateProfessor(professor: Professor) {
    if (!professor.id) return;
    this.http.put<Professor>(`http://localhost:8080/api/admin/professors/${professor.id}`, professor)
      .subscribe(() => {
        this.editingProfessorId = null;
        this.loadProfessors();
      });
  }

  deleteProfessor(id: number) {
    if (!confirm('Are you sure you want to delete this professor?')) return;
    this.http.delete(`http://localhost:8080/api/admin/professors/${id}`)
      .subscribe(() => this.loadProfessors());
  }

  editProfessor(professor: Professor) {
    this.editingProfessorId = professor.id || null;
    this.newProfessor = { ...professor };
  }

  cancelEdit() {
    this.editingProfessorId = null;
    this.newProfessor = { username: '', email: '', institute: '' };
  }
}
