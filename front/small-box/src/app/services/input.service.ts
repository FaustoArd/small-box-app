import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders,HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError } from 'rxjs';
import { InputDto } from '../models/inputDto';

const INPUT_BASE_URL = 'http://localhost:8080/api/v1/small-box/inputs'

@Injectable({
  providedIn: 'root'
})
export class InputService {

  constructor(private http:HttpClient) { }

  httpOptions = { 
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

  handleError(error:HttpErrorResponse):Observable<any>{
    if(error.status===500){
       throw catchError(error.error)
    }else if(error.status===404){
      throw catchError(error.error)
    }else{
      throw catchError(error.error)
    }
  }

  findAllInputs():Observable<InputDto[]>{
    return this.http.get<InputDto[]>(`${INPUT_BASE_URL}/all`,this.httpOptions).pipe(catchError(this.handleError));
  }
  findAllInputsByExample():Observable<InputDto>{
    return this.http.get<InputDto>(`${INPUT_BASE_URL}/example`,this.httpOptions).pipe(catchError(this.handleError));
  }
}
