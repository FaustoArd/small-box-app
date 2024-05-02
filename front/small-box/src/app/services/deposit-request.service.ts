import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { DepositRequestDto } from '../models/depositRequestDto';
import { DepositReceiverDto } from '../models/depositReceiverDto';
import { DepositControlRequestDto } from '../models/depositControlRequestDto';

const DEPOSIT_REQUEST_BASE_URL = 'http://localhost:8080/api/v1/smallbox/deposit-request'

@Injectable({
  providedIn: 'root'
})
export class DepositRequestService {

  constructor(private http:HttpClient) { }

  httpOptions = {
    headers : new HttpHeaders({
      'Content-Type':'application/json'
    })
  }
  
    handleError(error:HttpErrorResponse){
      return throwError(()=> new Error(error.error));
     
    }

    createRequest(depositRequestDto:DepositRequestDto):Observable<DepositRequestDto>{
      return this.http.post<DepositRequestDto>(`${DEPOSIT_REQUEST_BASE_URL}/create-request`
      ,depositRequestDto,this.httpOptions).pipe(catchError(this.handleError))
    }

    saveItemsToRequest(depositRequestDto:DepositRequestDto):Observable<DepositRequestDto>{
      return this.http.post<DepositRequestDto>(`${DEPOSIT_REQUEST_BASE_URL}/save-items-to-request`,depositRequestDto,this.httpOptions)
      .pipe(catchError(this.handleError));
    }
    sendRequest(depositRequestId:number,destinationOrganizationId:number):Observable<string>{
      return this.http.post<string>
      (`${DEPOSIT_REQUEST_BASE_URL}/send-request?depositRequestId=${depositRequestId}&destinationOrganizationId=${destinationOrganizationId}`
      ,this.httpOptions).pipe(catchError(this.handleError));
    }
    findAllDepositRequestsByOrganizationByUserId(userId:number):Observable<DepositRequestDto[]>{
      return this.http.get<DepositRequestDto[]>(`${DEPOSIT_REQUEST_BASE_URL}/find-requests?userId=${userId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }
    findAllControlRequestsByRequest(depositRequestId:number):Observable<DepositControlRequestDto[]>{
      return this.http.get<DepositControlRequestDto[]>
      (`${DEPOSIT_REQUEST_BASE_URL}/find-control-requests?depositRequestId=${depositRequestId}`).pipe(catchError(this.handleError))
    }
}
