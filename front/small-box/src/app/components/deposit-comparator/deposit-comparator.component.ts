import { Component, TemplateRef, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { DepositControlDto } from 'src/app/models/depositControlDto';
import { DepositItemComparatorDto } from 'src/app/models/depositItemComparatorDto';
import { ExcelItemDto } from 'src/app/models/excelItemDto';
import { FileDetails } from 'src/app/models/fileDetails';
import { PurchaseOrderItemDto } from 'src/app/models/purchaseOrderItemDto';
import { ConfirmDialogService } from 'src/app/services/confirm-dialog.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { DepositControlService } from 'src/app/services/deposit-control.service';
import { DialogService } from 'src/app/services/dialog.service';
import { FileUploadService } from 'src/app/services/file-upload.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { DialogTemplateComponent } from '../dialog/dialog-template/dialog-template.component';

@Component({
  selector: 'app-deposit-comparator',
  templateUrl: './deposit-comparator.component.html',
  styleUrls: ['./deposit-comparator.component.css']
})
export class DepositComparatorComponent {

  constructor(private dialogService: DialogService,
    private fileUploadService: FileUploadService, private snackBar: SnackBarService
    , private depositControlService: DepositControlService, private cookieService: CookieStorageService,
    private confirmDialogService: ConfirmDialogService, private formBuilder: FormBuilder
    , private router: Router
  ) { }

  file!: File;
  fileDetails!: FileDetails;
  fileUris: Array<string> = [];
  selectFile(event: any) {
    console.log("select file: " + event.target.files.item(0))
    this.file = event.target.files.item(0);
  }

  depositComparators!: DepositItemComparatorDto[];
  uploadDepositExcelFile(): void {
    console.log("Hola 1")
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    this.fileUploadService.sendDepositItemExcelFileToBackEnd(this.file, orgId).subscribe({
      next: (comparatorDatas) => {
        this.depositComparators = comparatorDatas;

      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      },
      complete: () => {
        this.depositComparators.map(m => console.log(m.excelItemDto))
      }
    });
  }


  selectedExcelItemDtos: ExcelItemDto[] = [];
  selectedExcelItemsDtoShows: PurchaseOrderItemDto[] = [];
  selectedOrderItem!: PurchaseOrderItemDto;
  selectPurchaseOrderItem(excelItemId: number, purchaseOrderId: number): void {
    const exelItemQuantity = this.depositComparators.filter(f => f.excelItemDto.excelItemId == excelItemId).map(m => m.excelItemDto.itemQuantity);
    this.selectedExcelItemDtos.push(new ExcelItemDto(purchaseOrderId, Number(exelItemQuantity)));
    
      this.selectedOrderItem = new PurchaseOrderItemDto();
      this.depositComparators
        .filter(f => f.excelItemDto.excelItemId == excelItemId)
        .map(m => m.purchaseOrderItemCandidateDtos
          .map(m => {
            if(m.orderId==purchaseOrderId){
            this.selectedOrderItem.code = m.code;
            this.selectedOrderItem.itemDetail = m.itemDetail;
            this.selectedOrderItem.measureUnit = m.measureUnit;
            this.selectedOrderItem.quantity = Number(exelItemQuantity);
            }
          }));
          this.selectedExcelItemsDtoShows.push(this.selectedOrderItem);
          this.depositComparators.map(m => m).map((item, index) => {
            if (item.excelItemDto.excelItemId == excelItemId) {
              this.depositComparators.splice(index, 1);
            }
          });
  }

depositControlDtos:DepositControlDto[]=[];
  saveExcelItemsToDeposit(){
    const orgId =Number(this.cookieService.getUserMainOrganizationId()) ;
    const depositId =Number(this.cookieService.getCurrentDepositSelectedId()) ;
    this.depositControlService.saveExcelItemsToDeposit(orgId,depositId,this.selectedExcelItemDtos).subscribe({
      next:(depositControlDatas)=>{
        this.depositControlDtos = depositControlDatas;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      },complete:()=>{
        this.openDialogPurchaseOrderItemsList();
      }
    });
  }

  onCloseDepositControlistTemplate() {
    this.depositControlListMatDialogRef.close();
  }
  @ViewChild('depositControlTemplate') depositControlTemplateTemplateRef !: TemplateRef<any>
  private depositControlListMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  openDialogPurchaseOrderItemsList() {
   const template = this.depositControlTemplateTemplateRef;
    this.depositControlListMatDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.depositControlListMatDialogRef.afterClosed().subscribe();
  }

}
