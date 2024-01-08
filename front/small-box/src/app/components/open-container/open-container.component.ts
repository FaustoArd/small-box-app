import { Component, OnInit } from '@angular/core';
import { ContainerDto } from 'src/app/models/containerDto';
import { ContainerService } from 'src/app/services/container.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
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

  constructor(private containerService:ContainerService,private cookieService:CookieStorageService,private snackBar:SnackBarService){}

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

  setCurrentContainerId(containerId:number){
    this.cookieService.setCurrentContainerId(JSON.stringify(containerId));
  }

  getAllContainersByOrganizationsByUser():void{
    this.containerService.getAllContainersByOrganizationsbyUser(Number(this.cookieService.getCurrentUserId())).subscribe({
      next:(containersData)=>{
        this.containers = containersData;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }

    })
  }

  
}
