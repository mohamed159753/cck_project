import { Component } from '@angular/core';
import { NgFor,NgClass,NgIf } from '@angular/common';
import { Router } from '@angular/router'; // Import Router

@Component({
  selector: 'app-plans',
  standalone: true,
  imports: [NgClass,NgFor,NgIf],
  templateUrl: './plans.component.html',
  styleUrl: './plans.component.css'
})
export class PlansComponent {
  constructor(
      private router: Router // Inject Router
    ) { }
  pricingPlans = [
    {
      title: 'Basic',
      icon: '📦',
      popular: false,
      features: [
        'Email & support during working hours',
        'Basic usage statistics',
        'Monthly usage report (PDF)'
      ],
      specs: {
        cpu: '2 vCPUs',
        ram: '4 GB',
        storage: '100 GB',
        network: '100 Mbps'
      },
      price: '20€/month',
      color: 'bg-blue-50'
    },
    {
      title: 'Standard',
      icon: '🔧',
      popular: true,
      features: [
        'Real-time resource tracking dashboard',
        '24/7 technical support',
        'Everything in Basic'
      ],
      specs: {
        cpu: '4 vCPUs',
        ram: '8 GB',
        storage: '250 GB',
        network: '300 Mbps'
      },
      price: '45€/month',
      color: 'bg-yellow-50'
    },
    {
      title: 'Pro',
      icon: '🚀',
      popular: false,
      features: [
        'Early access to new features',
        'Dedicated support agent',
        'Everything in Standard'
      ],
      specs: {
        cpu: '8 vCPUs',
        ram: '16 GB',
        storage: '500 GB',
        network: '1 Gbps'
      },
      price: '90€/month',
      color: 'bg-red-50'
    }
  ];

  getPlan(planTitle: string) {
    console.log(`Selected plan: ${planTitle}`);
    // Add your logic here - e.g., navigate to a signup page or open a modal
    this.router.navigate(['/universityReports']);
  }

}


