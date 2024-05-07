import { Injectable } from '@angular/core';
import  { Observable, catchError, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { AppUserRegistrationDto } from '../models/appUserRegistrationDto';
import { LoginDto } from '../models/loginDto';
import { LoginResponseDto } from '../models/loginResponseDto';
import { AppUserUpdateDto } from '../models/appUserUpdateDto';

const AUTHORIZATION_BASE_URL = 'http://localhost:8080/api/v1/smallbox/authorization';

const REGISTRATION_BASE_URL =  'http://localhost:8080/api/v1/smallbox/registration';


@Injectable({
  providedIn: 'root'
})
export class AuthorizationService {

  constructor(private http:HttpClient) { }

  httpOptions =  {
    headers : new HttpHeaders({
      'Content-Type':'application/json'
    })
  };

  private handleError(error: HttpErrorResponse){
    return throwError(() => new Error(error.error));
 }

 loginUser(loginDto:LoginDto):Observable<LoginResponseDto>{
  return this.http.post<LoginResponseDto>(`${AUTHORIZATION_BASE_URL}/login`,loginDto,this.httpOptions)
  .pipe(catchError(this.handleError));
 }

 registerUser(registrationDto:AppUserRegistrationDto,authority:string):Observable<AppUserRegistrationDto>{
  return this.http.post<AppUserRegistrationDto>(`${REGISTRATION_BASE_URL}/register?authority=${authority}`
  ,registrationDto,this.httpOptions).pipe(catchError(this.handleError));
 }
 updateUser(updateUserDto:AppUserUpdateDto,authority:string):Observable<AppUserUpdateDto>{
  return this.http.put<AppUserUpdateDto>(`${REGISTRATION_BASE_URL}/update?authority=${authority}`,updateUserDto,this.httpOptions)
  .pipe(catchError(this.handleError));
 }


}
