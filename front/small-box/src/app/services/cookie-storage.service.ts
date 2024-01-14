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

  setCurrentContainerId(containerId:string){
    this.cookieService.set('containerId', containerId);
  }
 
  getCurrentContainerId(){
   return  this.cookieService.get('containerId');
  }
  deleteCurrentContainerId(){
    this.cookieService.delete('containerId');
  }

  setCurrentUserId(userId:string){
    this.cookieService.set('userId', userId);

  }
  getCurrentUserId(){
    return this.cookieService.get('userId');
  }
  deleteCurrentUserId(){
    this.cookieService.delete('userId');
  }

  setCurrentUsername(username:string){
    this.cookieService.set('username', username);
  }
  getCurrentUsername(){
    return this.cookieService.get('username');
  }
  deleteCurrentUsername(){
    this.cookieService.delete('username');
  }
}
