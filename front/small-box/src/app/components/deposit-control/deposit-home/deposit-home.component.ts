import { Component, OnInit } from '@angular/core';
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

@Component({
  selector: 'app-deposit-home',
  templateUrl: './deposit-home.component.html',
  styleUrls: ['./deposit-home.component.css']
})


export class DepositHomeComponent implements OnInit {
  purchaseOrderDto!:PurchaseOrderDto
  purchaseOrderItemDtos:Array<PurchaseOrderItemDto>=[];
  supplyItemDtos:Array<SupplyItemDto>=[];
  supplyDto!:SupplyDto;

constructor(private dialogService:DialogService,
    private fileUploadService:FileUploadService,private snackBar:SnackBarService
    ,private depositControlService:DepositControlService,private cookieService:CookieStorageService){}

  
ngOnInit(): void {
   
}

  file!:File;
  fileDetails!: FileDetails;
  fileUris:Array<string> = [];
  //Upload file last
  selectFile(event:any){
    console.log("select file: " + event.target.files.item(0))
    this.file = event.target.files.item(0);
  }

  uploadPurchaseOrderFile(){
    const orgId = this.cookieService.getUserMainOrganizationId();
    console.log(orgId);
    this.fileUploadService.sendPurchaseOrderPdfToBackEnd(this.file,Number(orgId)).subscribe({
      next:(purchaseOrderData) =>{
       this.purchaseOrderDto = purchaseOrderData;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      },
      complete:()=>{
        this.purchaseOrderItemDtos = this.purchaseOrderDto.items;
      }
     
    });
   
  }
  uploadSupplyFile(){
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    this.fileUploadService.sendSupplyPdfToBackEnd(this.file,orgId).subscribe({
      next:(supplyData) =>{
       this.supplyDto = supplyData;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      },
      complete:()=>{
     this.supplyItemDtos = this.supplyDto.supplyItems; 
      }
    });
   
  }

  private matDialogRef!:MatDialogRef<DialogTemplateComponent>;  
  
purchaseOrderDtos:PurchaseOrderDto[]=[];
  openDialogPurchaseOrderList(template:any){
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    this.getAllPurchaseOrders(orgId);
    this.matDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.matDialogRef.afterClosed().subscribe();
  }

  getAllPurchaseOrders(organizationId:number){
    this.depositControlService.findAllPurchaseOrdersByOrganization(organizationId).subscribe({
      next:(orgsData)=>{
        this.purchaseOrderDtos = orgsData;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    })
  }
}
