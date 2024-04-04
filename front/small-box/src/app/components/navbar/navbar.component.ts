import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { OrganizationService } from 'src/app/services/organization.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

userAuth!:boolean;
adminAuth!:boolean;
superUserAuth!:boolean;
currentUsername!:string;
destinationsList!:Array<string>

constructor(private cookieService:CookieStorageService,private router:Router,private organizationService:OrganizationService){}


ngOnInit(): void {
  this.decodeToken();
    /*this.adminAuth = this.decodeAdminToken();
    this.superUserAuth = this.decodeSuperUserToken();
    this.userAuth = this.decodeUserToken();*/
    this.currentUsername = this.cookieService.getCurrentUsername();
    
}
onLogout(){
    this.cookieService.deleteToken();
    this.router.navigateByUrl("/login")
  }

  decodeToken():boolean{
    let token = this.cookieService.getToken();
    let decodedJWT = JSON.parse(window.atob(token.split('.')[1]));
    if(decodedJWT.roles==='ADMIN'){
      return this.adminAuth = true;
    }else if(decodedJWT.roles==='SUPERUSER'){
      return this.superUserAuth= true;
    }else{
      return this.userAuth = true;
    }
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
