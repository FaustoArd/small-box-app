import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {


adminAuth!:boolean;
superUserAuth!:boolean;
currentUsername!:string;
destinationsList!:Array<string>

constructor(private cookieService:CookieStorageService,private router:Router){}


ngOnInit(): void {
    this.adminAuth = this.decodeAdminToken();
    this.superUserAuth = this.decodeSuperUserToken();
    this.currentUsername = this.cookieService.getCurrentUsername();
    
}
onLogout(){
    this.cookieService.deleteToken();
    this.router.navigateByUrl("/login")
  }
  
  decodeAdminToken():boolean{
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


}
