import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgFor } from '@angular/common';
import { NgIf } from '@angular/common';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from "../../sidebar/sidebar.component";
import { ViewChild, AfterViewInit,ElementRef } from '@angular/core';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

interface QuotaData {
  university: string;
  Month: string;
  CPU: number;
  Storage: number;
  Network: number;
  RAM: number;
}

@Component({
  selector: 'app-bills-management',
  standalone: true,
  imports: [SidebarComponent,SidebarComponent,FormsModule,NgFor,NgIf,CommonModule],
  templateUrl: './bills-management.component.html',
  styleUrl: './bills-management.component.css'
})
export class BillsManagementComponent implements AfterViewInit{
  @ViewChild('resourceChart') resourceChart!: ElementRef;
  chartInstance!: Chart | null;

  // University & month Filter Options
  universities = ['All', 'UMA', 'US'];
  months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
  // Selected Filters (Default: 'All')
  selectedUniversity = 'UMA';
  selectedMonth = 'January';

  UnitCost=[0.05,0.02,0.01,0.03];
  UsedQuantity:QuotaData[]=[
    { university:"UMA" , Month:"January" , CPU:400,Storage:600,Network:800,RAM:350},
    { university:"UMA" , Month:"February" , CPU:500,Storage:300,Network:800,RAM:350},
    { university:"UMA" , Month:"March" , CPU:400,Storage:400,Network:800,RAM:350},
    { university:"UMA" , Month:"April" , CPU:400,Storage:600,Network:800,RAM:350},
    { university:"UMA" , Month:"May" , CPU:400,Storage:350,Network:800,RAM:350},
    { university:"UMA" , Month:"June" , CPU:400,Storage:700,Network:800,RAM:350},
    { university:"UMA" , Month:"July" , CPU:400,Storage:500,Network:800,RAM:350},
    { university:"UMA" , Month:"August" , CPU:400,Storage:550,Network:800,RAM:350},
    { university:"UMA" , Month:"September" , CPU:400,Storage:600,Network:800,RAM:350},
    { university:"UMA" , Month:"October" , CPU:400,Storage:600,Network:800,RAM:400},
    { university:"UMA" , Month:"November" , CPU:400,Storage:600,Network:850,RAM:350},
    { university:"UMA" , Month:"December" , CPU:400,Storage:650,Network:800,RAM:350},

    { university:"US" , Month:"January" , CPU:400,Storage:600,Network:800,RAM:350},
    { university:"US" , Month:"February" , CPU:450,Storage:650,Network:800,RAM:350},
    { university:"US" , Month:"March" , CPU:400,Storage:600,Network:800,RAM:350},
    { university:"US" , Month:"April" , CPU:400,Storage:600,Network:800,RAM:350},
    { university:"US" , Month:"May" , CPU:400,Storage:600,Network:800,RAM:350},
    { university:"US" , Month:"June" , CPU:80,Storage:700,Network:800,RAM:350},
    { university:"US" , Month:"July" , CPU:400,Storage:600,Network:800,RAM:350},
    { university:"US" , Month:"August" , CPU:400,Storage:600,Network:800,RAM:350},
    { university:"US" , Month:"September" , CPU:400,Storage:350,Network:800,RAM:350},
    { university:"US" , Month:"October" , CPU:400,Storage:600,Network:800,RAM:350},
    { university:"US" , Month:"November" , CPU:400,Storage:600,Network:800,RAM:350},
    { university:"US" , Month:"December" , CPU:400,Storage:60,Network:800,RAM:350},
  ];

  getFilteredData() {
    if (this.selectedUniversity=="All"){
      // Sum all universities for the selected month
      const aggregated = this.UsedQuantity
        .filter(item => item.Month === this.selectedMonth)
        .reduce((acc, item) => {
          return {
            university: 'All',
            resource: item.Month,
            CPU: acc.CPU + item.CPU,
            Storage: acc.Storage + item.Storage,
            Network: acc.Network + item.Network,
            RAM: acc.RAM + item.RAM
          };
        }, { CPU: 0, Storage: 0, Network: 0, RAM: 0 });
    return aggregated;
    }else{
    // Find data for the selected university and month
    return this.UsedQuantity.find(item => 
      item.university === this.selectedUniversity && item.Month === this.selectedMonth
    ) || null; // Return null if no match found
  }}


//chart creation
  ngAfterViewInit() {
    this.renderChart();
  }

  updateChart() {
    if (this.chartInstance) {
      this.chartInstance.destroy(); // Destroy the old chart
      this.chartInstance = null;
    }

    setTimeout(() => {
      this.renderChart(); // Wait for the canvas to be updated:this is imprtant to update infos everytime there are changes
    });
  }

  renderChart() {
    const data = this.getFilteredData();
    if (!data || !this.resourceChart) return;//When either data is missing (null or undefined) or this.resourceChart is not available, 
    // the function will exit immediately without executing any further code.

    const ctx = this.resourceChart.nativeElement.getContext('2d');
    if (!ctx) return;
    const total=(data.CPU * this.UnitCost[0])+(data.Storage * this.UnitCost[1])+(data.Network * this.UnitCost[2])+(data.RAM * this.UnitCost[3]);

    this.chartInstance = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['% CPU', '% Storage', '% Network', '% RAM'],
        datasets: [{
          data: [
            Math.round((data.CPU * this.UnitCost[0]*100)/total),
            Math.round((data.Storage * this.UnitCost[1]*100)/total),
            Math.round((data.Network * this.UnitCost[2]*100)/total),
            Math.round((data.RAM * this.UnitCost[3]*100)/total)
          ],
          backgroundColor: ['#ff6384', '#36a2eb', '#ffce56', '#4bc0c0']
        }]
      }
    });
  }

  //Progress bars:
  resources: { 
    name: string; 
    percentage: number; 
    change: number; 
    color: string; 
  }[] = [];
  currentData: QuotaData = { university: '', Month: '', CPU: 0, Storage: 0, Network: 0, RAM: 0 };
  previousData: QuotaData = { university: '', Month: '', CPU: 0, Storage: 0, Network: 0, RAM: 0 };


  ngOnInit() {
    this.updateData();
  }

  updateData() {
    // Get current and previous month's data; This ensures currentData and previousData are always valid objects, preventing runtime errors.
    this.currentData = this.getFilteredDataForProgressBar(this.selectedMonth);
    this.previousData = this.getFilteredDataForProgressBar(this.getPreviousMonth(this.selectedMonth));

    // Calculate percentage change
    this.resources = this.calculateResources(); //resources is populated inside the updateData() function;calculateResources() 
    // returns an array, and resources stores this array.
  }

  getFilteredDataForProgressBar(month: string): QuotaData {
    return (
      this.UsedQuantity.find(
        (item) => item.university === this.selectedUniversity && item.Month === month
      ) || { university: this.selectedUniversity, Month: month, CPU: 0, Storage: 0, Network: 0, RAM: 0 }
    );
  }

  getPreviousMonth(currentMonth: string): string {
    const index = this.months.indexOf(currentMonth);
    return index > 0 ? this.months[index - 1] : this.months[this.months.length - 1]; // If you choose UMA in January, 
    // the previous month is December of the previous year.
  }

  calculateResources() {
    return [
      { name: 'CPU', percentage: this.calculatePercentage(this.currentData.CPU), change: this.calculateChange(this.currentData.CPU, this.previousData.CPU), color: '#ff6384' },
      { name: 'Storage', percentage: this.calculatePercentage(this.currentData.Storage), change: this.calculateChange(this.currentData.Storage, this.previousData.Storage), color: '#36a2eb' },
      { name: 'Network', percentage: this.calculatePercentage(this.currentData.Network), change: this.calculateChange(this.currentData.Network, this.previousData.Network), color: '#ffce56' },
      { name: 'RAM', percentage: this.calculatePercentage(this.currentData.RAM), change: this.calculateChange(this.currentData.RAM, this.previousData.RAM), color: '#4bc0c0' }
    ];
  }

  calculatePercentage(value: number) {
    const total = this.currentData.CPU + this.currentData.Storage + this.currentData.Network + this.currentData.RAM;
    return total ? Math.round((value / total) * 100) : 0;
  }

  calculateChange(current: number, previous: number) {
    if (previous === 0) return current > 0 ? 100 : 0; //if previous ==0 condition 
    return Math.round(((current - previous) / previous) * 100);
  }

}

