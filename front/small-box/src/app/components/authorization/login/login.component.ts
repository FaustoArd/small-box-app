import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginDto } from 'src/app/models/loginDto';
import { LoginResponseDto } from 'src/app/models/loginResponseDto';
import { AuthorizationService } from 'src/app/services/authorization.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginDto!: LoginDto;
  loginResponseDto!: LoginResponseDto;
  constructor(private formBuilder: FormBuilder, private authorizationService: AuthorizationService,
    private cookieService: CookieStorageService, private router: Router, private snackBarService: SnackBarService) { }

  strRes!: string;

  ngOnInit(): void {
  
  }

  //Login Form builder
  loginForm = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  //Getters For Control Validators
  get username() {
    return this.loginForm.controls.username;
  }
  get password() {
    return this.loginForm.controls.password;
  }



  //Login Method 
  onSubmit(): void {
    //Assign Form Values(username,password) to LoginDto class
    this.loginDto = new LoginDto();
    this.loginDto = Object.assign(this.loginDto, this.loginForm.value);
    //if form control is valid
    if (this.loginForm.valid) {
      //POST Method, recieve LoginDto class
      this.authorizationService.loginUser(this.loginDto).subscribe({
        next: (tokenData) => {
         
          //Return jwtToken, userName and User id, both saved in cookies
          this.cookieService.setToken(tokenData.token);
          this.cookieService.setCurrentUserId(JSON.stringify(tokenData.userId));
          this.cookieService.setCurrentUsername(tokenData.username);
        
        
        },
        error: (errorData) => {
          //if error, reload login
          this.snackBarService.openSnackBar(errorData, 'Cerrar',3000);
          this.router.navigateByUrl('/login');
        },
        complete: () => {
          //if Completed , go main page
          this.router.navigateByUrl('/home');
        }
      });
    } else {
      //if Form Control validators error
      this.snackBarService.openSnackBar('Alguno de los datos es invalido...', 'Cerrar',3000);
    }
  }

}
