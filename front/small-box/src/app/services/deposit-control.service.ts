import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { SupplyDto } from '../models/SupplyDto';
import { PurchaseOrderDto } from '../models/purchaseOrderDto';
import { SupplyReportDto } from '../models/suppplyReportDto';
import { SupplyCorrectionNote } from '../models/supplyCorrectionNoteDto';
import { SupplyItemDto } from '../models/supplyItemDto';
import { PurchaseOrderItemDto } from '../models/purchaseOrderItemDto';
import { PurchaseOrderToDepositReportDto } from '../models/purchaseOrderToDepositReportDto';
import { DepositControlDto } from '../models/depositControlDto';
import { DepositDto } from '../models/depositDto';
import { DepositReponseDto } from '../models/depositReponseDto';

const DEPOSIT_CONTROL_BASE_URL = "http://localhost:8080/api/v1/smallbox/deposit-control";

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
      return throwError(()=> new Error(error.message));
     
    }

    findAllPurchaseOrdersByOrganization(organizationId:number):Observable<PurchaseOrderDto[]>{
      return this.http.get<PurchaseOrderDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/find-all-orders-by-org?organizationId=${organizationId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }

    findAllSuppliesByOrganization(organizationId:number):Observable<SupplyDto[]>{
      return this.http.get<SupplyDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/find-all-supplies-by-org?organizationId=${organizationId}`)
      .pipe(catchError(this.handleError));
    }

    findFullPurchaseOrder(purchaseOrderId:number):Observable<PurchaseOrderDto>{
      return this.http.get<PurchaseOrderDto>(`${DEPOSIT_CONTROL_BASE_URL}/find-purchase-order/${purchaseOrderId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }

    loadPurchaseOrderToDeposit(purchaseOrderId:number,depositId:number):Observable<Array<PurchaseOrderToDepositReportDto>>{
      return this.http.put<Array<PurchaseOrderToDepositReportDto>>(`${DEPOSIT_CONTROL_BASE_URL}/load-order-to-deposit?depositId=${depositId}`,purchaseOrderId)
      .pipe(catchError(this.handleError));
    }

    // createSupplyReport(supplyId:number):Observable<SupplyReportDto[]>{
    //   return this.http.get<SupplyReportDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/create-supply-report?supplyId=${supplyId}`,this.httpOptions)
    //   .pipe(catchError(this.handleError));
    // }
    createSupplyCorrectionNote(supplyId:number, depositId:number):Observable<SupplyCorrectionNote>{
      return this.http.get<SupplyCorrectionNote>(`${DEPOSIT_CONTROL_BASE_URL}/create-supply-correction-note?supplyId=${supplyId}&depositId=${depositId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }
findPuchaseOrderItems(purchaseOrderId:number):Observable<PurchaseOrderItemDto[]>{
  return this.http.get<PurchaseOrderItemDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/find-order-items?purchaseOrderId=${purchaseOrderId}`,this.httpOptions)
  .pipe(catchError(this.handleError))
}

    findSupplyItems(supplyId:number):Observable<SupplyItemDto[]>{
      return this.http.get<SupplyItemDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/find-supply-items?supplyId=${supplyId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }

    findDepositControlsByDeposit(depositId:number):Observable<DepositControlDto[]>{
      return this.http.get<DepositControlDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/find-deposit-controls-by-org?depositId=${depositId}`)
      .pipe(catchError(this.handleError));
    }
   createDeposit(depositDto:DepositDto):Observable<string>{
    return this.http.post<string>(`${DEPOSIT_CONTROL_BASE_URL}/create-deposit`,depositDto,this.httpOptions)
    .pipe(catchError(this.handleError));
   }

   findAllDepositsByOrganization(organizationId:number):Observable<DepositDto[]>{
    return this.http.get<DepositDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/find-deposits?organizationId=${organizationId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
   }

   setCurrentDeposit(userId:number,depositId:number):Observable<DepositReponseDto>{
    return this.http.put<DepositReponseDto>(`${DEPOSIT_CONTROL_BASE_URL}/set-current-deposit?userId=${userId}`,depositId,this.httpOptions)
    .pipe(catchError(this.handleError));
   }
   getCurrentDeposit(userId:number):Observable<DepositReponseDto>{
    return this.http.get<DepositReponseDto>(`${DEPOSIT_CONTROL_BASE_URL}/get-current-deposit?userId=${userId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
   }
}
