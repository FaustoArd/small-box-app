import { Component, OnInit } from '@angular/core';
import { SmallBoxDto } from 'src/app/models/smallBoxDto';
import { InputService } from 'src/app/services/input.service';
import { SmallBoxService } from 'src/app/services/small-box.service';
import { FormGroup,FormControl, Validators,FormBuilder } from '@angular/forms'
import { Router } from '@angular/router';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

constructor(private router:Router, private cookieService:CookieStorageService){}

  result:boolean = false;

  ngOnInit(): void {
    
    if(this.cookieService.getToken()==''){
      this.router.navigateByUrl('login');
    }else{
      this.router.navigateByUrl('home')
    }
 }

}
