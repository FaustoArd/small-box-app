import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { DepositReceiverService } from 'src/app/services/deposit-receiver.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

userMainOrganizationName!:string; 
userMainOrganizationSelected!:boolean; 
userAuth!:boolean;
adminAuth!:boolean;
superUserAuth!:boolean;
currentUsername!:string;
destinationsList!:Array<string>
messageQuantity!:number;
constructor(private cookieService:CookieStorageService,private router:Router,private organizationService:OrganizationService
  ,private snackBarService:SnackBarService,private depositReceiverService:DepositReceiverService){}


ngOnInit(): void {
  this.decodeToken();
    /*this.adminAuth = this.decodeAdminToken();
    this.superUserAuth = this.decodeSuperUserToken();
    this.userAuth = this.decodeUserToken();*/
    this.getUserOrg();
    this.currentUsername = this.cookieService.getCurrentUsername();
    this.countDepositReceiverMessages();
    
}
onLogout(){
    this.cookieService.deleteToken();
    this.cookieService.deleteCurrentContainerId();
    this.cookieService.deleteCurrentDepositSelectedId();
    this.cookieService.deleteCurrentUserId();
    this.cookieService.deleteCurrentUsername();
    this.cookieService.deleteMainUserOrganizationId();
    this.router.navigateByUrl("/login")
  }

  decodeToken():boolean{
    let token = this.cookieService.getToken();
    let decodedJWT = JSON.parse(window.atob(token.split('.')[1]));
    if(decodedJWT.roles==='ADMIN'){
      return this.adminAuth = true;
    }else if(decodedJWT.roles==='SUPERUSER'){
      return this.superUserAuth= true;
    }else if(decodedJWT.roles==='USER'){
      return this.userAuth = true;
    }else{
      return false;
    }
  }
  getUserOrg(){
    const userId = Number(this.cookieService.getCurrentUserId());
    this.organizationService.getMainUserOrganizationId(userId).subscribe({
      next:(orgIdData)=>{
        if(orgIdData<1){
          this.userMainOrganizationName = "SIN ASIGNAR";
          this.userMainOrganizationSelected = false;
        }else{
          this.getOrganization(orgIdData);
        }
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      }
    })
  }

  getOrganization(orgId:number){
    this.organizationService.getOrganizationById(orgId).subscribe({
        next:(orgData)=>{
          this.userMainOrganizationName = orgData.organizationName;
          this.userMainOrganizationSelected = true;
          this.cookieService.setUserMainOrganizationId(JSON.stringify(orgData.id));
        },
        error:(errorData)=>{
          this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
        }
      });
    
  }

  countDepositReceiverMessages(){
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    this.depositReceiverService.countMessages(orgId).subscribe({
      next:(messageQuantityData)=>{
        this.messageQuantity = messageQuantityData;
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      }
    })
  }
  
 /* decodeAdminToken():boolean{
    let token = this.cookieService.getToken();
    let decodedJWT = JSON.parse(window.atob(token.split('.')[1]));
    if(decodedJWT.roles==='ADMIN'){
      return true;
    }
    return false;
  }

  decodeSuperUserToken():boolean{
    let token = this.cookieService.getToken();
    let decodedJWT = JSON.parse(window.atob(token.split('.')[1]));

    if(decodedJWT.roles==='SUPERUSER'){
      return true;
    }
    return false;
  }

  decodeUserToken():boolean{
    let token = this.cookieService.getToken();
    let decodedJWT = JSON.parse(window.atob(token.split('.')[1]));
    console.log(decodedJWT.role)
    if(decodedJWT.role==='USER'){
      return true;
    }else{
      return false;
    }
  } */


}
