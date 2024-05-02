import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { DepositReceiverDto } from '../models/depositReceiverDto';

const DEPOSIT_RECEIVER_BASE_URL = 'http://localhost:8080/api/v1/smallbox/deposit-receiver'

@Injectable({
  providedIn: 'root'
})
export class DepositReceiverService {

  constructor(private http:HttpClient) { }

  httpOptions = {
    headers : new HttpHeaders({
      'Content-Type':'application/json'
    })
  }
  
    handleError(error:HttpErrorResponse){
      return throwError(()=> new Error(error.error));
     
    }

    findAllReceiversByOrganization(organizationId:number):Observable<DepositReceiverDto[]>{
      return this.http.get<DepositReceiverDto[]>
      (`${DEPOSIT_RECEIVER_BASE_URL}/find-receivers-by-organization?organizationId=${organizationId}`)
      .pipe(catchError(this.handleError));
    }
}
