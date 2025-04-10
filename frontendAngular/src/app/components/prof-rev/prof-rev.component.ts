import { CommonModule } from '@angular/common';
import { Component, NgModule } from '@angular/core';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { ChatbotService } from '../../services/chatbot.service';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-prof-rev',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './prof-rev.component.html',
  styleUrl: './prof-rev.component.css'
})
export class ProfRevComponent {

  constructor(private chatbotService:ChatbotService){}

  chatbot : String =""
  response : any
  loading: boolean = false;
  error: boolean = false;
  messages: any[] = [];

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

}
