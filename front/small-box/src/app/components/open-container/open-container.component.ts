import { Component, OnInit, TemplateRef } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ContainerDto } from 'src/app/models/containerDto';
import { SmallBoxTypeDto } from 'src/app/models/smallBoxTypeDto';
import { ContainerService } from 'src/app/services/container.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { DialogService } from 'src/app/services/dialog.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { StorageService } from 'src/app/services/storage.service';
import { DialogTemplateComponent } from '../dialog/dialog-template/dialog-template.component';
import { OrganizationService } from 'src/app/services/organization.service';
import { OrganizationDto } from 'src/app/models/organizationDto';

@Component({
  selector: 'app-open-container',
  templateUrl: './open-container.component.html',
  styleUrls: ['./open-container.component.css']
})
export class OpenContainerComponent implements OnInit {

  container!:ContainerDto;
  containerShow!:ContainerDto;
smallBoxTypes:SmallBoxTypeDto[] =[];
organizations:OrganizationDto[]=[];
private matDialogRef!: MatDialogRef<DialogTemplateComponent>

  containers:ContainerDto[]= [];
   constructor(private containerService:ContainerService,private cookieService:CookieStorageService,private snackBarService:SnackBarService
    ,private formBuilder:FormBuilder,private dialogService:DialogService,private organizationService:OrganizationService){}

  ngOnInit(): void {
      this.getAllContainersByOrganizationsByUser();
  }

  getAllContainers():void{
    this.containerService.getAllContainers().subscribe({
      next:(containersData)=>{
        this.containers = containersData;
      },
      error:(errorData)=>{
      
        this.snackBarService.openSnackBar(errorData + ", cierre sesion y vuelva a logearse",'Cerrar',3000);
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
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      }

    });
  }

  containerForm = this.formBuilder.group({
    id:[0,Validators.required],
    smallBoxType:['', Validators.required],
    organization:['',Validators.required]
 
  });

  ondUpdateContainerShow(container:ContainerDto):void{
    this.containerForm.patchValue({
      id: container?.id,
      smallBoxType: container?.smallBoxType,
      organization: container?.organization
    
    });
  }

  get id(){
    return this.containerForm.controls.id;
  }

  get smallBoxType(){
    return this.containerForm.controls.smallBoxType;
  }
  get organization(){
  return this.containerForm.controls.organization;
  }

 

  openDialogUpdateContainer(id:number,template:TemplateRef<any>){
    this.getContainerById(id);
    this.getAllSmallBoxTypes();
    this.getOrganizationsByUserId();
    this.matDialogRef = this.dialogService.openDialogCreation({
      template
    });

  }

  updateContainer():void{
    if(this.containerForm.valid){
      this.container = new ContainerDto();
      this.container = Object.assign(this.container,this.containerForm.value);
      this.containerService.updateContainer(this.container).subscribe({
        next:(contData)=>{
          this.snackBarService.openSnackBar('Se actualizo la rendicion: ' + contData.smallBoxType + ' ,'+ contData.smallBoxDate,'Cerrar',3000);
        },
        error:(errorData)=>{
          this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
        }
      });
    }
  }

  getAllSmallBoxTypes(){
    this.containerService.getAllSmallBoxesTypes().subscribe({
      next:(smTypeData)=>{
        this.smallBoxTypes =   smTypeData;
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      }
    });
  }

  getAllOrganizations():void{
    this.organizationService.getAllOrganizations().subscribe({
      next:(orgsData)=>{
        this.organizations = orgsData;
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar', 3000);
      }
    });
  };

  getContainerById(id:number):void{
    this.containerService.getContainerById(id).subscribe({
      next:(contData)=>{
        this.containerShow = contData;
        this.ondUpdateContainerShow(this.containerShow);
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      }
    })
  }

  getOrganizationsByUserId():void{
    this.organizationService.getAllOrganizationsByUser(Number(this.cookieService.getCurrentUserId())).subscribe({
      next:(orgsData)=>{
        this.organizations = orgsData;
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      }
    })
  }
}
