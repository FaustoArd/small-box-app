import { Component, OnInit } from '@angular/core';
import { SupplyDto } from 'src/app/models/SupplyDto';
import { FileDetails } from 'src/app/models/fileDetails';
import { PurchaseOrderDto } from 'src/app/models/purchaseOrderDto';
import { PurchaseOrderItemDto } from 'src/app/models/purchaseOrderItemDto';
import { SupplyItemDto } from 'src/app/models/supplyItemDto';
import { DepositControlService } from 'src/app/services/deposit-control.service';
import { DialogService } from 'src/app/services/dialog.service';
import { FileUploadService } from 'src/app/services/file-upload.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';

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
    private fileUploadService:FileUploadService,private snackBar:SnackBarService,private depositControlService:DepositControlService){}

  
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
    this.fileUploadService.sendPurchaseOrderPdfToBackEnd(this.file,2).subscribe({
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
    this.fileUploadService.sendSupplyPdfToBackEnd(this.file,2).subscribe({
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
  
}
