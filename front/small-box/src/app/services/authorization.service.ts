import { Injectable } from '@angular/core';
import  { Observable, catchError, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { AppUserRegistrationDto } from '../models/appUserRegistrationDto';
import { LoginDto } from '../models/loginDto';
import { LoginResponseDto } from '../models/loginResponseDto';

const AUTHORIZATION_BASE_URL = 'http://localhost:8080/api/v1/small-box/authorization';

const REGISTRATION_BASE_URL =  'http://localhost:8080/api/v1/small-box/registration';


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
    if(error.status===0){
     return throwError(() => new Error(error.error));
    }else if(error.status===500){
      return throwError(() => new Error('Error en el servidor...'));
   
    }
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

 addOrganizationToUser(userId:number,organizationsId:Array<number>):Observable<string>{
  return this.http.put<string>(`${REGISTRATION_BASE_URL}/add-organization?userId=${userId}&organizationsId=${organizationsId}`
  ,this.httpOptions).pipe(catchError(this.handleError));
 }

 test():Observable<string>{
  return this.http.get<string>(`${AUTHORIZATION_BASE_URL}/test`, this.httpOptions).pipe(catchError(this.handleError));
 }

}
