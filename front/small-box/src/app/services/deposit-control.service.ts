import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { SupplyDto } from '../models/SupplyDto';

const DEPOSIT_CONTROL_BASE_URL = "http://localhost:8080/api/v1/smallbox/deposit_control";

@Injectable({
  providedIn: 'root'
})
export class DepositControlService {

  constructor(private http:HttpClient) { }

  httpOptions = {
    headers : new HttpHeaders({
      'Content-Type':'application/json'
    })
  }
  
    handleError(error:HttpErrorResponse){
      return throwError(()=> new Error(error.error));
     
    }

   

}
