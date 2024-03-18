import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthorityDto } from 'src/app/models/authorityDto';
import { AppUserRegistrationDto } from 'src/app/models/appUserRegistrationDto';
import { AuthorizationService } from 'src/app/services/authorization.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
//This component resgister new user in the application
export class RegistrationComponent implements OnInit {

registrationDto!:AppUserRegistrationDto;

regResponseDto!:AppUserRegistrationDto;

strAuthority!:string;

authorityDto!:AuthorityDto;

authorities:string[] = [];



  constructor(private cookieService:CookieStorageService, private formBuilder:FormBuilder,
    private authorizationService:AuthorizationService, private snackBarService:SnackBarService){}

  ngOnInit(): void {
     this.getRoles();

  }

//User register form builder
  registerForm = this.formBuilder.group({
    name:['', Validators.required],
    lastname:['', Validators.required],
    email:['', Validators.required],
    username:['', Validators.required],
    password:['', Validators.required],
    repeatedPassword:['', Validators.required],

    
  });

  //Role form buidler
  authorityForm =  this.formBuilder.group({
    authority:['', Validators.required]
  });

 
//Getters for control validators form
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
  //Get role list for selection
  getRoles(){
  this.authorities = ['ADMIN','SUPERUSER','USER'];
  }

  //Register user in database
  onSubmit(){
   if(this.registerForm.value.password!==this.registerForm.value.repeatedPassword){
     this.snackBarService.openSnackBar("Los password son distintos!!",'Cerrar',3000);
     
    }else{
      if(this.registerForm.valid && this.authorityForm.valid){
        //Assign user and authority form values To objects
        this.registrationDto = new AppUserRegistrationDto();
        this.registrationDto = Object.assign(this.registrationDto,this.registerForm.value);
        this.authorityDto = new AuthorityDto();
        this.authorityDto = Object.assign(this.authorityDto,this.authorityForm.value);
        //Authorization Service class, POST method, recieve RegistrationDto and AuthorityDto classes 
       this.authorizationService.registerUser(this.registrationDto,this.authorityDto.authority).subscribe({
          next:(regData)=>{
            //Return name and lastname
            this.regResponseDto = regData;
           
          },
          error:(errorData)=>{
            this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
          },
          complete:()=>{
            this.snackBarService.openSnackBar("Se registro el usuario: " + this.regResponseDto.name + " " + this.regResponseDto.lastname,'Cerrar',3000);
          }
        });
      }else{
        this.snackBarService.openSnackBar("Datos invalidos!",'Cerrar',3000);
      }
    }
   
  }




}
