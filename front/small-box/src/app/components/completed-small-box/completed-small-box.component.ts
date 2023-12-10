import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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

  completedSmallBox: SmallBoxUnifierDto[] = [];
  errorData!: string;
  container!: ContainerDto;
  smallBoxCreated!:boolean;


  constructor(private smallBoxService: SmallBoxService, private containerService: ContainerService,
    private storageService: StorageService, private route: ActivatedRoute,private router:Router) { }


  ngOnInit(): void {
    this.containerService.getContainerById(Number(this.storageService.getCurrentContainerId())).subscribe({
      next: (containerData) => {
        this.container = containerData;
        this.smallBoxCreated = containerData.smallBoxCreated;
        if(this.smallBoxCreated){
         this.deleteAllbyContainerId(Number(this.storageService.getCurrentContainerId()));
         this.onCompleteSmallBox();
        }else{
          this.deleteAllbyContainerId(Number(this.storageService.getCurrentContainerId()));
        this.onCompleteSmallBox();
        }
      }, error: (errorData) => {
        this.errorData = errorData;
      }
    });
  
  
   
     
   
  }


  onCompleteSmallBox(): void {
    this.smallBoxService.completeSmallBox(Number(this.storageService.getCurrentContainerId())).subscribe({
      next: (compData) => {
        this.completedSmallBox = compData;
      },
      error: (errorData) => {
        this.errorData = errorData;
      }, complete: () => {
        this.getContainerById();
      }
    });
  }
  getContainerById(): void {
    this.containerService.getContainerById(Number(this.storageService.getCurrentContainerId())).subscribe({
      next: (containerData) => {
        this.container = containerData;
        this.smallBoxCreated = containerData.smallBoxCreated;
       
      }, error: (errorData) => {
        this.errorData = errorData;
      }
    });
  }

  getCompletedSmallboxByContainerId(id: number): void {
    this.smallBoxService.getCompletedSmallBoxByContainerId(id).subscribe({
      next: (completedData) => {
        this.completedSmallBox = completedData;
      },
      error: (erorrData) => {
        this.errorData = erorrData;
      },
      complete:()=>{
        this.router.navigateByUrl("/completed");
      }
    });
  }

  deleteAllbyContainerId(containerId:number):void{
    this.smallBoxService.deleteAllByContainerId(containerId).subscribe();
  }

}

