import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { SupplyDto } from 'src/app/models/SupplyDto';
import { FileDetails } from 'src/app/models/fileDetails';
import { PurchaseOrderDto } from 'src/app/models/purchaseOrderDto';
import { PurchaseOrderItemDto } from 'src/app/models/purchaseOrderItemDto';
import { SupplyItemDto } from 'src/app/models/supplyItemDto';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { DepositControlService } from 'src/app/services/deposit-control.service';
import { DialogService } from 'src/app/services/dialog.service';
import { FileUploadService } from 'src/app/services/file-upload.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { DialogTemplateComponent } from '../../dialog/dialog-template/dialog-template.component';
import { MatDialogRef } from '@angular/material/dialog';

import { PurchaseOrderToDepositReportDto } from 'src/app/models/purchaseOrderToDepositReportDto';
import { DepositControlDto } from 'src/app/models/depositControlDto';
import { SupplyCorrectionNote } from 'src/app/models/supplyCorrectionNoteDto';
import { ConfirmDialogService } from 'src/app/services/confirm-dialog.service';

@Component({
  selector: 'app-deposit-home',
  templateUrl: './deposit-home.component.html',
  styleUrls: ['./deposit-home.component.css']
})


export class DepositHomeComponent implements OnInit {
  purchaseOrderDto!: PurchaseOrderDto
  purchaseOrderItemDtos: Array<PurchaseOrderItemDto> = [];
  supplyItemDtos: Array<SupplyItemDto> = [];
  supplyDto!: SupplyDto;

  constructor(private dialogService: DialogService,
    private fileUploadService: FileUploadService, private snackBar: SnackBarService
    , private depositControlService: DepositControlService, private cookieService: CookieStorageService,
    private confirmDialogService: ConfirmDialogService
  ) { }


  ngOnInit(): void {

  }

  file!: File;
  fileDetails!: FileDetails;
  fileUris: Array<string> = [];
  //Upload file last
  selectFile(event: any) {
    console.log("select file: " + event.target.files.item(0))
    this.file = event.target.files.item(0);
  }

  uploadPurchaseOrderFile() {
    const orgId = this.cookieService.getUserMainOrganizationId();
    console.log(orgId);
    this.fileUploadService.sendPurchaseOrderPdfToBackEnd(this.file, Number(orgId)).subscribe({
      next: (purchaseOrderData) => {
        this.purchaseOrderDto = purchaseOrderData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      },
      complete: () => {
        this.openPurchaseOrderTableTemplate(this.purchaseOrderDto.items);
      }

    });

  }



  onClosePurchaseOrderTableTemplate(): void {
    this.purchaseOrderTableMatDialogRef.close();

  }

  private purchaseOrderTableMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  @ViewChild('purchaseOrderTableTemplate') purchaseOrderTableTemplate !: TemplateRef<any>
  openPurchaseOrderTableTemplate(purchaseOrderItems: PurchaseOrderItemDto[]): void {
    this.purchaseOrderItemDtos = purchaseOrderItems;
    const template = this.purchaseOrderTableTemplate;
    this.purchaseOrderTableMatDialogRef = this.dialogService.openSupplyCorrectionNoteCreation({
      template
    });
    this.purchaseOrderTableMatDialogRef.afterClosed().subscribe();

  }
  uploadSupplyFile() {
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    this.fileUploadService.sendSupplyPdfToBackEnd(this.file, orgId).subscribe({
      next: (supplyData) => {
        this.supplyDto = supplyData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      },
      complete: () => {
        this.openSupplyTableTemplate(this.supplyDto.supplyItems);
      }
    });

  }
  onCloseSupplyTableTemplate() {
    this.supplyTableTemplateRef.close();
  }

  private supplyTableTemplateRef!: MatDialogRef<DialogTemplateComponent>;

  @ViewChild('supplyTableTemplate') supplyTableTemplate !: TemplateRef<any>
  openSupplyTableTemplate(supplyItems: SupplyItemDto[]): void {
    this.supplyItemDtos = supplyItems;
    const template = this.supplyTableTemplate;
    this.supplyTableTemplateRef = this.dialogService.openSupplyCorrectionNoteCreation({
      template
    });
    this.supplyTableTemplateRef.afterClosed().subscribe();

  }

  onClosepurchaseOrderListTemplate() {
    this.purchaseOrderTemplateRef.close();
  }

  private purchaseOrderTemplateRef!: MatDialogRef<DialogTemplateComponent>;
  purchaseOrderDtos: PurchaseOrderDto[] = [];
  openDialogPurchaseOrderList(template: any) {
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    this.getAllPurchaseOrders(orgId);
    this.purchaseOrderTemplateRef = this.dialogService.openDialogCreation({
      template
    });
    this.purchaseOrderTemplateRef.afterClosed().subscribe();
  }

  private confirmData!: boolean;
  confirmDepositLoad(purchaseOrderId: number, loadedToDeposit: boolean, orderNumber: number): void {
    if (loadedToDeposit) {
      var confirmText = "El suministro n°: " + orderNumber + " ya fue cargado al deposito, desea cargarlo nuevamente?";
      this.confirmDialogService.confirmDialog(confirmText).subscribe({
        next: (confirmData) => {
          this.confirmData = confirmData;
          if (this.confirmData) {
            this.loadPurchaseOrderToDepositControl(purchaseOrderId, orderNumber);
          }
        }
      });
    } else {
      this.loadPurchaseOrderToDepositControl(purchaseOrderId, orderNumber);
    }
  }

  depositReport: PurchaseOrderToDepositReportDto[] = [];
  loadPurchaseOrderToDepositControl(purchaseOrderId: number, orderNumber: number) {
    this.depositControlService.loadPurchaseOrderToDeposit(purchaseOrderId).subscribe({
      next: (depositReportData) => {
        this.depositReport = depositReportData;
        console.log(this.depositReport);
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }, complete: () => {
        this.openDialogOrderToDepositReport();
      }
    });
  }

  onCloseOrderToDepositTemplate() {
    this.orderToDepositMatDialogRef.close();
  }

  private orderToDepositMatDialogRef!: MatDialogRef<DialogTemplateComponent>;

  @ViewChild('purchaseOrderToDepositTemplate') purchaseOrderReportTemplateRef !: TemplateRef<any>
  openDialogOrderToDepositReport(): void {
    const template = this.purchaseOrderReportTemplateRef;
    this.orderToDepositMatDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.orderToDepositMatDialogRef.afterClosed().subscribe();
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    this.getAllPurchaseOrders(orgId);

  }

  onClosepurchaseOrderItemListTemplate() {
    this.purchaseOrderItemListMatDialogRef.close();
  }
  private purchaseOrderItemListMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  openDialogPurchaseOrderItemsList(template: any, purchaseOrderId: number) {
    this.getPurchaseOrderItems(purchaseOrderId);
    this.purchaseOrderItemListMatDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.purchaseOrderItemListMatDialogRef.afterClosed().subscribe();
  }

  getAllPurchaseOrders(organizationId: number) {
    this.depositControlService.findAllPurchaseOrdersByOrganization(organizationId).subscribe({
      next: (orgsData) => {
        this.purchaseOrderDtos = orgsData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    })
  }

  purchaseOrderItemDtosListShow: PurchaseOrderItemDto[] = [];
  getPurchaseOrderItems(purchaseOrderId: number) {
    this.depositControlService.findPuchaseOrderItems(purchaseOrderId).subscribe({
      next: (orderItemData) => {
        this.purchaseOrderItemDtosListShow = orderItemData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    })
  }

  onCloseSupplyListTemplate() {
    this.supplyListMatDialogRef.close();
  }
  private supplyListMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  openDialogSupplyList(template: any) {
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    this.getAllSupplies(orgId);
    this.supplyListMatDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.supplyListMatDialogRef.afterClosed().subscribe();

  }
  suppliesDto: SupplyDto[] = [];
  getAllSupplies(organizationId: number) {
    this.depositControlService.findAllSuppliesByOrganization(organizationId).subscribe({
      next: (suppliesData) => {
        this.suppliesDto = suppliesData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }
  onCloseSupplyItemListTemplate() {
    this.supplyItemListMatDialogRef.close();
  }

  private supplyItemListMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  openDialogSuppliesItemList(template: any, supplyId: number) {
    this.getSupplyItems(supplyId);
    this.supplyItemListMatDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.supplyItemListMatDialogRef.afterClosed().subscribe();
  }

  supplyItemsDtoListShow: SupplyItemDto[] = [];
  getSupplyItems(supplyId: number) {
    this.depositControlService.findSupplyItems(supplyId).subscribe({
      next: (itemsData) => {
        this.supplyItemsDtoListShow = itemsData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }

  depositControlDtos: DepositControlDto[] = [];

  getDepositControlsByOrganization() {
    const organizationId = Number(this.cookieService.getUserMainOrganizationId());
    this.depositControlService.findDepositControlsByOrganization(organizationId).subscribe({
      next: (depositData) => {
        this.depositControlDtos = depositData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    })
  }

  onCloseDepositControlListTemplate() {
    this.depositControlListMatDialogRef.close();
  }
  private depositControlListMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  openDepositControlListTemplate(template: any): void {
    this.getDepositControlsByOrganization();
    this.depositControlListMatDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.depositControlListMatDialogRef.afterClosed().subscribe();
  }

  onCloseSupplyCorrectionNoteTemplate() {
    this.supplyCorrectionNoteMatDialogRef.close();
  }
  private supplyCorrectionNoteMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  now!: Date;
  openSupplyCorrectionNoteDialog(template: any, supplyId: number) {
    this.now = new Date();
    this.now.getTime();
    this.getSupplyCorrectionNote(supplyId);
    this.supplyCorrectionNoteMatDialogRef = this.dialogService.openSupplyCorrectionNoteCreation({
      template
    });
    this.supplyCorrectionNoteMatDialogRef.afterClosed().subscribe();
  }

  supplyCorrectionNote!: SupplyCorrectionNote;
  getSupplyCorrectionNote(supplyId: number) {
    this.depositControlService.createSupplyCorrectionNote(supplyId).subscribe({
      next: (noteData) => {
        this.supplyCorrectionNote = noteData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }



}

