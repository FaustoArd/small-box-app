import { Component, OnInit, TemplateRef } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthorityDto } from 'src/app/models/authorityDto';
import { AppUserRegistrationDto } from 'src/app/models/appUserRegistrationDto';
import { AuthorizationService } from 'src/app/services/authorization.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { AppUserService } from 'src/app/services/app-user.service';
import { AppUserDto } from 'src/app/models/appUserDto';
import { MatDialogRef } from '@angular/material/dialog';
import { DialogTemplateComponent } from '../../dialog/dialog-template/dialog-template.component';
import { DialogService } from 'src/app/services/dialog.service';
import { AppUserUpdateDto } from 'src/app/models/appUserUpdateDto';

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
disableSelect:boolean=true;


  constructor(private cookieService:CookieStorageService, private formBuilder:FormBuilder,
    private authorizationService:AuthorizationService, private snackBarService:SnackBarService
  ,private appUserService:AppUserService,private dialogService:DialogService){}

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
  appUserDtos:AppUserDto[]=[];
  getAllUsers(){
    this.appUserService.getUsersWithAutorities().subscribe({
      next:(userDatas)=>{
        this.appUserDtos = userDatas;
        console.log(this.appUserDtos);
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      }
    });
  }
onCloseUserListTemplate(){
  this.userListTemplateMatDialogRef.close();
}

  private userListTemplateMatDialogRef!: MatDialogRef<DialogTemplateComponent>
  openUserListTemplate(template:TemplateRef<any>){
    this.getAllUsers();
    this.userListTemplateMatDialogRef = this.dialogService.openCustomDialogCreation({
      template
    },'70%','70%',true,true);
    this.userListTemplateMatDialogRef.afterClosed().subscribe();
  }

  updateUserForm = this.formBuilder.group({
  id:[0,Validators.required],
  name:['', Validators.required],
  lastname:['', Validators.required],
  email:['', Validators.required],
  username:['', Validators.required],
  password:['', Validators.required],
  repeatedPassword:['', Validators.required],
  
})


updateAuthorityForm =  this.formBuilder.group({
  authority:['', Validators.required]

});
onCloseUpdateUserTemplate(){
  this.updateUserTemplateMatDialogRef.close();
}

private updateUserTemplateMatDialogRef!: MatDialogRef<DialogTemplateComponent>
openUpdateUserTemplate(template:TemplateRef<any>,userId:number){
  this.getUserById(userId);
 
  this.updateUserTemplateMatDialogRef = this.dialogService.openSupplyCorrectionNoteCreation({
    template
  });
  this.updateUserTemplateMatDialogRef.afterClosed().subscribe();
}

get id(){
  return this.updateUserForm.controls.id;
}
findedUser!:AppUserDto;
onUpdateUserShow(userDto:AppUserDto,authority:string){
  this.updateUserForm.patchValue({
    id:this.findedUser.id,
    name:this.findedUser.name,
    lastname:this.findedUser.lastname,
    username:this.findedUser.username,
    email:this.findedUser.email

  });
  this.updateAuthorityForm.patchValue({
    authority:authority
  })
}

getUserById(id:number){
  this.appUserService.getUserById(id).subscribe({
    next:(userData)=>{
      this.findedUser = userData;
    },
    error:(errorData)=>{
      this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
    },
    complete:()=>{
    const userIndex = this.appUserDtos.findIndex(user => user.id==id);
    this.onUpdateUserShow(this.findedUser,this.appUserDtos[userIndex].authorities[0]);
    }
  })
}

  updateUserDto!:AppUserUpdateDto;
  updateAuthorityDto!:AuthorityDto;
  updateUser(){
    if(this.updateUserForm.value.password!==this.updateUserForm.value.repeatedPassword){
      this.snackBarService.openSnackBar("Los password son distintos!!",'Cerrar',3000);
      
     }else{
       if(this.updateUserForm.valid && this.updateAuthorityForm.valid){
         //Assign user and authority form values To objects
         this.updateUserDto = new AppUserUpdateDto();
         this.updateUserDto = Object.assign(this.updateUserDto,this.updateUserForm.value);
         this.updateAuthorityDto = new AuthorityDto();
         this.updateAuthorityDto = Object.assign(this.updateAuthorityDto,this.updateAuthorityForm.value);
         console.log("updated user: " + this.updateUserDto.id + "-" + this.updateAuthorityDto.authority)
         //Authorization Service class, PUT method, recieve AppUserUpdateDto and AuthorityDto classes 
        this.authorizationService.updateUser(this.updateUserDto,this.updateAuthorityDto.authority).subscribe({
           next:(regData)=>{
             //Return name and lastname
             this.regResponseDto = regData;
            
           },
           error:(errorData)=>{
             this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
           },
           complete:()=>{
             this.snackBarService.openSnackBar("Se actualizo el usuario: " + this.regResponseDto.name + " " + this.regResponseDto.lastname,'Cerrar',3000);
             this.updateAuthorityForm.reset();
             this.authorityForm.reset();
           }
         });
       }else{
         this.snackBarService.openSnackBar("Datos invalidos!",'Cerrar',3000);
       }
     }
  }

  deleteUserById(id:number){
    this.appUserService.deleteUserById(id).subscribe({
      next:(deleledUserFullNameData)=>{
        this.snackBarService.openSnackBar('Se elimino el usuario: ' + deleledUserFullNameData,'Cerrar',3000);
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      },
      complete:()=>{
        this.getAllUsers();
      }
    });
  }




}
