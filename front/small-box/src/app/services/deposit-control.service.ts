import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { SupplyDto } from '../models/SupplyDto';
import { PurchaseOrderDto } from '../models/purchaseOrderDto';
import { SupplyReportDto } from '../models/suppplyReportDto';
import { SupplyCorrectionNote } from '../models/supplyCorrectionNoteDto';

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

    loadPurchaseOrderToDeposit(purchaseOrderId:number):Observable<Array<string>>{
      return this.http.put<Array<string>>(`${DEPOSIT_CONTROL_BASE_URL}/load-order-to-deposit`,purchaseOrderId)
      .pipe(catchError(this.handleError));
    }

    createSupplyReport(supplyId:number):Observable<SupplyReportDto[]>{
      return this.http.get<SupplyReportDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/create-supply-report?supplyId=${supplyId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }
    createSupplyCorrectionNote(supplyId:number):Observable<SupplyCorrectionNote>{
      return this.http.get<SupplyCorrectionNote>(`${DEPOSIT_CONTROL_BASE_URL}/create-supply-correction-note?supplyId=${supplyId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }
   

}
