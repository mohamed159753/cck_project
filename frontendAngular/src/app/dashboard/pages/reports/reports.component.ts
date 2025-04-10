import { Component, SimpleChanges } from '@angular/core';
import { SidebarComponent } from "../../sidebar/sidebar.component";
import { MainComponent } from "../../main/main.component";
import { FormsModule } from '@angular/forms';
import { NgFor } from '@angular/common';
import { NgIf } from '@angular/common';
import { CommonModule } from '@angular/common';
import { AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { Chart, ChartConfiguration, Colors, registerables } from 'chart.js';
import { MatProgressBarModule } from '@angular/material/progress-bar';
interface UsageData {
  university: string;
  resource: string;
  jan: number;
  feb: number;
  mar: number;
  apr: number;
  may: number;
  jun: number;
  jul: number;
  aug: number;
  sep: number;
  oct: number;
  nov: number;
  dec: number;
}
Chart.register(...registerables);  //Registers all necessary components needed to render the chart, Ensures that Chart.js can properly 
// handle different types of charts (bar, line, pie, etc.),
//Prevents errors related to missing chart elements.
interface QuotaData {
  university: string;
  resource: string;
  reservations: number;
  pending: number;
  approved: number;
  totalQuota: number;
  currentUse: number;
  toBeUsed: number;
}
interface ConsData {
  uni: string;
  inst: string;
  res:string;
  cons: number;
}
@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule,SidebarComponent,FormsModule,NgFor,NgIf,MatProgressBarModule ],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.css'
})
export class ReportsComponent  implements AfterViewInit{//implementation ensures the <canvas> is available.
  @ViewChild('barChart') barChartRef!: ElementRef<HTMLCanvasElement>; //Gets a reference to a DOM element in the template after view initialization,grabs the canvas only after Angular renders the view.
  chart!: Chart;

  // University & Resource Filter Options
  universities = ['All', 'UMA', 'US'];
  resources = ['CPU', 'RAM', 'Storage', 'Network'];

  // Selected Filters (Default: 'All')
  selectedUniversity = 'All';
  selectedResource = 'CPU';

  // Sample Data
  data: QuotaData[] =  [ 
    { university: 'UMA', resource: 'CPU', reservations: 50, pending: 10, approved: 40, totalQuota: 100, currentUse: 75, toBeUsed: 25 },
    { university: 'US', resource: 'CPU', reservations: 55, pending: 12, approved: 43, totalQuota: 110, currentUse: 90, toBeUsed: 30 },

    { university: 'UMA', resource: 'RAM', reservations: 25, pending: 6, approved: 19, totalQuota: 70, currentUse: 30, toBeUsed: 20 },
    { university: 'US', resource: 'RAM', reservations: 30, pending: 5, approved: 25, totalQuota: 80, currentUse: 60, toBeUsed: 20 },

    { university: 'UMA', resource: 'Storage', reservations: 20, pending: 8, approved: 12, totalQuota: 90, currentUse: 50, toBeUsed: 40 },
    { university: 'US', resource: 'Storage', reservations: 18, pending: 5, approved: 13, totalQuota: 85, currentUse: 55, toBeUsed: 30 },

    { university: 'US', resource: 'Network', reservations: 60, pending: 15, approved: 45, totalQuota: 120, currentUse: 85, toBeUsed: 35 },
    { university: 'UMA', resource: 'Network', reservations: 40, pending: 10, approved: 30, totalQuota: 100, currentUse: 70, toBeUsed: 30 }
  ];

  getFilteredData() {
    if (this.selectedUniversity === 'All') {
      // Sum all universities for the selected resource
      const aggregated = this.data
        .filter(item => item.resource === this.selectedResource)
        .reduce((acc, item) => {
          return {
            university: 'All',
            resource: item.resource,
            reservations: acc.reservations + item.reservations,
            pending: acc.pending + item.pending,
            approved: acc.approved + item.approved,
            totalQuota: acc.totalQuota + item.totalQuota,
            currentUse: acc.currentUse + item.currentUse,
            toBeUsed: acc.toBeUsed + item.toBeUsed
          };
        }, { reservations: 0, pending: 0, approved: 0, totalQuota: 0, currentUse: 0, toBeUsed: 0 });
        // Convert to percentage
          aggregated.currentUse = (aggregated.currentUse / aggregated.totalQuota) * 100;
          aggregated.toBeUsed = (aggregated.toBeUsed / aggregated.totalQuota) * 100;

    return aggregated;
    } else {
      // Filter specific university and resource
      return this.data.find(item => item.university === this.selectedUniversity && item.resource === this.selectedResource)|| null;
    }
  }
//Colors:


//Usage statistics:

data_usage: UsageData[]  = [ 
  { university: 'UMA', resource: 'CPU', jan: 20, feb: 25, mar: 25, apr: 45, may: 50, jun: 40, jul: 45, aug: 20, sep: 50,oct: 65,nov: 70,dec: 80 },
  { university: 'UMA', resource: 'RAM', jan: 25, feb: 60, mar: 100, apr: 40, may: 50, jun: 40, jul: 45, aug: 20, sep: 50,oct: 65,nov: 70,dec: 80 },

  { university: 'UMA', resource: 'Storage', jan: 20, feb: 25, mar: 40, apr: 45, may: 50, jun: 40, jul: 45, aug: 20, sep: 50,oct: 65,nov: 70,dec: 80 },
  { university: 'UMA', resource: 'Network', jan: 20, feb: 25, mar: 25, apr: 45, may: 50, jun: 60, jul: 45, aug: 20, sep: 31,oct: 65,nov: 70,dec: 80 },

  { university: 'US', resource: 'CPU', jan: 20, feb: 25, mar: 25, apr: 45, may: 50, jun: 40, jul: 45, aug: 20, sep: 50,oct: 65,nov: 70,dec: 80 },
  { university: 'US', resource: 'RAM', jan: 20, feb: 25, mar: 25, apr: 45, may: 50, jun: 40, jul: 40, aug: 20, sep: 50,oct: 65,nov: 70,dec: 80 },

  { university: 'US', resource: 'Storage', jan: 20, feb: 25, mar: 12, apr: 45, may: 50, jun: 40, jul: 45, aug: 20, sep: 50,oct: 65,nov: 70,dec: 80 },
  { university: 'US', resource: 'Network', jan: 20, feb: 25, mar: 25, apr: 45, may: 50, jun: 80, jul: 45, aug: 20, sep: 70,oct: 65,nov: 70,dec: 80 }

];

ngAfterViewInit() {
  this.createChart(); //Calls createChart() to generate the chart once when the page loads.
}


getFilteredUsageData() {
  if (this.selectedUniversity === 'All') {
    const result = this.data_usage
      .filter(item => item.resource === this.selectedResource)
      .reduce((acc, item) => {
        (Object.keys(item) as (keyof UsageData)[]).forEach((key) => {
          if (key !== 'university' && key !== 'resource') {
            acc[key] = (acc[key] ?? 0) + (item[key] as number ?? 0);
          }
        });
        return acc;
      }, {
        university: 'All',
        resource: this.selectedResource,
        jan: 0, feb: 0, mar: 0, apr: 0, may: 0, jun: 0,
        jul: 0, aug: 0, sep: 0, oct: 0, nov: 0, dec: 0
      } as UsageData);

    // **Normalize values to ensure they stay within 100%**
    (Object.keys(result) as (keyof UsageData)[]).forEach((key) => {
      if (key !== 'university' && key !== 'resource') {
        result[key] = Math.min(result[key]!, 100); // checks if the value is greater than 100; if it is, it sets it to 100; otherwise, it keeps the original value.
      }
    });

    return result;
  } else {
    const data = this.data_usage.find(item =>
      item.university === this.selectedUniversity && item.resource === this.selectedResource
    );

    if (data) {
      // Ensure each value is a percentage
      (Object.keys(data) as (keyof UsageData)[]).forEach((key) => {
        if (key !== 'university' && key !== 'resource') {
          data[key] = Math.min(data[key]!, 100);//checks if the value is greater than 100; if it is, it sets it to 100; otherwise, it keeps the original value.
        }
      });
    }

    return data || null;
  }
}



createChart() {
  const ctx = this.barChartRef.nativeElement.getContext('2d');//Finds the canvas (ctx) where the chart will be drawn. 
  const usageData = this.getFilteredUsageData();
//if ALL,usageData contains an aggregated object where the monthly values (jan, feb, mar, etc.) are summed across all universities for the selected resource (selectedResource).
// else ;usageData contains specific data for the selected university and resource. Each month will hold the usage percentage for that particular university and resource.
  if (ctx && usageData) {
    this.chart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: Object.keys(usageData).filter(key => key !== 'university' && key !== 'resource'),
        datasets: [{
          label: `${this.selectedResource} Usage (%)`,
          data: Object.values(usageData).filter((_, index) => index > 1),
          backgroundColor: 'rgba(54, 162, 235, 0.6)',
          borderColor: 'rgba(54, 162, 235, 1)',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            display: false
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            max: 100 // Ensures percentage stays within 100%
          }
        }
      }
    });
  }
}

updateChart() {
  if (!this.selectedUniversity || !this.selectedResource) return;

  const resourceKey = this.selectedResource as keyof UsageData;
  let data: number[] = [];

  if (this.selectedUniversity === 'All') {
    // Aggregate data for all universities for the selected resource
    const aggregatedData: { [key: string]: number } = {
      jan: 0, feb: 0, mar: 0, apr: 0, may: 0, jun: 0, jul: 0, aug: 0, sep: 0, oct: 0, nov: 0, dec: 0
    };

    this.data_usage
      .filter(item => item.resource === this.selectedResource)
      .forEach(item => {
        Object.keys(aggregatedData).forEach(month => {
          aggregatedData[month] += (item[month as keyof UsageData] as number) || 0;
        });
      });

    data = Object.values(aggregatedData);
  } else {
    // Get data for the selected university and resource
    const universityData = this.data_usage.find(
      item => item.university === this.selectedUniversity && item.resource === this.selectedResource
    );

    data = universityData
      ? Object.values(universityData).slice(2) as number[] // Skip university & resource fields
      : [];//It extracts the numerical monthly data from universityData, ignoring the first two fields (university and resource).
  }

  // Update the chart
  this.chart.data.labels = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  this.chart.data.datasets[0].label = this.selectedResource; // Dynamic title
  this.chart.data.datasets[0].data = data; //datasets[0] represents "CPU Usage" is the one being updated.
  this.chart.update();
}


//Alerts section

    sug:string | null=null;
    info:string | null=null;
    progress: number = 0;  // Progress bar value (0-100)
    alertMessage: string | null = null;  // Alert message
    //progressBarColor: 'primary' | 'accent' | 'warn' = 'primary';  // Allowed Angular Material colors
    alertColor: string = 'black'; // Default color

  updateProgressBar() {
    const selectedData = this.getFilteredData();

    if (selectedData) {
      // Set progress bar value based on current use
      this.progress = selectedData.currentUse;

      // Set progress bar color based on the current usage
      if (selectedData.currentUse > 80) {
        //this.progressBarColor = 'warn';   // Red color for high usage
        this.alertMessage = `High ${this.selectedResource} Usage Alert!`;  // Alert message
        this.alertColor="red";
        this.info=`${Math.round(this.progress)}% of ${this.selectedResource} quota has been used.`;
        this.sug=`Only ${Math.round(selectedData.toBeUsed)}% remaining before reaching the limit.`;
      } else if (selectedData.currentUse < 50) {
        //this.progressBarColor = 'primary'; // Blue (default) for low usage
        this.alertMessage = `Low ${this.selectedResource} Usage Alert!`;  // Alert message
        this.info=`${Math.round(this.progress)}% of ${this.selectedResource} quota has been used.`;
        this.sug=`Only ${Math.round(selectedData.toBeUsed)}% remaining before reaching the limit.`;
        this.alertColor="green";
      } else if (selectedData.currentUse >= 50 && selectedData.currentUse <= 80) {
        //this.progressBarColor = 'accent';  // Yellow/Orange for medium usage
        this.alertMessage = `Average ${this.selectedResource} Usage Alert!`;  // Alert message
        this.info=`${Math.round(this.progress)}% of ${this.selectedResource} quota has been used.`;
        this.sug=`Only ${Math.round(selectedData.toBeUsed)}% remaining before reaching the limit.`;
        this.alertColor="orange";
      }
    } else {
      this.alertMessage = `No data available for ${this.selectedResource} at ${this.selectedUniversity}.`;
      //this.progressBarColor = 'primary';  // Default color when no data
      this.progress = 0;
      this.alertColor = 'gray';
    }
  }
  ngOnInit() {
    this.updateProgressBar(); // Update progress bar when the component initializes
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['selectedUniversity'] || changes['selectedResource']) {
      this.updateProgressBar(); // Update progress bar if inputs change
    }
  }

  // Add a method to trigger progress bar update
  changeUniversityAndResource(university: string, resource: string) {
    this.selectedUniversity = university;
    this.selectedResource = resource;
    this.updateProgressBar(); // Trigger update after changing selection
  }

 //Top 3 universities/instit section:
  consumption:ConsData[]=[
    {uni:"UMA", inst:"ISAMM", res:"CPU",cons:20},
    {uni:"UMA", inst:"ISAMM", res:"RAM",cons:50},
    {uni:"UMA", inst:"ISAMM", res:"Storage",cons:80},
    {uni:"UMA", inst:"ISAMM", res:"Network",cons:30},

    {uni:"UMA", inst:"ENSI",res:"CPU", cons:90},
    {uni:"UMA", inst:"ENSI", res:"RAM",cons:90},
    {uni:"UMA", inst:"ENSI",res:"Storage", cons:90},
    {uni:"UMA", inst:"ENSI",res:"Network", cons:90},

    {uni:"UMA", inst:"ESEN",res:"CPU", cons:75},
    {uni:"UMA", inst:"ESEN",res:"RAM", cons:75},
    {uni:"UMA", inst:"ESEN",res:"Storage", cons:75},
    {uni:"UMA", inst:"ESEN",res:"Network", cons:75},

    {uni:"UMA", inst:"FLAHM",res:"CPU", cons:25},
    {uni:"UMA", inst:"FLAHM",res:"RAM", cons:25},
    {uni:"UMA", inst:"FLAHM",res:"Storage", cons:25},
    {uni:"UMA", inst:"FLAHM",res:"Network", cons:25},

    {uni:"UT", inst:"ISG",res:"CPU", cons:75},
    {uni:"UT", inst:"ISG",res:"RAM", cons:75},
    {uni:"UT", inst:"ISG",res:"Storage", cons:75},
    {uni:"UT", inst:"ISG",res:"Network", cons:75},

    {uni:"UT", inst:"FMT",res:"CPU", cons:50},
    {uni:"UT", inst:"FMT",res:"RAM", cons:50},
    {uni:"UT", inst:"FMT",res:"Storage", cons:50},
    {uni:"UT", inst:"FMT",res:"Network", cons:50},

    {uni:"UT", inst:"ENSIT",res:"CPU", cons:80},
    {uni:"UT", inst:"ENSIT",res:"RAM", cons:80},
    {uni:"UT", inst:"ENSIT",res:"Storage", cons:80},
    {uni:"UT", inst:"ENSIT",res:"Network", cons:80},

    {uni:"US", inst:"ISSAT",res:"CPU", cons:75},
    {uni:"US", inst:"ISSAT",res:"RAM", cons:75},
    {uni:"US", inst:"ISSAT",res:"Storage", cons:75},
    {uni:"US", inst:"ISSAT",res:"Network", cons:75},

    {uni:"US", inst:"ISIMM",res:"CPU", cons:50},
    {uni:"US", inst:"ISIMM",res:"RAM", cons:50},
    {uni:"US", inst:"ISIMM",res:"Storage", cons:50},
    {uni:"US", inst:"ISIMM",res:"Network", cons:50},

    {uni:"US", inst:"FMS",res:"CPU", cons:80},
    {uni:"US", inst:"FMS",res:"RAM", cons:80},
    {uni:"US", inst:"FMS",res:"Storage", cons:80},
    {uni:"US", inst:"FMS",res:"Network", cons:80},
  ];
/*
  get topConsumers() {
    const filtered = this.consumption.filter(d => d.res === this.selectedResource);

    if (this.selectedUniversity === 'All') {
      // Top 3 universities based on sum of consumption
      const grouped = new Map<string, number>();
      filtered.forEach(d => {
        grouped.set(d.uni, (grouped.get(d.uni) || 0) + d.cons);
      });

      return Array.from(grouped.entries())
        .sort((a, b) => b[1] - a[1])
        .slice(0, 3)
        .map(([name, value]) => ({ name, value }));
    } else {
      // Top 3 institutions in that university
      const filteredInst = filtered.filter(d => d.uni === this.selectedUniversity);
      return filteredInst
        .sort((a, b) => b.cons - a.cons)
        .slice(0, 3)
        .map(d => ({ name: d.inst, value: d.cons }));
    }
  }*/
}
