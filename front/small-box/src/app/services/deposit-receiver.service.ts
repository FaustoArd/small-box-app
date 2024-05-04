import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { DepositReceiverDto } from '../models/depositReceiverDto';
import { DepositControlReceiverDto } from '../models/depositControlReceiverDto';

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
    markAsReaded(depositReceiverId:number):Observable<boolean>{
      return this.http.get<boolean>
      (`${DEPOSIT_RECEIVER_BASE_URL}/mark-as-readed?depositReceiverId=${depositReceiverId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }
    countMessages(organizationId:number):Observable<number>{
      return this.http.get<number>(`${DEPOSIT_RECEIVER_BASE_URL}/count-messages?organizationId=${organizationId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }
    findAllControlReceiversByReceiver(depositReceiverId:number):Observable<DepositControlReceiverDto[]>{
      return this.http.get<DepositControlReceiverDto[]>
      (`${DEPOSIT_RECEIVER_BASE_URL}/find-all-control-receivers-by-receiver?depositReceiverId=${depositReceiverId}`)
      .pipe(catchError(this.handleError));
    }
}
