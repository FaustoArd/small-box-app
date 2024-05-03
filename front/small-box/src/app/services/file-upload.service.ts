import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { DispatchControlDto } from '../models/dispatchControlDto';
import { SupplyDto } from '../models/SupplyDto';
import { PurchaseOrderDto } from '../models/purchaseOrderDto';
import { DepositItemComparatorDto } from '../models/depositItemComparatorDto';

const PDF_STRING_BASE_URL = 'http://localhost:8080/api/v1/smallbox/dispatchs';
const DEPOSIT_CONTROL_BASE_URL = 'http://localhost:8080/api/v1/smallbox/deposit-control';
const PURCHASE_ORDER_BASE_URL = 'http://localhost:8080/api/v1/smallbox/purchase-order';
const SUPPLY_BASE_URL = 'http://localhost:8080/api/v1/smallbox/supply';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  constructor(private http:HttpClient) { }

  httpOptions = { 
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

httpOptionsForm = {
    headers: new HttpHeaders({'Content-Type': 'multipart/form-data'})
  }
  handleError(error:HttpErrorResponse):Observable<any>{
    return throwError(()=> new Error(error.error));
    
  }


  sendFileToBackEnd(file:File,organizationId:number):Observable<DispatchControlDto[]>{
    const formData: FormData = new FormData;
    formData.append('file', file);
     return this.http.post<DispatchControlDto[]>(`${PDF_STRING_BASE_URL}/upload-file?organizationId=${organizationId}`,formData)
     .pipe(catchError(this.handleError));
   }

   sendPurchaseOrderPdfToBackEnd(file:File,organizationId:number):Observable<PurchaseOrderDto>{
    const formData: FormData = new FormData;
    formData.append('file', file);
    return this.http.post<PurchaseOrderDto>(`${PURCHASE_ORDER_BASE_URL}/collect-purchase-order-pdf?file=${file}&organizationId=${organizationId}`,formData)
    .pipe(catchError(this.handleError));
   }
 sendSupplyPdfToBackEnd(file:File,organizationId:number):Observable<SupplyDto>{
    const formData:FormData = new FormData;
    formData.append('file',file);
    return this.http.post<SupplyDto>(`${SUPPLY_BASE_URL}/collect-supply-pdf?file=${file}&organizationId=${organizationId}`,formData)
    .pipe(catchError(this.handleError));
   }

   sendDepositItemExcelFileToBackEnd(file:File,organizationId:number):Observable<DepositItemComparatorDto[]>{
    const formData:FormData = new FormData;
    formData.append('file',file);
    return this.http.post<DepositItemComparatorDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/excel-order-comparator?file=${file}&organizationId=${organizationId}`,formData)
    .pipe(catchError(this.handleError));
   }
}
