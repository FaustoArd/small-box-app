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
    organizationId: [0, Validators.required]
  });

  get organizationId() {
    return this.depositRequestFormBuilder.controls.organizationId;
  }

  closeCreateRequestTemplate(): void {

    this.matDialogRef.close();
  }
//Update ticket Mat dialog template
private matDialogRef!: MatDialogRef<DialogTemplateComponent>


opencreateRequestTemplate( template: TemplateRef<any>) {
 this.getAllOrganizationsByUser();
  this.matDialogRef = this.dialogService.openSupplyCorrectionNoteCreation({
    template
  })

  this.matDialogRef.afterClosed().subscribe();
  this.depositRequestFormBuilder.reset();
 }

 private supplyItemRequestMatDialogRef!:MatDialogRef<DialogTemplateComponent>;
 @ViewChild('supplyItemRequestSelectionTemplate')supplyItemRequestSelectionTemplate!:TemplateRef<any>;
 openSupplyItemRequestSelectionTemplate(organizationApplicantId:number):void{
  this.getllSupplyItemsByOrganizationApplicant(organizationApplicantId);
  const template = this.supplyItemRequestSelectionTemplate;
  this.supplyItemRequestMatDialogRef = this.dialogService.openSupplyCorrectionNoteCreation({
    template
  });
 }

  depositRequestDto!: DepositRequestDto;
  savedDepositRequestDto!: DepositRequestDto;
  createRequest() {
    if (this.depositRequestFormBuilder.valid) {
      this.depositRequestDto = new DepositRequestDto();
      this.depositRequestDto = Object.assign(this.depositRequestDto, this.depositRequestFormBuilder.value);
      this.depositRequestService.createRequest(this.depositRequestDto).subscribe({
        next: (requestData) => {
          this.savedDepositRequestDto = requestData;
        },
        error: (errorData) => {
          this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
        },
        complete:()=>{
          this.openSupplyItemRequestSelectionTemplate(this.savedDepositRequestDto.organizationId)
        }
      });
    }

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

  organizationDtos:OrganizationDto[]=[];
  getAllOrganizationsByUser(){
    const userId = Number(this.cookieService.getCurrentUserId());
    this.organizationService.getAllOrganizationsByUser(userId).subscribe({
      next:(orgDatas)=>{
        this.organizationDtos = orgDatas; 
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    });
  }
  allOrganizationDtos:OrganizationDto[]=[];
  getAllOrganizations(){
    this.organizationService.getAllOrganizations().subscribe({
      next:(orgsData)=>{
        this.allOrganizationDtos = orgsData;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    });
  }
  supplyItemRequestDtos:SupplyItemRequestDto[]=[];
  getllSupplyItemsByOrganizationApplicant(organizationApplicantId:number){
    this.depositControlService.findAllSupplyItemsByOrganizationApplicant(organizationApplicantId).subscribe({
      next:(supplyRequestDatas)=>{
        this.supplyItemRequestDtos = supplyRequestDatas;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    })
  }
itemQuantityForm = this.formBuilder.group({
  quantity:[[],Validators.required]
});

get quantity(){
  return this.itemQuantityForm.controls.quantity;
}

selectedSupplyItemRequestDtos:DepositControlRequestDto[]=[];

quantityControlRequest!:QuantityControlRequest;
  selectedSupplyItemRequest(itemId:number):void{
    
    if(this.itemQuantityForm.valid){
      this.quantityControlRequest = new QuantityControlRequest();
      this.quantityControlRequest = Object.assign(this.quantityControlRequest,this.itemQuantityForm.value)

    this.supplyItemRequestDtos.forEach(item=>{
      if(item.itemId==itemId){
        this.selectedSupplyItemRequestDtos.push
        (new DepositControlRequestDto(item.code,item.measureUnit,item.itemDetail,this.quantityControlRequest.quantity));
      }

    });
    
    this.itemQuantityForm.reset();
  }else{
    this.snackBar.openSnackBar('Debe seleccionar una cantidad valida.','Cerrar',3000);
  }
  }

  private savedDepositControlrequestMatDialogRef!:MatDialogRef<DialogTemplateComponent>;
 @ViewChild('savedDepositControlRequestTemplate')savedDepositControlRequestTemplate!:TemplateRef<any>;
 openSavedDepositControlRequestTemplate():void{
  this.getAllOrganizations();
   const template = this.savedDepositControlRequestTemplate;
  this.savedDepositControlrequestMatDialogRef = this.dialogService.openSupplyCorrectionNoteCreation({
    template
  });
  this.savedDepositControlrequestMatDialogRef.afterClosed().subscribe();
 }

 destinationOrganizationForm = this.formBuilder.group({
  destinationOrganizationId:[0,Validators.required]
 });
 get destinationOrganizationId(){
  return this.destinationOrganizationForm.controls.destinationOrganizationId;
 }

 toSendDepositRequestDto!:DepositRequestDto;
 objDestinationOrganizationId!:DestinationOrganization;
 sendDepositRequest(){
  if(this.destinationOrganizationForm.valid){
    this.objDestinationOrganizationId = new DestinationOrganization();
    this.objDestinationOrganizationId = Object.assign(this.objDestinationOrganizationId,this.destinationOrganizationForm.value);
    this.depositRequestService.sendRequest(this.toSendDepositRequestDto.id,this.objDestinationOrganizationId.destinationOrganizationId).subscribe({
      next:(codeData)=>{
        this.snackBar.openSnackBar('Se envio con exito pedido. numero envio: ' + codeData,'Cerrar',3000);
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      },complete:()=>{
        this.savedDepositControlrequestMatDialogRef.close();
      }
    })
  }
 }

  
  saveControlRequestItems(){
    this.savedDepositRequestDto.depositControlRequestDtos = this.selectedSupplyItemRequestDtos;
    this.depositRequestService.saveItemsToRequest(this.savedDepositRequestDto).subscribe({
      next:(requestData)=>{
        this.toSendDepositRequestDto = requestData;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      },
      complete:()=>{
        this.openSavedDepositControlRequestTemplate();
      }
    });
  }
}
