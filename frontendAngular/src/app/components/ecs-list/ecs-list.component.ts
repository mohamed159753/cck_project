import { Component } from '@angular/core';
import { EcsListService } from '../../services/ecs-list.service';
import { CommonModule } from '@angular/common';
import { ProfSidebarComponent } from '../../prof-sidebar/prof-sidebar.component';

@Component({
  selector: 'app-ecs-list',
  standalone: true,
  imports: [CommonModule, ProfSidebarComponent],
  templateUrl: './ecs-list.component.html',
  styleUrl: './ecs-list.component.css'
})
export class EcsListComponent {
  activePaygEcs: any[] = [];
  activeEcs: any[] = [];
  loading = true;

  vncUrls: { [externalId: string]: string } = {};

  constructor(private ecsService: EcsListService) {}

  ngOnInit(): void {
    this.refreshEcsLists();
  }

  private refreshEcsLists(): void {
    this.loading = true;

    this.ecsService.getActiveEcs().subscribe({
      next: (data) => {
        this.activeEcs = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error refreshing ECSs:', err);
        this.loading = false;
      }
    });

    this.ecsService.getActivePaygEcs().subscribe({
      next: (data) => {
        this.activePaygEcs = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error refreshing PAYG ECSs:', err);
        this.loading = false;
      }
    });
  }

  onRegenerateVnc(ecs: any) {
    console.log("generate")
    this.ecsService.regenerateVncLink(ecs.externalId).subscribe({
      next: (res) => {
        this.vncUrls[ecs.externalId] = res.vnc_url;
        console.log(this.vncUrls[ecs.externalId]);
      },
      error: (err) => {
        console.error('Failed to regenerate VNC link', err);
        alert('Failed to regenerate VNC link.');
      }
    });
  }

  onStart(resource: any): void {
    this.ecsService.startEcs(resource.externalId).subscribe({
      next: (response) => {
        console.log('Start Success:', response);
        setTimeout(() => {
          this.refreshEcsLists();
        }, 2000);

        

        
      this.ecsService.logStart(resource.id).subscribe({
          next: (logResponse) => {
            console.log('Log Start Success:', logResponse);
          },
          error: (logError) => {
            console.error('Log Start Error:', logError);
          }
        });
      },
      error: (error) => {
        console.error('Start Error:', error);
      }
    });
  }

  onStop(resource: any): void {
    this.ecsService.stopEcs(resource.externalId).subscribe({
      next: (response) => {
        console.log('Stop Success:', response);
        
        setTimeout(() => {
          this.refreshEcsLists();
        }, 2000);

        this.ecsService.logStop(resource.id).subscribe({
          next: (logResponse) => {
            console.log('Log Stop Success:', logResponse);
          },
          error: (logError) => {
            console.error('Log Stop Error:', logError);
          }
        });
      },
      error: (error) => {
        console.error('Stop Error:', error);
      }
    });
  }

  onPaygStart(resource: any): void {
    this.ecsService.startEcs(resource.externalId).subscribe({
      next: (response) => {
        console.log('Start Success:', response);
        setTimeout(() => {
          this.refreshEcsLists();
        }, 2000);

        this.ecsService.logStart(resource.id).subscribe({
          next: (logResponse) => {
            console.log('Log Start Success:', logResponse);
          },
          error: (logError) => {
            console.error('Log Start Error:', logError);
          }
        });
      },
      error: (error) => {
        console.error('Start Error:', error);
      }
    });
  }

  onPaygStop(resource: any): void {
    this.ecsService.stopEcs(resource.externalId).subscribe({
      next: (response) => {
        console.log('Stop Success:', response);
        setTimeout(() => {
          this.refreshEcsLists();
        }, 2000);

        this.ecsService.logStop(resource.id).subscribe({
          next: (logResponse) => {
            console.log('Log Stop Success:', logResponse);
          },
          error: (logError) => {
            console.error('Log Stop Error:', logError);
          }
        });
      },
      error: (error) => {
        console.error('Stop Error:', error);
      }
    });
  }
}
