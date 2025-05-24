import { CommonModule } from '@angular/common';
import { Component, NgModule } from '@angular/core';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { ChatbotService } from '../../services/chatbot.service';
import { HttpHeaders } from '@angular/common/http';
import { AuthSerivce } from '../../services/auth.service';
import { UniversitySidebarComponent } from '../university-sidebar/university-sidebar.component';
import { FullCalendarModule } from '@fullcalendar/angular'; // 👈 import
import { CalendarOptions } from '@fullcalendar/core';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction'; // Optional (for click/drag)
import dayGridPlugin from '@fullcalendar/daygrid'; // If you want month view too


import { ProfSidebarComponent } from '../../prof-sidebar/prof-sidebar.component';
@Component({
  selector: 'app-prof-rev',
  standalone: true,
  imports: [ProfSidebarComponent,CommonModule,FormsModule,FullCalendarModule],
  templateUrl: './prof-rev.component.html',
  styleUrl: './prof-rev.component.css'
})
export class ProfRevComponent {
  

  constructor(private chatbotService:ChatbotService, private authService:AuthSerivce){}

  chatbot : String =""
  response : any
  loading: boolean = false;
  error: boolean = false;
  messages: any[] = [];
  flavors : any;
  images : any;

  selectedFlavorId: string = '';
  selectedImageId: string = '';
  selectedImageName:string='';
  storage: number | null = null;

  selectedFlavor: any;

  uniName : any;
  uniId: any;

  startTime : any;
  endTime : any;

  unavailableSlots: any[] = [];
  errorMessage: string = '';

 calendarOptions: CalendarOptions = {
  plugins: [timeGridPlugin, dayGridPlugin, interactionPlugin],
  initialView: 'timeGridWeek',
  headerToolbar: {
    left: 'prev,next today',
    center: 'title',
    right: 'dayGridMonth,timeGridWeek,timeGridDay'
  },
  aspectRatio: 1.6,
  expandRows: true,
  slotMinTime: '08:00:00',
  slotMaxTime: '20:00:00',
  allDaySlot: false,
  nowIndicator: true,
  slotDuration: '00:30:00',
  slotLabelInterval: '01:00',
  slotLabelFormat: {
    hour: 'numeric',
    minute: '2-digit',
    meridiem: 'short'
  },
  events: [],
  eventClick: this.handleEventClick.bind(this),
  height: 'auto',
  dayHeaderFormat: { weekday: 'short', month: 'numeric', day: 'numeric' },
  navLinks: true,
  editable: false,
  selectable: false,
  eventDisplay: 'block',
  eventColor: '#e3342f', // consistent red color for unavailable slots
};


showQuotaOverrideOption: boolean = false;
quotaExceeded: boolean = false;

handleEventClick(arg: any) {
  alert(`⛔ This slot is unavailable.\n\nFrom: ${arg.event.start.toLocaleString()}\nTo: ${arg.event.end.toLocaleString()}`);
}

updateCalendarWithUnavailableSlots(slots: any[]) {
  const events = slots.map(slot => ({
    title: 'Unavailable',
    start: slot.start,
    end: slot.end,
    color: '#dc2626',
    textColor: '#fff',
    display: 'block', // show event as block with color & text
    borderColor: '#b91c1c',
    classNames: ['unavailable-event']
  }));

  this.calendarOptions = {
    ...this.calendarOptions,
    events
  };
}

  ngOnInit(): void {
    
    this.checkForToken();
    this.loadFlavors();
    this.loadImages();
    this.getUni();

  }

  checkForToken() {
    const token = this.authService.getStoredAdminToken();
    if (!token) {
      // If no token is found or token is expired, fetch a new token
      this.authService.getAdminToken().subscribe(() => {
        console.log('Token fetched and saved in localStorage');
      });
    } else {
      console.log('Token found and valid:', token);
    }
  }

  getUni(){
    const token = localStorage.getItem("token2");
    this.chatbotService.getUniverstiy(token).subscribe((response)=>{
      this.uniName = response.universityName;

    })
    
    this.chatbotService.getUniverstiyId(token).subscribe((response)=>{
      console.log(response)
      this.uniId = response.universityId;
    })

  }

  getselectedImageName(): string {
  const selected = this.images.find((img: { id: string; }) => img.id === this.selectedImageId);
  return selected ? selected.name : '';
}



  loadFlavors(){
    this.chatbotService.getFlavors().subscribe((response) =>{
      this.flavors = response.flavors;
      console.log(this.flavors)

    })
  }

  loadImages(){
    this.chatbotService.getImages().subscribe((response) =>{
      this.images = response.images;
      console.log(this.images)

    })
  }

  onFlavorSelect(event: any) {
    const selectedFlavorId = event.target.value;
    this.selectedFlavor = this.flavors.find((flavor: { id: any; }) => flavor.id === selectedFlavorId);
    console.log(this.selectedFlavor)
  }



  

  onSubmit(){
    console.log("clicked !")
    console.log(this.chatbot)

    this.messages.push({ message: this.chatbot, fromUser: true });

    const sendingMessage = { message: this.chatbot };  // ✅ Prepare the sending object

    console.log(this.messages)
    this.chatbot = ''; // Reset the input field

    this.loading = true;
    this.error = false;
    
    this.chatbotService.send_user_input(sendingMessage).subscribe((res)=>{
      this.response = res;
      console.log(res)
if (res.status === 'success' && res.resources) {
  this.messages.push({ 
    text: `vCPU: ${res.resources.vCPU}, Storage: ${res.resources.Storage}, RAM: ${res.resources.RAM}`, 
    fromUser: false 
  });
} else if (res.status === 'invalid_request') {
  this.messages.push({ 
    text: res.message || "The request is not related to cloud resources.", 
    fromUser: false 
  });
} else if (res.status === 'validation_error') {
  this.messages.push({ 
    text: `⚠️ AI returned unreasonable values:\n${res.details?.join(', ')}`, 
    fromUser: false 
  });
} else {
  this.messages.push({ 
    text: "❌ Sorry, something went wrong with the AI response.", 
    fromUser: false 
  });
}      this.loading = false;
      
    },
  (err)=>{

    this.error = true;
    this.loading = false;

  });

  }


onSubmitRequest() {

  

  this.errorMessage = '';
  this.quotaExceeded = false;
  this.showQuotaOverrideOption = false;

  const storageValue = this.storage ? parseInt(this.storage.toString()) : 0;

  const formatDateTime = (dt: string) => dt.length === 16 ? dt + ':00' : dt;
  const start = formatDateTime(this.startTime);
  const end = formatDateTime(this.endTime);

  const vcpu = this.selectedFlavor?.vcpus ?? 0;
  const ram = this.selectedFlavor?.ram ?? 0;

  const now = new Date();

  // Calculate the date 2 months from now (end)
  const twoMonthsLater = new Date();
  twoMonthsLater.setMonth(now.getMonth() + 2);

  // Format dates as ISO strings without milliseconds (like '2025-06-02T11:49:00')
  function toISOWithoutMillis(date: Date) {
    return date.toISOString().slice(0, 19);
  }

  const fromDate = toISOWithoutMillis(now);
  const toDate = toISOWithoutMillis(twoMonthsLater);

  console.log('From:', fromDate);
  console.log('To:', toDate);

  // Use the new availability check endpoint
  this.chatbotService.checkAvailability(this.uniId!, vcpu, ram, storageValue, fromDate, toDate).subscribe(
    (result: any) => {
      if (result.quotaExceeded) {
        // Handle quota exceeded case
        this.quotaExceeded = true;
        this.showQuotaOverrideOption = true;
        this.errorMessage = 'The requested resources exceed your university quota limits. Please reduce your resource requirements or contact your administrator.';
        
        // Clear calendar events since this is not a time conflict
        this.calendarOptions = {
          ...this.calendarOptions,
          events: []
        };
        
      } else if (result.timeConflict) {
        // Handle time overlap case - show calendar with unavailable slots
        this.unavailableSlots = result.unavailableSlots;
        this.updateCalendarWithUnavailableSlots(result.unavailableSlots);

        const selectedStart = new Date(start);
        const selectedEnd = new Date(end);

        const overlaps = result.unavailableSlots.some((slot: any) => {
          const slotStart = new Date(slot.start);
          const slotEnd = new Date(slot.end);
          return slotEnd > selectedStart && slotStart < selectedEnd;
        });

        if (overlaps) {
          this.errorMessage = 'The selected time overlaps with an unavailable slot. Please pick another time from the calendar below.';
          return;
        }

        // If no direct overlap, proceed with reservation
        this.proceedWithReservation(start, end, vcpu, ram, storageValue);
        
      } else {
        // Resources are available - proceed with reservation
        this.proceedWithReservation(start, end, vcpu, ram, storageValue);
      }
    },
    (err: any) => {
      console.error('Failed to check availability:', err);
      this.errorMessage = 'Could not verify resource availability. Please try again.';
    }
  );

   

  }


  onSubmitRequestPayg() {

  

  this.errorMessage = '';
  this.quotaExceeded = false;
  this.showQuotaOverrideOption = false;

  const storageValue = this.storage ? parseInt(this.storage.toString()) : 0;

  const formatDateTime = (dt: string) => dt.length === 16 ? dt + ':00' : dt;
  const start = formatDateTime(this.startTime);
  const end = formatDateTime(this.endTime);

  const vcpu = this.selectedFlavor?.vcpus ?? 0;
  const ram = this.selectedFlavor?.ram ?? 0;

  
        
        // Resources are available - proceed with reservation
  this.proceedWithReservationPYAG(start, end, vcpu, ram, storageValue);
    

  }


  



    proceedWithReservationPYAG(start: string, end: string, vcpu: number, ram: number, storageValue: number,) {
      const springPayload = {
      university_id: this.uniId || '',
      status: "Pending",
      start_time: start,
      end_time: end,
      type: 'ECS',
      vcpu: vcpu.toString(),
      ram: ram.toString(),
      storage: storageValue.toString(),
      image: this.getselectedImageName(),
      imageId:this.selectedImageId,
      flavorId:this.selectedFlavorId
    };

    
    console.log(springPayload)

    this.chatbotService.saveReservationToSpringPayg(springPayload).subscribe(
      (res: any) => {
        console.log('Reservation saved:', res);
        this.errorMessage = 'Reservation successfully created!';
        // Reset form or redirect
      }, 
      (err: any) => {
        console.error('Error saving reservation:', err);
        this.errorMessage = 'Failed to create reservation. Please try again.';
      }
    );
}



  proceedWithReservation(start: string, end: string, vcpu: number, ram: number, storageValue: number) {
  const springPayload = {
    university_id: this.uniId || '',
    status: "Pending",
    start_time: start,
    end_time: end,
    type: 'ECS',
    vcpu: vcpu.toString(),
    ram: ram.toString(),
    storage: storageValue.toString(),
    image: this.getselectedImageName()
  };

  this.chatbotService.saveReservationToSpring(springPayload).subscribe(
    (res: any) => {
      console.log('Reservation saved:', res);
      const reservationId = res.id;
      this.errorMessage = 'Reservation successfully created!';

      const ecsPayload = {
        storage: storageValue.toString(),
        imageId: this.selectedImageId,
        flavorId: this.selectedFlavorId
      };

      this.chatbotService.createVM(ecsPayload).subscribe(
        (vmRes: any) => {
          console.log('VM created:', vmRes);

          const updatePayload = {
            reservationId: reservationId,
            vm_id: vmRes.vm_id,
          };

          this.chatbotService.updateReservationWithVmId(updatePayload).subscribe(
            () => {
              console.log('Reservation updated with VM ID.');
            },
            (err: any) => {
              console.error('Error updating reservation:', err);
            }
          );
        },
        (err: any) => {
          console.error('Error creating VM via Flask:', err);
        }
      );
    },
    (err: any) => {
      console.error('Error saving reservation:', err);
      this.errorMessage = 'Failed to create reservation. Please try again.';
    }
  );
}

}
