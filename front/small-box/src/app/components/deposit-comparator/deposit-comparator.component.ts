import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
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
import { PurchaseOrderItemCandidateDto } from 'src/app/models/purchaseOrderItemCandidateDto';

@Component({
  selector: 'app-deposit-comparator',
  templateUrl: './deposit-comparator.component.html',
  styleUrls: ['./deposit-comparator.component.css']
})
export class DepositComparatorComponent implements OnInit {

  constructor(private dialogService: DialogService,
    private fileUploadService: FileUploadService, private snackBar: SnackBarService
    , private depositControlService: DepositControlService, private cookieService: CookieStorageService,
    private confirmDialogService: ConfirmDialogService, private formBuilder: FormBuilder
    , private router: Router
  ) { }

ngOnInit(): void {
 
}

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
  selectedExcelItemsDtoShows: ExcelItemDto[] = [];
  repeatedExcelItem!: ExcelItemDto;
  itemIndex!:number;
  selectPurchaseOrderItem(itemPO: PurchaseOrderItemCandidateDto): void {
    const excelItemQuantity = this.depositComparators
      .filter(f => f.excelItemDto.excelItemId == itemPO.excelItemDtoId)
      .map(m => m.excelItemDto.itemQuantity);
      console.log("EXcel quantity"+excelItemQuantity)
      const index = this.selectedExcelItemDtos.findIndex(item => item.itemCode==itemPO.code);
      console.log("EXcel index:"+index)
      if(index>-1){
        this.selectedExcelItemDtos[index].itemQuantity =this.selectedExcelItemDtos[index].itemQuantity + Number(excelItemQuantity);
      }else{
        this.selectedExcelItemDtos.push(
          new ExcelItemDto(itemPO.excelItemDtoId,itemPO.orderItemId,itemPO.code,Number(excelItemQuantity),itemPO.measureUnit,itemPO.itemDetail));
      }
      this.depositComparators.map(m => m).map((item, index) => {
        if (item.excelItemDto.excelItemId == itemPO.excelItemDtoId) {
          this.depositComparators.splice(index, 1);
        }
      });
  }
  
  deleteSelectedItem(excelItemId: number): void {
    this.selectedExcelItemDtos.forEach((item, index) => {
      if (item.excelItemId === excelItemId) {
        this.selectedExcelItemDtos.splice(index, 1);
      }
    });
  }

  depositControlDtos: DepositControlDto[] = [];
  saveExcelItemsToDeposit() {
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    const depositId = Number(this.cookieService.getCurrentDepositSelectedId());
    this.depositControlService.saveExcelItemsToDeposit(orgId, depositId, this.selectedExcelItemDtos).subscribe({
      next: (depositControlDatas) => {
        this.depositControlDtos = depositControlDatas;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }, complete: () => {
        this.openDialogPurchaseOrderItemsList();
      }
    });
  }


  onCloseDepositControlistTemplate() {
    this.depositControlListMatDialogRef.close();
    this.reloadPage();
  }
async reloadPage(){
  const currentUrl = this.router.url;
  await this.router.navigate(['home']);
  await this.router.navigate([currentUrl])
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
