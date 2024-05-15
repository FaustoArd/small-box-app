import { Component, OnInit, TemplateRef } from '@angular/core';
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
import { ConfirmDialogService } from 'src/app/services/confirm-dialog.service';
import { RequestComparationNoteDto } from 'src/app/models/requestComparationNoteDto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-deposit-receiver',
  templateUrl: './deposit-receiver.component.html',
  styleUrls: ['./deposit-receiver.component.css']
})
export class DepositReceiverComponent implements OnInit {

  constructor(private depositReceiverService: DepositReceiverService, private snackBar: SnackBarService
    , private organizationService: OrganizationService, private depositControlService: DepositControlService
    , private formBuilder: FormBuilder, private cookieService: CookieStorageService, private dialogService: DialogService,
    private confirmDialogService: ConfirmDialogService, private router: Router
  ) { }

  ngOnInit(): void {

    this.getAllReceiversByOrganization();
   this.getCurrentDepositIdIfEmpty();
   
  }
  getCurrentDepositIdIfEmpty(){
    if(Number(this.cookieService.getCurrentDepositSelectedId())==0){
      this.getCurrentDeposit();
    }
  }

  depositReceiverDtos: DepositReceiverDto[] = [];
  getAllReceiversByOrganization() {
    const organizationId = Number(this.cookieService.getUserMainOrganizationId());
    this.depositReceiverService.findAllReceiversByOrganization(organizationId).subscribe({
      next: (receiverDatas) => {
        this.depositReceiverDtos = receiverDatas;
        console.log(this.depositReceiverDtos)
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }
  messageReaded!: boolean;
  markAsReaded(depositReceiverId: number) {
    this.depositReceiverService.markAsReaded(depositReceiverId).subscribe({
      next: (readedData) => {
        this.messageReaded = readedData;
        if (this.messageReaded) {
          this.snackBar.openSnackBar("Listo!", 'Cerrar', 3000);
        }

      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      },
      complete: () => {
        this.reloadPage();

      }
    });
  }
  onCloseDepositCreationTemplate() {
    this.depositControlReceivceTemplate.close();

  }

  private depositControlReceivceTemplate!: MatDialogRef<DialogTemplateComponent>;
  openDialogDepositCreation(template: any, depositReceiverId: number) {
    this.findAllDepositControlReceiversByReceiver(depositReceiverId);
    this.getReceiverData(depositReceiverId);
    this.depositControlReceivceTemplate = this.dialogService.openSupplyCorrectionNoteCreation({
      template
    });
    this.depositControlReceivceTemplate.afterClosed().subscribe();
  }
  depositReceiverDataShow!: DepositReceiverDto;
  getReceiverData(depositReceiverId: number) {
    this.depositReceiverDtos.forEach(item => {
      if (item.id == depositReceiverId) {
        this.depositReceiverDataShow = item;
      }
    });
  }
  depositControlReceiverDtos: DepositControlReceiverDto[] = [];
  findAllDepositControlReceiversByReceiver(depositReceiverId: number) {
    this.depositReceiverService.findAllControlReceiversByReceiver(depositReceiverId).subscribe({
      next: (controlReceiverDatas) => {
        this.depositControlReceiverDtos = controlReceiverDatas;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }
  receiverConfirmData!: boolean;
  confirmDeleteDepositReceiver(depositReceiverId: number) {
    var confirmText = "Desea eliminar el pedido de deposito?";
    this.confirmDialogService.confirmDialog(confirmText).subscribe({
      next: (confirmData) => {
        this.receiverConfirmData = confirmData;
        if (this.receiverConfirmData) {
          this.deleteDepositReceiverById(depositReceiverId);
         // this.reloadPage();
        } else {
          this.snackBar.openSnackBar('Se cancelo la operacion.', 'Cerrar', 3000);
        }
      }, error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      },
      complete:()=>{
        this.reloadPage();
      }
    });
  }
  deletedCodeData!: string;
  deleteDepositReceiverById(depositReceiverId: number) {
    this.depositReceiverService.deleteDepositReceiverById(depositReceiverId).subscribe({
      next: (deleteCodeData) => {
        this.deletedCodeData = deleteCodeData;
      },error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }, complete: () => {
       this.snackBar.openSnackBar('Se elimino el pedido: ' + this.getDeletedReceiverCode(depositReceiverId), 'Cerrar', 3000);
      }
    });
  }
  getDeletedReceiverCode(depositReceiverId:number):string{
    const deletedReceiverIndex = this.depositReceiverDtos.findIndex(reciever => reciever.id==depositReceiverId);
    return this.depositReceiverDtos[deletedReceiverIndex].depositRequestCode;
  }

  async reloadPage() {
    const currentUrl = this.router.url;
    await this.router.navigate(['home']);
    await this.router.navigate([currentUrl])
  }



  onCloseRequestComparatorNoteTemplate() {
    this.requestComparatorNoteTemplateRef.close();
  }

  requestComparatorNoteTemplateRef!: MatDialogRef<DialogTemplateComponent>;
  openRequestComparatorNoteTemplate(template: TemplateRef<any>, depositReceiverId: number) {
    this.getItemsComparator(depositReceiverId);
    this.requestComparatorNoteTemplateRef = this.dialogService.openCustomDialogCreation({
      template
    },'85%','95%',true,true);
    this.requestComparatorNoteTemplateRef.afterClosed().subscribe();

  }

  comparatorDto!: RequestComparationNoteDto;
  getItemsComparator(depositReceiverId: number) {
    const depositId = Number(this.cookieService.getCurrentDepositSelectedId());
    console.log("DEPOSIT ID:" + depositId)
    this.depositReceiverService.getItemsComparationNote(depositReceiverId, depositId).subscribe({
      next: (comparatorData) => {
        this.comparatorDto = comparatorData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    })
  }

   getCurrentDeposit() {

     const userId = Number(this.cookieService.getCurrentUserId());
     const organizationId = Number(this.cookieService.getUserMainOrganizationId());
     this.depositControlService.getCurrentDeposit(userId, organizationId).subscribe({
       next: (depositIdData) => {
         if (depositIdData.id === null || undefined || depositIdData.id === 0) {
           this.snackBar.openSnackBar('No hay ningun deposito asignado', 'Cerrar', 3000);
         } else {
           this.cookieService.setCurrentDepositSelectedId(JSON.stringify(depositIdData.id));

         }
       },
    error: (errorData) => {
         this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
       }
     });
   }

}
