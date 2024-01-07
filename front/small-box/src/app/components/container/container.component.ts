import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { ContainerService } from 'src/app/services/container.service';
import { ContainerDto } from 'src/app/models/containerDto';
import { Router } from '@angular/router';
import { StorageService } from 'src/app/services/storage.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { CookieService } from 'ngx-cookie-service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { OrganizationComponent } from '../authorization/organization/organization.component';
import { OrganizationDto } from 'src/app/models/organizationDto';

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.css']
})
export class ContainerComponent implements OnInit {


container!:ContainerDto;
errorData!:string;
returnedData!:ContainerDto;
smallBoxTypes: string[] = [];
organizations:OrganizationDto[]=[];



  constructor(private formBuilder: FormBuilder,private containerService:ContainerService,
    private router:Router,private cookieService:CookieStorageService,private snackBar:SnackBarService,
    private organizationService:OrganizationService){ }


  ngOnInit(): void {
    this.getOrganizationsByUserId();
    this.getSmallBoxTypes();
  }

  getSmallBoxTypes(){
    this.smallBoxTypes = ["CHICA", "ESPECIAL"];
  }

  containerFormBuilder = this.formBuilder.group({
   title:['', Validators.required],
    organization:['', Validators.required],
    responsible:['',Validators.required]
});

get title(){
    return this.containerFormBuilder.controls.title;
  }
  get organization(){
    return this.containerFormBuilder.controls.organization;
  }

  get responsible(){
    return this.containerFormBuilder.controls.responsible;
  }

  onCreateContainer(){
    if(this.containerFormBuilder.valid){
      this.container = new ContainerDto();
      this.container = Object.assign(this.container,this.containerFormBuilder.value)
      this.containerService.createContainer(this.container).subscribe({
        next:(contData)=>{
          this.returnedData = contData;
          this.cookieService.deleteCurrentContainerId();
          this.cookieService.setCurrentContainerId(JSON.stringify(this.returnedData.id));
         },
        error:(errorData)=>{
          this.errorData = errorData;
          if(errorData===401){
          this.router.navigateByUrl("login")
          }
        },
        complete:()=>{
          this.router.navigateByUrl("/small-box")
        }
      })
    }
  }

  getOrganizationsByUserId():void{
    this.organizationService.getAllOrganizationsByUser(Number(this.cookieService.getCurrentUserId())).subscribe({
      next:(orgsData)=>{
        this.organizations = orgsData;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    })
  }

}
