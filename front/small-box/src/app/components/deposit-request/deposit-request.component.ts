import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { DepositRequestDto } from 'src/app/models/depositRequestDto';
import { OrganizationDto } from 'src/app/models/organizationDto';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { DepositControlService } from 'src/app/services/deposit-control.service';
import { DepositRequestService } from 'src/app/services/deposit-request.service';
import { DialogService } from 'src/app/services/dialog.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { DialogTemplateComponent } from '../dialog/dialog-template/dialog-template.component';
import { SupplyItemRequestDto } from 'src/app/models/supplyItemRequestDto';
import { DepositControlRequestDto } from 'src/app/models/depositControlRequestDto';
import { QuantityControlRequest } from 'src/app/models/quantityControlRequest';
import { DestinationOrganization } from 'src/app/models/destinationOrganization';

@Component({
  selector: 'app-deposit-request',
  templateUrl: './deposit-request.component.html',
  styleUrls: ['./deposit-request.component.css']
})
export class DepositRequestComponent implements OnInit {

  constructor(private depositRequestService: DepositRequestService, private snackBar: SnackBarService
    , private organizationService: OrganizationService, private depositControlService: DepositControlService
    , private formBuilder: FormBuilder, private cookieService: CookieStorageService, private dialogService: DialogService
  ) { }

  ngOnInit(): void {
    this.getAllDepositRequests();
  }

  depositRequestFormBuilder = this.formBuilder.group({
    mainOrganizationId: [0, Validators.required],
  
  });

 

  get mainOrganizationId() {
    return this.depositRequestFormBuilder.controls.mainOrganizationId;
  }

  

  closeCreateRequestTemplate(): void {

    this.createRequestTemplateMatDialogRef.close();
  }
  //Update ticket Mat dialog template
  private createRequestTemplateMatDialogRef!: MatDialogRef<DialogTemplateComponent>

  openCreateRequestTemplate(template: TemplateRef<any>) {
    this.getAllOrganizationsByUser();
    this.createRequestTemplateMatDialogRef = this.dialogService.openCreateRequestCreation({
      template
    });

    this.createRequestTemplateMatDialogRef.afterClosed().subscribe({
      complete: () => {
        this.depositRequestFormBuilder.reset();
      }
    });
}

setDestinationOrganizationForm = this.formBuilder.group({
  destinationOrganizationId: [0, Validators.required]
});
get destinationOrganizationId() {
  return this.setDestinationOrganizationForm.controls.destinationOrganizationId;
}

closeSetDestinationOrganization(): void {
   this.setDestinationOrganizationTemplateMatDialogRef.close();
}

private setDestinationOrganizationTemplateMatDialogRef!:MatDialogRef<DialogTemplateComponent>
@ViewChild('setDestinationOrganizationTemplate') setDestinationOrganizationTemplate!: TemplateRef<any>;
openSetDestinationOrganizationTemplate( mainOrganizationId:number){
  this.getParentOrganizations(mainOrganizationId);
  const template = this.setDestinationOrganizationTemplate;
  this.setDestinationOrganizationTemplateMatDialogRef = this.dialogService.openCreateRequestCreation({
    template
  });
  this.setDestinationOrganizationTemplateMatDialogRef.afterClosed().subscribe;
}

destinationOrganizationRequestDto!:DepositRequestDto;
udpatedDepositRequestDto!:DepositRequestDto;
setDestinationOrganization(){
  if(this.setDestinationOrganizationForm.valid){
    this.destinationOrganizationRequestDto = new DepositRequestDto();
    this.destinationOrganizationRequestDto = Object.assign
    (this.destinationOrganizationRequestDto,this.setDestinationOrganizationForm.value);
    this.destinationOrganizationRequestDto.id = this.savedDepositRequestDto.id;
    this.depositRequestService.setDestinationOrganization(this.destinationOrganizationRequestDto).subscribe({
      next:(requestData)=>{
        this.udpatedDepositRequestDto = requestData;
      },
      error:(errorData)=>{

      },
      complete:()=>{
        this.closeSetDestinationOrganization();
         this.openSupplyItemRequestSelectionTemplate
          (this.udpatedDepositRequestDto.destinationOrganizationId, this.udpatedDepositRequestDto.mainOrganizationId);
      }
    })
  }
}

  private supplyItemRequestMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  @ViewChild('supplyItemRequestSelectionTemplate') supplyItemRequestSelectionTemplate!: TemplateRef<any>;
  openSupplyItemRequestSelectionTemplate(mainOrganization: number, organizationApplicantId: number): void {
    /**WARNING */
    //Read the info inside createRequest() method .
    this.getllSupplyItemsByMainOrganizationAndOrganizationApplicant(mainOrganization, organizationApplicantId);
    const template = this.supplyItemRequestSelectionTemplate;
    this.supplyItemRequestMatDialogRef = this.dialogService.openCreateRequestCreation({
      template
    });

  }

  depositRequestDto!: DepositRequestDto;
  savedDepositRequestDto!: DepositRequestDto;
  createRequest() {
    if (this.depositRequestFormBuilder.valid) {
      this.depositRequestDto = new DepositRequestDto();
      this.depositRequestDto = Object.assign(this.depositRequestDto, this.depositRequestFormBuilder.value);
      console.log("deposit request Dto!!!Main org: " + this.depositRequestDto.mainOrganizationId)
      console.log("deposit request Dto!!!destinationOrganization org: " + this.depositRequestDto.destinationOrganizationId)
      this.depositRequestService.createRequest(this.depositRequestDto).subscribe({
        next: (requestData) => {
          this.savedDepositRequestDto = requestData;
        },
        error: (errorData) => {
          this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
        },
        complete: () => {
          this.createRequestTemplateMatDialogRef.close();
          /**WARNING!**/
          /**This method recieves destination organization and main organization, but when 
           * we have to find all supplies by main organization and applicant organization,
           * destination organization becomes main organization and main organization
           * becomes organization applicant. Becouse:**/
          /*      DepositRequest(mainOrganization,destinationOrganization)
            mainOrganization here is the deposit request "from", and destination organization is deposit request "to" */
          /*    Supply(mainOrganization,applicantOrganization);
               mainOrganization here is the supply main organization 
                , and applicant organization is the supply organization applicant "from" */
this.openSetDestinationOrganizationTemplate( this.savedDepositRequestDto.mainOrganizationId)
         
        }
      });
    }

  }
  parentOrganizationDtos:OrganizationDto[]=[];
  getParentOrganizations(mainOrganizationId:number){
    this.organizationService.getParentOrganizationsByMainOrganization(mainOrganizationId).subscribe({
      next:(orgsData)=>{
        this.parentOrganizationDtos = orgsData;
        console.log("PARENMT" +this.parentOrganizationDtos);
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    });
  }

  depositRequestDtos: DepositRequestDto[] = [];
  getAllDepositRequests() {
    const userId = Number(this.cookieService.getCurrentUserId());
    this.depositRequestService.findAllDepositRequestsByOrganizationByUserId(userId).subscribe({
      next: (requestDatas) => {
        this.depositRequestDtos = requestDatas;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }

  organizationDtos: OrganizationDto[] = [];
  getAllOrganizationsByUser() {
    const userId = Number(this.cookieService.getCurrentUserId());
    this.organizationService.getAllOrganizationsByUser(userId).subscribe({
      next: (orgDatas) => {
        this.organizationDtos = orgDatas;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }
  // allOrganizationDtos: OrganizationDto[] = [];
  // getAllOrganizations() {
  //   this.organizationService.getAllOrganizations().subscribe({
  //     next: (orgsData) => {
  //       this.allOrganizationDtos = orgsData;
  //     },
  //     error: (errorData) => {
  //       this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
  //     }
  //   });
  // }
  supplyItemRequestDtos: SupplyItemRequestDto[] = [];
  getllSupplyItemsByMainOrganizationAndOrganizationApplicant(mainOrganizationId: number, organizationApplicantId: number) {
    this.depositControlService.findAllSupplyItemsByMainOrganizationAndOrganizationApplicant(mainOrganizationId, organizationApplicantId).subscribe({
      next: (supplyRequestDatas) => {
        this.supplyItemRequestDtos = supplyRequestDatas;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    })
  }
  itemQuantityForm = this.formBuilder.group({
    quantity: [0, Validators.required]
  });

  get quantity() {
    return this.itemQuantityForm.controls.quantity;
  }

  selectedSupplyItemRequestDtos: DepositControlRequestDto[] = [];

  quantityControlRequest!: QuantityControlRequest;
  selectedSupplyItemRequest(itemId: number): void {
    const code = this.getItemCode(itemId);
    const itemIndex  = this.selectedSupplyItemRequestDtos.findIndex(item => item.itemCode==code);
    if(itemIndex>-1){
      const repeatedItem = this.selectedSupplyItemRequestDtos[itemIndex];
      this.snackBar.openSnackBar("El item codigo: "+repeatedItem.itemCode + ", ya fue seleccionado...",'Cerrar',3000);
    }else{
      this.selectItemWithQuantity(itemId);
    }
   
  }
private getItemCode(itemId:number):string{
  const itemIndex = this.supplyItemRequestDtos.findIndex(item => item.itemId==itemId);
  const item = this.supplyItemRequestDtos[itemIndex];
  return item.code;
}

  private selectItemWithQuantity(itemId:number){
    if (this.itemQuantityForm.valid) {
      this.quantityControlRequest = new QuantityControlRequest();
      this.quantityControlRequest = Object.assign(this.quantityControlRequest, this.itemQuantityForm.value)

      this.supplyItemRequestDtos.forEach(item => {
        if (item.itemId == itemId) {
          this.selectedSupplyItemRequestDtos.push
            (new DepositControlRequestDto(item.code, item.measureUnit, item.itemDetail, this.quantityControlRequest.quantity));
        }

      });

      this.itemQuantityForm.reset();
    } else {
      this.snackBar.openSnackBar('Debe seleccionar una cantidad valida.', 'Cerrar', 3000);
    }
  }

  onDeleteSelectedSupplyItemRequest(itemId:number){
    this.selectedSupplyItemRequestDtos.forEach((item,index)=>{
      if(item.id==itemId){
        this.selectedSupplyItemRequestDtos.splice(index,1);
      }
    });
    const deletedItemIndex = this.selectedSupplyItemRequestDtos.findIndex(item => item.id==itemId);
    const deletedItemCode = this.selectedSupplyItemRequestDtos[deletedItemIndex].itemCode;
    this.snackBar.openSnackBar("Se elimino el item: " + deletedItemCode,'Cerrar',3000);
  }

  onCloseSavedDepositControlRequestTemplate(){
    this.savedDepositControlrequestMatDialogRef.close();
  }
  private savedDepositControlrequestMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  @ViewChild('savedDepositControlRequestTemplate') savedDepositControlRequestTemplate!: TemplateRef<any>;
  openSavedDepositControlRequestTemplate(): void {
    const template = this.savedDepositControlRequestTemplate;
    this.savedDepositControlrequestMatDialogRef = this.dialogService.openCreateRequestCreation({
      template
    });
    this.savedDepositControlrequestMatDialogRef.afterClosed().subscribe();
  }

  //  destinationOrganizationForm = this.formBuilder.group({
  //   destinationOrganizationId:[0,Validators.required]
  //  });
  //  get destinationOrganizationId(){
  //   return this.destinationOrganizationForm.controls.destinationOrganizationId;
  //  }

  toSendDepositRequestDto!: DepositRequestDto;
  //objDestinationOrganizationId!:DestinationOrganization;
  sendDepositRequest() {
    // this.objDestinationOrganizationId = new DestinationOrganization();
    //   this.objDestinationOrganizationId = Object.assign(this.objDestinationOrganizationId,this.destinationOrganizationForm.value);
    this.depositRequestService.sendRequest(this.toSendDepositRequestDto.id).subscribe({
      next: (codeData) => {
        this.snackBar.openSnackBar('Se envio con exito el pedido!. codigo:' + codeData, 'Cerrar', 3000);
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }, complete: () => {
        this.savedDepositControlrequestMatDialogRef.close();
        this.getAllDepositRequests();
      }
    })

  }


  saveControlRequestItems() {
    this.savedDepositRequestDto.depositControlRequestDtos = this.selectedSupplyItemRequestDtos;
    this.depositRequestService.saveItemsToRequest(this.savedDepositRequestDto).subscribe({
      next: (requestData) => {
        this.toSendDepositRequestDto = requestData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      },
      complete: () => {
        this.supplyItemRequestMatDialogRef.close();
        this.openSavedDepositControlRequestTemplate();
      }
    });
  }


  onCloseDepositControlRequestsTemplate(){
    this.depositControlrequestsMatDialogRef.close();
  }
  private depositControlrequestsMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  
  openDepositControlRequestsTemplate(template:TemplateRef<any>,depositRequestId:number): void {
    this.getAllControlRequestsByRequestId(depositRequestId);
    this.depositControlrequestsMatDialogRef = this.dialogService.openSupplyCorrectionNoteCreation({
      template
    });
    this.depositControlrequestsMatDialogRef.afterClosed().subscribe();
  }

  depositControlRequestDtos:DepositControlRequestDto[]=[];
  selectedDepositRequestDto!:DepositRequestDto;
  getAllControlRequestsByRequestId(depositRequestId:number){
    this.depositRequestService.findAllControlRequestsByRequest(depositRequestId).subscribe({
      next:(controlRequestDatas)=>{
        this.depositControlRequestDtos = controlRequestDatas;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      },
      complete:()=>{
        this.selectedDepositRequestDto = this.getDepositRequestData(depositRequestId);
      }
    });
  }

  getDepositRequestData(depositRequestId:number):DepositRequestDto{
    const requestInddex = this.depositRequestDtos.findIndex(request => request.id==depositRequestId);
    if(requestInddex>-1){
    return this.depositRequestDtos[requestInddex];
    }
    return new DepositRequestDto();
  }
}
