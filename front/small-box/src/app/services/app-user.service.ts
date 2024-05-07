import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { AppUserDto } from '../models/appUserDto';

const USER_BASE_URL = 'http://localhost:8080/api/v1/smallbox/users';

@Injectable({
  providedIn: 'root'
})
export class AppUserService {

  constructor(private http:HttpClient) { }


  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':'application/json'
    })
  }

  private handleError(error:HttpErrorResponse):Observable<any>{
    return throwError(() => new Error(error.error));
  }

  getUsers():Observable<AppUserDto[]>{
    return this.http.get<AppUserDto[]>(`${USER_BASE_URL}/all-users`,this.httpOptions).pipe(catchError(this.handleError));
  }

  getUsersWithAutorities():Observable<AppUserDto[]>{
    return this.http.get<AppUserDto[]>(`${USER_BASE_URL}/all-users-with-authorities`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  getUserById(id:number):Observable<AppUserDto>{
    return this.http.get<AppUserDto>(`${USER_BASE_URL}/by-id/${id}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  deleteUserById(id:number):Observable<string>{
    return this.http.delete<string>(`${USER_BASE_URL}/delete-by-id/${id}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }
}
