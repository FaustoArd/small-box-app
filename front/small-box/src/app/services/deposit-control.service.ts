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
import { BigBagDto } from '../models/bigBagDto';
import { BigBagItemDto } from '../models/bigBagItemDto';
import { ExcelItemDto } from '../models/excelItemDto';
import { OrganizationDto } from '../models/organizationDto';
import { SupplyItemRequestDto } from '../models/supplyItemRequestDto';

const DEPOSIT_CONTROL_BASE_URL = "http://localhost:8080/api/v1/smallbox/deposit-control";

const SUPPLY_BASE_URL = "http://localhost:8080/api/v1/smallbox/supply";

const PURCHASE_ORDER_BASE_URL = "http://localhost:8080/api/v1/smallbox/purchase-order";

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
      return throwError(()=> error.error);
     
    }

    findAllPurchaseOrdersByOrganization(organizationId:number):Observable<PurchaseOrderDto[]>{
      return this.http.get<PurchaseOrderDto[]>(`${PURCHASE_ORDER_BASE_URL}/find-all-orders-by-org?organizationId=${organizationId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }

    findAllSuppliesByOrganization(organizationId:number):Observable<SupplyDto[]>{
      return this.http.get<SupplyDto[]>(`${SUPPLY_BASE_URL}/find-all-supplies-by-org?organizationId=${organizationId}`)
      .pipe(catchError(this.handleError));
    }

    findFullPurchaseOrder(purchaseOrderId:number):Observable<PurchaseOrderDto>{
      return this.http.get<PurchaseOrderDto>(`${PURCHASE_ORDER_BASE_URL}/find-full-purchase-order/${purchaseOrderId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }

    loadPurchaseOrderToDeposit(purchaseOrderId:number,depositId:number):Observable<Array<PurchaseOrderToDepositReportDto>>{
      return this.http.put<Array<PurchaseOrderToDepositReportDto>>(`${PURCHASE_ORDER_BASE_URL}/load-order-to-deposit?depositId=${depositId}`,purchaseOrderId)
      .pipe(catchError(this.handleError));
    }

    // createSupplyReport(supplyId:number):Observable<SupplyReportDto[]>{
    //   return this.http.get<SupplyReportDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/create-supply-report?supplyId=${supplyId}`,this.httpOptions)
    //   .pipe(catchError(this.handleError));
    // }
    createSupplyCorrectionNote(supplyId:number, depositId:number):Observable<SupplyCorrectionNote>{
      return this.http.get<SupplyCorrectionNote>(`${SUPPLY_BASE_URL}/create-supply-correction-note?supplyId=${supplyId}&depositId=${depositId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }
findPuchaseOrderItems(purchaseOrderId:number):Observable<PurchaseOrderItemDto[]>{
  return this.http.get<PurchaseOrderItemDto[]>(`${PURCHASE_ORDER_BASE_URL}/find-order-items?purchaseOrderId=${purchaseOrderId}`,this.httpOptions)
  .pipe(catchError(this.handleError))
}

    findSupplyItems(supplyId:number):Observable<SupplyItemDto[]>{
      return this.http.get<SupplyItemDto[]>(`${SUPPLY_BASE_URL}/find-supply-items?supplyId=${supplyId}`,this.httpOptions)
      .pipe(catchError(this.handleError));
    }

    findAllDepositControlsByDeposit(depositId:number):Observable<DepositControlDto[]>{
      return this.http.get<DepositControlDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/find-deposit-controls-by-deposit?depositId=${depositId}`)
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

   setCurrentDeposit(userId:number,organizationId:number,depositId:number):Observable<DepositReponseDto>{
    return this.http.put<DepositReponseDto>
    (`${DEPOSIT_CONTROL_BASE_URL}/set-current-deposit?userId=${userId}&organizationId=${organizationId}`,depositId,this.httpOptions)
    .pipe(catchError(this.handleError));
   }
   getCurrentDeposit(userId:number,organizationId:number):Observable<DepositReponseDto>{
    return this.http.get<DepositReponseDto>
    (`${DEPOSIT_CONTROL_BASE_URL}/get-current-deposit?userId=${userId}&organizationId=${organizationId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
   }

   deletePurchaseOrderById(orderId:number):Observable<number>{
    return this.http.delete<number>(`${PURCHASE_ORDER_BASE_URL}/delete-purchase-order/${orderId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
   }
   deleteSupplyById(supplyId:number):Observable<number>{
    return this.http.delete<number>(`${SUPPLY_BASE_URL}/delete-supply/${supplyId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
   }

   findPurchaseOrderById(orderId:number):Observable<PurchaseOrderDto>{
    return this.http.get<PurchaseOrderDto>(`${PURCHASE_ORDER_BASE_URL}/find-purchase-order/${orderId}`)
    .pipe(catchError(this.handleError))
   }

   findSupplyById(supplyId:number):Observable<SupplyDto>{
    return this.http.get<SupplyDto>(`${SUPPLY_BASE_URL}/find-supply/${supplyId}`,this.httpOptions)
    .pipe(catchError(this.handleError))
   }

   createBigBag(bigBagDto:BigBagDto,organizationId:number):Observable<BigBagDto>{
    return this.http.post<BigBagDto>(`${DEPOSIT_CONTROL_BASE_URL}/create-big-bag?organizationId=${organizationId}`,bigBagDto,this.httpOptions)
    .pipe(catchError(this.handleError));
   }
   findAllBigBagsByOrg(organizationId:number):Observable<BigBagDto[]>{
    return this.http.get<BigBagDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/find-big-bags?organizationId=${organizationId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
   }

   updateBigBagItemQuantity(bigBagItemId:number,quantity:number):Observable<BigBagItemDto>{
    return this.http.get<BigBagItemDto>(`${DEPOSIT_CONTROL_BASE_URL}/update-big-bag-item-quantity?bigBagItemId=${bigBagItemId}&quantity=${quantity}`,this.httpOptions)
    .pipe(catchError(this.handleError));
   }

   findAllBigBagItems(bigBagId:number):Observable<BigBagItemDto[]>{
    return this.http.get<BigBagItemDto[]>(`${DEPOSIT_CONTROL_BASE_URL}/find-big-bag-items?bigBagId=${bigBagId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
   }

   calculateBigBagTotalquantity(bigBagId:number,depositId:number):Observable<number>{
    return this.http.get<number>(`${DEPOSIT_CONTROL_BASE_URL}/calculate-big-bag-total-quantity?bigBagId=${bigBagId}&depositId=${depositId}`)
    .pipe(catchError(this.handleError));
   }

   saveExcelItemsToDeposit(organizationId:number,depositId:number,excelItemDtos:ExcelItemDto[]):Observable<DepositControlDto[]>{
    return this.http.post<DepositControlDto[]>
    (`${DEPOSIT_CONTROL_BASE_URL}/save-excel-items-to-deposit?organizationId=${organizationId}&depositId=${depositId}`,
    excelItemDtos,this.httpOptions).pipe(catchError(this.handleError));
   }

   deleteDepositControlById(depositControlId:number):Observable<string>{
    return this.http.delete<string>(`${DEPOSIT_CONTROL_BASE_URL}/delete-deposit-control/${depositControlId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
   }

   findDepositControlbyId(depositControlId:number):Observable<DepositControlDto>{
    return this.http.get<DepositControlDto>(`${DEPOSIT_CONTROL_BASE_URL}/find-deposit-control/${depositControlId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
   }

   updateDepositControl(depositControlDto:DepositControlDto,depositId:number):Observable<DepositControlDto>{
    return this.http.put<DepositControlDto>(`${DEPOSIT_CONTROL_BASE_URL}/update-deposit-control?depositId=${depositId}`
    ,depositControlDto,this.httpOptions).pipe(catchError(this.handleError));
   }

   setSupplyOrganizationApplicant(organizationDto:OrganizationDto,supplyId:number):Observable<string>{
    return this.http.put<string>
    (`${SUPPLY_BASE_URL}/set-supply-organization-applicant?supplyId=${supplyId}`
    ,organizationDto,this.httpOptions).pipe(catchError(this.handleError));
   }

   findAllSupplyItemsByMainOrganizationAndOrganizationApplicant(mainOrganization:number,organizationApplicantId:number):Observable<SupplyItemRequestDto[]>{
    return this.http.get<SupplyItemRequestDto[]>
    (`${SUPPLY_BASE_URL}/find-all-supply-items-by-organization-applicant?mainOrganization=${mainOrganization}&organizationApplicantId=${organizationApplicantId}`
    ,this.httpOptions).pipe(catchError(this.handleError));
   }
  }
