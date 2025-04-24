import { CommonModule } from '@angular/common';
import { Component, NgModule } from '@angular/core';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { ChatbotService } from '../../services/chatbot.service';
import { HttpHeaders } from '@angular/common/http';
import { AuthSerivce } from '../../services/auth.service';

@Component({
  selector: 'app-prof-rev',
  standalone: true,
  imports: [CommonModule,FormsModule],
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
  storage: number | null = null;

  selectedFlavor: any;

  ngOnInit(): void {
    this.checkForToken();
    this.loadFlavors();
    this.loadImages();

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

    this.messages.push({ text: this.chatbot, fromUser: true });
    this.chatbot = ''; // Reset the input field

    this.loading = true;
    this.error = false;
    
    this.chatbotService.send_user_input(this.chatbot).subscribe((res)=>{
      this.response = res;
      console.log(res)
      this.messages.push({ text: `vCPU: ${res.response.vCPU}, Storage: ${res.response.Storage}, RAM: ${res.response.RAM}`, fromUser: false });
      this.loading = false;
      
    },
  (err)=>{

    this.error = true;
    this.loading = false;

  });

  }


onSubmitRequest() {

  const storageValue = this.storage ? parseInt(this.storage.toString()) : 0;
  const payload = {
    flavorId: this.selectedFlavorId,
    imageId: this.selectedImageId,
    storage: storageValue
    };

    console.log(payload)

    this.chatbotService.createVM(payload).subscribe((response)=>{
      console.log(response);
    })

  }

}
