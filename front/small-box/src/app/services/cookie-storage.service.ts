import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class CookieStorageService {

  cookieValue!:string;
  constructor(private cookieService:CookieService) { }



  setToken(token:string){
    this.cookieService.set('token', token);
}
  getToken(){
    return this.cookieService.get('token');
  }
  deleteToken(){
    this.cookieService.delete('token');
  }
}
