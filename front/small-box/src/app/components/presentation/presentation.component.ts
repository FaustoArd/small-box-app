import { Component, OnInit } from '@angular/core';
import { ContainerDto } from 'src/app/models/containerDto';
import { SmallBoxUnifierDto } from 'src/app/models/smallBoxUnifierDto';
import { ContainerService } from 'src/app/services/container.service';
import { SmallBoxService } from 'src/app/services/small-box.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-presentation',
  templateUrl: './presentation.component.html',
  styleUrls: ['./presentation.component.css']
})


export class PresentationComponent implements OnInit {

  completedSmallBox: SmallBoxUnifierDto[] = [];
  errorData!: string;
  container!: ContainerDto;
  smallBoxCreated!:boolean;

constructor(private storageService:StorageService,private containerService:ContainerService,
  private smallBoxService:SmallBoxService){}
  


ngOnInit(): void {
    this.getContainerById();
   
}

  getContainerById(): void {
    this.containerService.getContainerById(Number(this.storageService.getCurrentContainerId())).subscribe({
      next: (containerData) => {
        this.container = new ContainerDto();
        this.container = containerData;
        this.smallBoxCreated = containerData.smallBoxCreated;
        this.getSmallBoxCompleteByContainerId(this.container.id);

       
      }, error: (errorData) => {
        this.errorData = errorData;
      }
    });
  }
  getSmallBoxCompleteByContainerId(containerId:number):void{
    this.smallBoxService.getCompletedSmallBoxByContainerId(containerId).subscribe({
      next:(smData)=>{
        this.completedSmallBox = smData;
      },
      error:(errorData)=>{
        console.log(errorData);
      }
    })
  }

}
