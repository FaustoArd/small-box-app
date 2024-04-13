import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { OrganizationDto } from 'src/app/models/organizationDto';
import {  UserMainOrganizationResponse } from 'src/app/models/userMainOrganizationResponse';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';

@Component({
  selector: 'app-user-panel',
  templateUrl: './user-panel.component.html',
  styleUrls: ['./user-panel.component.css']
})
export class UserPanelComponent implements OnInit {

selectedOrganizationId!:number;
organizations:Array<OrganizationDto>=[];

orgResponse!:number;
  constructor(private organizationService:OrganizationService,private snackBar:SnackBarService,
    private cookieService:CookieStorageService,private formBuilder:FormBuilder,private router:Router){}


    ngOnInit(): void {
        this.getMainUserOrganization();
        this.getAllOrganizationsByUserId();
    }

    getMainUserOrganization():number{
      const userId = Number(this.cookieService.getCurrentUserId());
      this.organizationService.getMainUserOrganizationId(userId).subscribe({
        next:(orgId)=>{
          this.orgResponse = orgId;
        },
        error:(errorData)=>{
          this.snackBar.openSnackBar(errorData,'Cerrar',3000);
        },complete:()=>{
          this.snackBar.openSnackBar('Organization id: ' + this.orgResponse,'Cerrar',3000);
        }
      })
      return this.orgResponse;
    }


    userMainOrganizationForm = this.formBuilder.group({
      organizationId:[0,Validators.required]
    });

    get organizationId(){
      return this.userMainOrganizationForm.controls.organizationId;
    }

   userMainOrganizationResponse!:UserMainOrganizationResponse;
    setUserMainOrganizationId(){
     
      if(this.userMainOrganizationForm.valid){
      console.log("formValue " +this.userMainOrganizationForm.value);
      this.userMainOrganizationResponse = new UserMainOrganizationResponse();
      this.userMainOrganizationResponse = Object.assign(this.userMainOrganizationResponse,this.userMainOrganizationForm.value);
      const orgId = Number(this.userMainOrganizationResponse.organizationId);
      console.log("orgid:" +orgId)
      const userId = Number(this.cookieService.getCurrentUserId());
      //Set user main organization id
      this.organizationService.setMainUserOrganizationId(orgId,userId).subscribe({
        next:(messageData)=>{
          this.cookieService.setUserMainOrganizationId(JSON.stringify(messageData));
          this.snackBar.openSnackBar("Se guardo la organization con id: " + messageData,'Cerrar',3000);
        },error:(errorData)=>{
          this.snackBar.openSnackBar(errorData,'Cerrar',3000);
        },
        complete:()=>{
          this.router.navigateByUrl("/home");
        }
      });
    }else{
      this.snackBar.openSnackBar("Organizacion invalida",'Cerrar',3000);
    }
    }

    getAllOrganizationsByUserId(){
      const userId = Number(this.cookieService.getCurrentUserId());
      this.organizationService.getAllOrganizationsByUser(userId).subscribe({
        next:(orgsData)=>{
          this.organizations= orgsData;
          this.organizations.forEach(e => console.log(e.id));
        },
        error:(errorData)=>{
          this.snackBar.openSnackBar(errorData,'Cerrar',3000);
        }
      });
    }

}
