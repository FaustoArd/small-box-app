import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { LoginDto } from 'src/app/models/loginDto';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

constructor(private formBuilder:FormBuilder){}

loginDto!:LoginDto;


loginForm = this.formBuilder.group({
  username:['', Validators.required],
  password:['', Validators.required]
});

get username(){
  return this.loginForm.controls.username;
}
get password(){
  return this.loginForm.controls.password;
}

onSubmit(){
  
}

}
