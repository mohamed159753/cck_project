import { Component } from '@angular/core';
import { NgFor,NgClass,NgIf } from '@angular/common';
import { Router } from '@angular/router'; // Import Router
import { PlansService } from '../../services/plans.service';
import { AuthSerivce } from '../../services/auth.service';

@Component({
  selector: 'app-plans',
  standalone: true,
  imports: [NgClass,NgFor,NgIf],
  templateUrl: './plans.component.html',
  styleUrl: './plans.component.css'
})
export class PlansComponent {
  constructor(
      private router: Router,
      private plansService:PlansService,
      private authService:AuthSerivce
    ) { } 


    ngOnInit(): void{
      this.getPlans();
    }
  
    plans:any

  getPlans() {
    this.plansService.getPlans().subscribe((response)=>{

      this.plans = response;

    })
  }

  getPlan(planName: string) {
    const universityId = localStorage.getItem('universityId');
    const universityName = localStorage.getItem('universityName');
  
    if (!universityId || !universityName) {
      console.error('University info is missing');
      return;
    }
  
    const payload = {
      name: universityName,
      planName: planName
        };
  
    this.plansService.uniregister(universityId,payload).subscribe({
      next: () => {
        // Save success → redirect to dashboard
        this.router.navigate(['/university/reports']);
      },
      error: (err) => {
        console.error('Failed to register university:', err);
      } 
    });
  }
  

  getColorClass(name: string): string {
    switch (name.toLowerCase()) {
      case 'basic': return 'bg-blue-50';
      case 'standard': return 'bg-yellow-50';
      case 'premium': return 'bg-red-50';
      default: return '';
    }
  }
  

}


