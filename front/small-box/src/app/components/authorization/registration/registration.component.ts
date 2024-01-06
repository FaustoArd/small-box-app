import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthorityDto } from 'src/app/models/authorityDto';
import { AppUserRegistrationDto } from 'src/app/models/appUserRegistrationDto';
import { AuthorizationService } from 'src/app/services/authorization.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

registrationDto!:AppUserRegistrationDto;

regResponseDto!:AppUserRegistrationDto;

strAuthority!:string;

authorityDto!:AuthorityDto;

authorities:string[] = [];



  constructor(private cookieService:CookieStorageService, private formBuilder:FormBuilder,
    private authorizationService:AuthorizationService){}

  ngOnInit(): void {
     this.getRoles();

  }

  registerForm = this.formBuilder.group({
    name:['', Validators.required],
    lastname:['', Validators.required],
    email:['', Validators.required],
    username:['', Validators.required],
    password:['', Validators.required],
    repeatedPassword:['', Validators.required],

    
  });

  authorityForm =  this.formBuilder.group({
    authority:['', Validators.required]
  });

 

  get name(){
    return this.registerForm.controls.name;
  }
  get lastname(){
    return this.registerForm.controls.lastname;

  }
  get email(){
    return this.registerForm.controls.email;
  }
  get username(){
    return this.registerForm.controls.username;
  }
  get password(){
    return this.registerForm.controls.password;
  }
  get repeatedPassword(){
    return this.registerForm.controls.repeatedPassword;
  }
  get authority(){
    return this.authorityForm.controls.authority;
  }
  getRoles(){
  this.authorities = ['ADMIN','SUPERUSER','USER'];
  }

  onSubmit(){
    console.log(this.registerForm.value)
    if(this.registerForm.value.password!==this.registerForm.value.repeatedPassword){
      console.log("password are different!!");
     
    }else{
      if(this.registerForm.valid && this.authorityForm.valid){
        this.registrationDto = new AppUserRegistrationDto();
        this.registrationDto = Object.assign(this.registrationDto,this.registerForm.value);
        this.authorityDto = new AuthorityDto();
        this.authorityDto = Object.assign(this.authorityDto,this.authorityForm.value);
        console.log(this.authorityDto.authority)
        this.authorizationService.registerUser(this.registrationDto,this.authorityDto.authority).subscribe({
          next:(regData)=>{
            this.regResponseDto = regData;
            console.log(this.regResponseDto.username);
          },
          error:(errorData)=>{
            console.log(errorData);
          }
        });
      }else{
        console.log('Error en los dtos')
      }
    }
   
  }




}
