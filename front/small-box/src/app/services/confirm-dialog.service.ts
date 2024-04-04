import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { ConfirmDialogModel } from '../models/confirmDialogModel';
import { ConfirmDialogComponent } from '../components/confirm-dialog/confirm-dialog/confirm-dialog.component';
import { PurchaseOrderToDepositReportDto } from '../models/purchaseOrderToDepositReportDto';



@Injectable({
  providedIn: 'root'
})
export class ConfirmDialogService {

  constructor(private dialog:MatDialog) { }

  confirmDialog(message:string):Observable<boolean>{
    const title = 'Confirmar';
    const btnOkText = 'OK';
    const btnCancelText = 'Cancel';

    const dialogData = new ConfirmDialogModel(title,message,btnOkText,btnCancelText);

    const dialogRef = this.dialog.open(ConfirmDialogComponent,{
      maxWidth:"400px",
      data:dialogData
    });
    return dialogRef.afterClosed() as Observable<boolean>;
  }

 
}
