import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { AppUserDto } from '../models/appUserDto';

const USER_BASE_USER = 'http://localhost:8080/api/v1/small-box/users';

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
    return this.http.get<AppUserDto[]>(`${USER_BASE_USER}/all-users`,this.httpOptions).pipe(catchError(this.handleError));
  }
}
