import { Component, OnInit } from '@angular/core';
import { SmallBoxDto } from 'src/app/models/smallBoxDto';
import { InputService } from 'src/app/services/input.service';
import { SmallBoxService } from 'src/app/services/small-box.service';
import { FormGroup,FormControl, Validators,FormBuilder } from '@angular/forms'

@Component({
  selector: 'app-small-box',
  templateUrl: './small-box.component.html',
  styleUrls: ['./small-box.component.css']
})
export class SmallBoxComponent implements OnInit{

  constructor(private smallBoxService:SmallBoxService,private inputService:InputService
    ,private formGroup:FormBuilder){}
  
  smallboxes:SmallBoxDto[]= [];
  smallbox!:SmallBoxDto;
  errorData!:string;
    ngOnInit(){
  
  
    }
  smallBoxForm = this.formGroup.group({
    date:['', Validators.required],
    ticketNumber:[0,Validators.required],
    provider:['', Validators.required],
    inputId:[0],
    description:['',Validators.required],
    ticketTotal:[0,Validators.required],
    inputNumber:[0],
    })
    get date(){
      return this.formGroup.control('date');
    }
    get ticketNumber(){
      return this.formGroup.control('ticketNumber');
    }
  
  
    onAddSmallBox(smallBox:SmallBoxDto){
      this.smallBoxService.addSmallBox(smallBox).subscribe({
        next:(smallBoxData)=>{
          this.smallbox = smallBoxData;
        },
        error:(errorData)=>{
          this.errorData = errorData;
        }
      })
    }
}
