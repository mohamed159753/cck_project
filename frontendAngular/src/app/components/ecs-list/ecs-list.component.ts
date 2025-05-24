import { Component } from '@angular/core';
import { EcsListService } from '../../services/ecs-list.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ecs-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ecs-list.component.html',
  styleUrl: './ecs-list.component.css'
})
export class EcsListComponent {

  activePaygEcs: any[] = [];
  loading = true;

  constructor(private ecsService: EcsListService) {}

  ngOnInit(): void {
    this.ecsService.getActiveEcs().subscribe({
      next: (data) => {
        console.log(data)
        this.activePaygEcs = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading ECSs:', err);
        this.loading = false;
      }
    });
  }

  onStart(resource: any): void {
    console.log('Start clicked for:', resource);
    this.ecsService.startEcs(resource.externalId).subscribe({
      next: (response) => {
        console.log('Start Success:', response);
        // Optionally refresh status or show success
      },
      error: (error) => {
        console.error('Start Error:', error);
      }
    });
  }

  onStop(resource: any): void {
    console.log('Stop clicked for:', resource);
    this.ecsService.stopEcs(resource.externalId).subscribe({
      next: (response) => {
        console.log('Stop Success:', response);
        // Optionally refresh status or show success
      },
      error: (error) => {
        console.error('Stop Error:', error);
      }
    });
  }

}
