import { Component } from '@angular/core';
import { LoginDto } from 'src/app/models/loginDto';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

loginDto!:LoginDto;



}
