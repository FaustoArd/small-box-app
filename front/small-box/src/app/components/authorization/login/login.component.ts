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

  loginForm = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  get username() {
    return this.loginForm.controls.username;
  }
  get password() {
    return this.loginForm.controls.password;
  }



  onSubmit(): void {
    this.loginDto = new LoginDto();
    this.loginDto = Object.assign(this.loginDto, this.loginForm.value);

    if (this.loginForm.valid) {
      this.authorizationService.loginUser(this.loginDto).subscribe({
        next: (tokenData) => {
          this.cookieService.setToken(tokenData.token);
        
        },
        error: (errorData) => {
          this.snackBarService.openSnackBar(errorData, 'Cerrar',3000);
          this.router.navigateByUrl('/login');
        },
        complete: () => {
          this.router.navigateByUrl('/home');
        }
      });
    } else {
      this.snackBarService.openSnackBar('Alguno de los datos es invalido...', 'Cerrar',3000);
    }
  }

}
