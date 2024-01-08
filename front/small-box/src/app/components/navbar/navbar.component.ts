import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {


permission!:boolean;

constructor(private cookieService:CookieStorageService,private router:Router){}


ngOnInit(): void {
    if(this.decodeToken()){
      this.permission==true;
    }
}

  onLogout(){
    this.cookieService.deleteToken();
    this.router.navigateByUrl("/login")
  }
  
  decodeToken():boolean{
    let token = this.cookieService.getToken();
    let decodedJWT = JSON.parse(window.atob(token.split('.')[1]));
    if(decodedJWT.roles==='ADMIN'){
      return true;
    }
    return false;
  }


}
