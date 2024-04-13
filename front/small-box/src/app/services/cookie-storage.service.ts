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

  setUserMainOrganizationId(mainUserOrganizationId:string){
    this.cookieService.set('mainUserOrganizationId',mainUserOrganizationId);
  }
  getUserMainOrganizationId(){
    return this.cookieService.get('mainUserOrganizationId')
  }
  deleteMainUserOrganizationId(){
    this.cookieService.delete('mainUserOrganizationId');
  }

  setCurrentDepositSelectedId(currentDepositSelectedId:string){
    this.cookieService.set('currentDepositSelectedId',currentDepositSelectedId);
  }
  getCurrentDepositSelectedId(){
    return this.cookieService.get('currentDepositSelectedId');
  }
  deleteCurrentDepositSelectedId(){
    this.cookieService.delete('currentDepositSelectedId');
  }

  setDestinationsList(destination:string){
    const destinationsListResult = JSON.stringify(destination);

    this.cookieService.set('destinationsList',destinationsListResult);
  }
  getDestinationsList(){
   const destinationListResult = this.cookieService.get('destinationsList');
   return  JSON.parse("[" + destinationListResult + "]");
  }
  deleteDestinationsList(){
    this.cookieService.delete('destinationsList');
  }

  setCurrentWorkTemplateId(workTemplateId:string){
    this.cookieService.set('workTemplateId', workTemplateId);
  }
  getCurrentWorkTemplateId(){
    return this.cookieService.get('workTemplateId');
       
    }
  deleteCurrentWorkTemplateId(){
    this.cookieService.delete('workTemplateId');
  }

  getCurrentDispatchOrganizationId(){
    return this.cookieService.get('organizationId');
  }
  setCurrentDispatchOrganizationId(organizationId:string){
     this.cookieService.set('organizationId',organizationId);
 }
 deleteCurrentDispatchOrganizationId(){
  this.cookieService.delete('organizationId');
 }

}
