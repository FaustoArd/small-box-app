import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class CookieStorageService {

  cookieValue!:string;
  constructor(private cookieService:CookieService) { }


  test(){
    this.cookieService.set('Test', 'hello world');
    this.cookieValue = this.cookieService.get('Test');
    console.log("OK!"  + this.cookieValue)
  }
}
