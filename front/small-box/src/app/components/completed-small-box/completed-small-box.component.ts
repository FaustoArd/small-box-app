import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ContainerDto } from 'src/app/models/containerDto';
import { SmallBoxUnifierDto } from 'src/app/models/smallBoxUnifierDto';
import { ContainerService } from 'src/app/services/container.service';
import { SmallBoxService } from 'src/app/services/small-box.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-completed-small-box',
  templateUrl: './completed-small-box.component.html',
  styleUrls: ['./completed-small-box.component.css']
})
export class CompletedSmallBoxComponent implements OnInit {

  completedSmallBox:SmallBoxUnifierDto[]=[];
  errorData!:string;
  container!:ContainerDto;
  
  
    constructor(private smallBoxService:SmallBoxService,private containerService:ContainerService,private storageService:StorageService){}
  
  
    ngOnInit(): void {
        this.onCompleteSmallBox();
    }
  
  
    onCompleteSmallBox():void{
      this.smallBoxService.completeSmallBox(Number(this.storageService.getCurrentContainerId())).subscribe({
        next:(compData)=>{
          this.completedSmallBox = compData;
        },
        error:(errorData)=>{
          this.errorData = errorData;
        },complete:()=>{
          this.getContainerById();
        }
      });
    }
    getContainerById():void{
      this.containerService.getContainerById(Number(this.storageService.getCurrentContainerId())).subscribe({
        next:(containerData)=>{
          this.container = containerData;
        },error:(errorData)=>{
          this.errorData = errorData;
        }
      });
    }
  
}

