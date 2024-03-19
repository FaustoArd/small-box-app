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
import { SmallBoxTypeDto } from 'src/app/models/smallBoxTypeDto';

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.css']
})

//This component take charge of the container creation,
//here, the user select the SmallBox type and the Organization.
export class ContainerComponent implements OnInit {


container!:ContainerDto;
errorData!:string;
returnedData!:ContainerDto;
smallBoxTypes: SmallBoxTypeDto[]=[];
organizations:OrganizationDto[]=[];



  constructor(private formBuilder: FormBuilder,private containerService:ContainerService,
    private router:Router,private cookieService:CookieStorageService,private snackBar:SnackBarService,
    private organizationService:OrganizationService){ }


  ngOnInit(): void {
    this.getOrganizationsByUserId();
    this.getSmallBoxTypes();
  }

  //Get the smallBoxType from backend
  getSmallBoxTypes(){
   this.containerService.getAllSmallBoxesTypes().subscribe({
    next:(smTypesData)=>{
      this.smallBoxTypes = smTypesData;
    },
    error:(errorData)=>{
      this.snackBar.openSnackBar(errorData,'Cerrar',3000);
    }
   })
  }
//Container form Builder
  containerFormBuilder = this.formBuilder.group({
   smallBoxType:['', Validators.required],
    organizationId:[0, Validators.required]
   
});

//Getters
get smallBoxType(){
    return this.containerFormBuilder.controls.smallBoxType;
  }
  get organizationId(){
    return this.containerFormBuilder.controls.organizationId;
  }

 

  //Send new container to backend 
  onCreateContainer(){
    if(this.containerFormBuilder.valid){
      //Assign Form builder to ContainerDto class
   this.container = new ContainerDto();
      this.container = Object.assign(this.container,this.containerFormBuilder.value)
   this.containerService.createContainer(this.container).subscribe({
        //Return container data.
        next:(contData)=>{
          this.returnedData = contData;
          this.cookieService.deleteCurrentContainerId();
          
          //Save current container id to cookies
          this.cookieService.setCurrentContainerId(JSON.stringify(this.returnedData.id));
         },
        error:(errorData)=>{
          this.errorData = errorData;
          this.snackBar.openSnackBar(errorData,'Cerrar',4000);
        },
        complete:()=>{
          this.router.navigateByUrl("/small-box")
        }
      })
    }
  }

  //Get organizations by current user
  getOrganizationsByUserId():void{
    this.organizationService.getAllOrganizationsByUser(Number(this.cookieService.getCurrentUserId())).subscribe({
      next:(orgsData)=>{
        this.organizations = orgsData;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    })
  };

}
