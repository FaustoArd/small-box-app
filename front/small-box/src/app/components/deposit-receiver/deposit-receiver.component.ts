import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { DepositControlReceiverDto } from 'src/app/models/depositControlReceiverDto';
import { DepositReceiverDto } from 'src/app/models/depositReceiverDto';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { DepositControlService } from 'src/app/services/deposit-control.service';
import { DepositReceiverService } from 'src/app/services/deposit-receiver.service';
import { DialogService } from 'src/app/services/dialog.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { DialogTemplateComponent } from '../dialog/dialog-template/dialog-template.component';

@Component({
  selector: 'app-deposit-receiver',
  templateUrl: './deposit-receiver.component.html',
  styleUrls: ['./deposit-receiver.component.css']
})
export class DepositReceiverComponent implements OnInit {

  constructor(private depositReceiverService: DepositReceiverService, private snackBar: SnackBarService
    , private organizationService: OrganizationService, private depositControlService: DepositControlService
    , private formBuilder: FormBuilder, private cookieService: CookieStorageService, private dialogService: DialogService
  ) { }

  ngOnInit(): void {
    
      this.getAllReceiversByOrganization();
  }

  depositReceiverDtos:DepositReceiverDto[]=[];
  getAllReceiversByOrganization(){
    const organizationId = Number(this.cookieService.getUserMainOrganizationId());
    this.depositReceiverService.findAllReceiversByOrganization(organizationId).subscribe({
      next:(receiverDatas)=>{
        this.depositReceiverDtos = receiverDatas;
        console.log(this.depositReceiverDtos)
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    });
  }
  messageReaded!:boolean;
  markAsReaded(depositReceiverId:number){
    this.depositReceiverService.markAsReaded(depositReceiverId).subscribe({
      next:(readedData)=>{
        this.messageReaded = readedData;
        if(this.messageReaded){
          this.snackBar.openSnackBar("Listo!",'Cerrar',3000);
        }
       
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      },
      complete:()=>{
        this.getAllReceiversByOrganization();
      }
    });
  }
  onCloseDepositCreationTemplate() {
    this.deopsitControlReceivceTemplate.close();

  }

  private deopsitControlReceivceTemplate!: MatDialogRef<DialogTemplateComponent>;
  openDialogDepositCreation(template: any,depositReceiverId:number) {
    this.findAllDepositControlReceiversByReceiver(depositReceiverId);
    this.getReceiverData(depositReceiverId);
    this.deopsitControlReceivceTemplate = this.dialogService.openSupplyCorrectionNoteCreation({
      template
    });
    this.deopsitControlReceivceTemplate.afterClosed().subscribe();
 }
 depositReceiverDataShow!:DepositReceiverDto;
 getReceiverData(depositReceiverId:number){
  this.depositReceiverDtos.forEach(item =>{
    if(item.id==depositReceiverId){
      this.depositReceiverDataShow = item;
    }
  });
 }
  depositControlReceiverDtos:DepositControlReceiverDto[]=[];
  findAllDepositControlReceiversByReceiver(depositReceiverId:number){
    this.depositReceiverService.findAllControlReceiversByReceiver(depositReceiverId).subscribe({
      next:(controlReceiverDatas)=>{
        this.depositControlReceiverDtos = controlReceiverDatas;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    });
  }
}
