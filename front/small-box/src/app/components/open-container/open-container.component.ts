import { Component, OnInit } from '@angular/core';
import { ContainerDto } from 'src/app/models/containerDto';
import { ContainerService } from 'src/app/services/container.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-open-container',
  templateUrl: './open-container.component.html',
  styleUrls: ['./open-container.component.css']
})
export class OpenContainerComponent implements OnInit {

  containers:ContainerDto[]= [];
  errorData!:string;

  constructor(private containerService:ContainerService,private storageService:StorageService,private snackBar:SnackBarService){}

  ngOnInit(): void {
      this.getAllContainers();
  }

  getAllContainers():void{
    this.containerService.getAllContainers().subscribe({
      next:(containersData)=>{
        this.containers = containersData;
      },
      error:(errorData)=>{
        this.errorData = errorData;
        this.snackBar.openSnackBar(errorData + ", cierre sesion y vuelva a logearse",'Cerrar',3000);
      }
    })
  }

  setCurrentContainerId(id:number){
    this.storageService.setCurrentContainerId(JSON.stringify(id));
  }

  
}
