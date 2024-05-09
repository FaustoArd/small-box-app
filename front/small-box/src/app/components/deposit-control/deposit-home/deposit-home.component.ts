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
import { FormBuilder, Validators } from '@angular/forms';
import { DepositDto } from 'src/app/models/depositDto';
import { Router } from '@angular/router';
import { DepositItemComparatorDto } from 'src/app/models/depositItemComparatorDto';
import { OrganizationService } from 'src/app/services/organization.service';
import { OrganizationDto } from 'src/app/models/organizationDto';

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
  selectedDepositBol: boolean = false;
  selectedDepositStr: string = "";
  disableSelect:boolean=true;

  constructor(private dialogService: DialogService,
    private fileUploadService: FileUploadService, private snackBar: SnackBarService
    , private depositControlService: DepositControlService, private cookieService: CookieStorageService,
    private confirmDialogService: ConfirmDialogService, private formBuilder: FormBuilder
    , private router: Router,private organizationService:OrganizationService
  ) { }

  ngOnInit(): void {
    this.getCurrentDeposit();
   
  }
  currentRouter = this.router.url;

  file!: File;
  fileDetails!: FileDetails;
  fileUris: Array<string> = [];

  selectFile(event: any) {
    console.log("select file: " + event.target.files.item(0))
    this.file = event.target.files.item(0);
  }

  /**PURCHASE ORDER*/
  uploadPurchaseOrderFile() {
    const orgId = this.cookieService.getUserMainOrganizationId();
   this.fileUploadService.sendPurchaseOrderPdfToBackEnd(this.file, Number(orgId)).subscribe({
      next: (purchaseOrderData) => {
        this.purchaseOrderDto = purchaseOrderData;
      },
      error: (errorData) => {
        const errorEdited=String (errorData).replace('Error:','');
        
        this.snackBar.openSnackBar(errorEdited, 'Cerrar', 4000);
      },
      complete: () => {
        this.openPurchaseOrderTableTemplate(this.purchaseOrderDto.items);
      }
    });
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
  onClosePurchaseOrderTableTemplate(): void {
    this.purchaseOrderTableMatDialogRef.close();
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
      var confirmText = "El suministro n°: " + orderNumber + " ya fue cargado a un deposito, desea cargarlo nuevamente?";
      this.confirmDialogService.confirmDialog(confirmText).subscribe({
        next: (confirmData) => {
          this.confirmData = confirmData;
          if (this.confirmData) {
            this.loadPurchaseOrderToDepositControl(purchaseOrderId);
          }
        }
      });
    } else {
      this.loadPurchaseOrderToDepositControl(purchaseOrderId);
    }
  }

  depositReport: PurchaseOrderToDepositReportDto[] = [];
  loadPurchaseOrderToDepositControl(purchaseOrderId: number) {
    const depoId = Number(this.cookieService.getCurrentDepositSelectedId());
    this.depositControlService.loadPurchaseOrderToDeposit(purchaseOrderId, depoId).subscribe({
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
    this.getPurchaseOrder(purchaseOrderId);
    this.purchaseOrderItemListMatDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.purchaseOrderItemListMatDialogRef.afterClosed().subscribe();
  }

  findedPurchaseOrder!: PurchaseOrderDto;
  getPurchaseOrder(orderId: number) {
    this.depositControlService.findPurchaseOrderById(orderId).subscribe({
      next: (orderData) => {
        this.findedPurchaseOrder = orderData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
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
  purchaseOrderConfirmData!: boolean;
  confirmDeletePurchaseOrder(orderId: number) {
    var confirmText = "Desea eliminar la orden?";
    this.confirmDialogService.confirmDialog(confirmText).subscribe({
      next: (confirmData) => {
        this.purchaseOrderConfirmData = confirmData;
        if (this.purchaseOrderConfirmData) {
          this.deletePurchaseOrderById(orderId);
        } else {
          this.snackBar.openSnackBar('Se cancelo la operacion.', 'Cerrar', 3000);
        }
      }
    });
  }
  deletePurchaseOrderById(orderId: number) {
    this.depositControlService.deletePurchaseOrderById(orderId).subscribe({
      next: (orderNumberDeletedData) => {
        this.snackBar.openSnackBar('Se elimino la order de compra: ' + orderNumberDeletedData, 'Cerrar', 3000);
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      },
      complete:()=>{
        this.purchaseOrderDtos.forEach((item,index)=>{
          if(item.id==orderId){
            this.purchaseOrderDtos.splice(index,1);
          }
        })
      }
    });
  }

  @ViewChild('supplyTableTemplate') supplyTableTemplate !: TemplateRef<any>
  openSupplyTableTemplate(supplyItems: SupplyItemDto[]): void {
    this.supplyItemDtos = supplyItems;
    const template = this.supplyTableTemplate;
    this.getAllOrganizations();
    this.supplyTableTemplateRef = this.dialogService.openCreateRequestCreation({
      template
    });
    this.supplyTableTemplateRef.afterClosed().subscribe();

  }

  /**SUPPLY */
  uploadSupplyFile() {
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    this.fileUploadService.sendSupplyPdfToBackEnd(this.file, orgId).subscribe({
      next: (supplyData) => {
        this.supplyDto = supplyData;
      },
      error: (errorData) => {
        const errorEdited=String (errorData).replace('Error:','');
        this.snackBar.openSnackBar(errorEdited, 'Cerrar', 4000);
      },
      complete: () => {
        this.openSupplyTableTemplate(this.supplyDto.supplyItems);
      }
    });

  }
  onCloseSupplyTableTemplate() {
    this.supplyTableTemplateRef.close();
    this.router.navigateByUrl('deposit-home');
  }

  supplyOrganizationApplicantForm = this.formBuilder.group({
    id:[0,Validators.required]
  });
  get id(){
    return this.supplyOrganizationApplicantForm.controls.id;
  }


  organizationsDto:OrganizationDto[]=[];
  getAllOrganizations(){
    this.organizationService.getAllOrganizations().subscribe({
      next:(orgsData)=>{
        this.organizationsDto = orgsData;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    });
  }
  selectedApplicantDto!:OrganizationDto;
  setSupplyOrganizationApplicant(supplyId:number){
  if(this.supplyOrganizationApplicantForm.valid){
      this.selectedApplicantDto = new OrganizationDto();
      this.selectedApplicantDto = Object.assign
      (this.selectedApplicantDto,this.supplyOrganizationApplicantForm.value);
      this.depositControlService.setSupplyOrganizationApplicant(this.selectedApplicantDto,supplyId).subscribe({
        next:(orgNameData)=>{
          this.snackBar.openSnackBar('Se guardo la organizacion: ' + orgNameData + '. ','Cerrar',3000);
        },
        error:(errorData)=>{
          this.snackBar.openSnackBar(errorData,'Cerrar',3000);
        },complete:()=>{
          this.supplyOrganizationApplicantForm.reset();
         this.onCloseSupplyTableTemplate();
           }
       
      });
    }
  }
  
  setUpdateSupplyOrganizationApplicant(supplyId:number){
    if(this.supplyOrganizationApplicantForm.valid){
      this.selectedApplicantDto = new OrganizationDto();
      this.selectedApplicantDto = Object.assign
      (this.selectedApplicantDto,this.supplyOrganizationApplicantForm.value);
      this.depositControlService.setSupplyOrganizationApplicant(this.selectedApplicantDto,supplyId).subscribe({
        next:(orgNameData)=>{
          this.snackBar.openSnackBar('Se guardo la organizacion: ' + orgNameData + '. ','Cerrar',3000);
        },
        error:(errorData)=>{
          this.snackBar.openSnackBar(errorData,'Cerrar',3000);
        },complete:()=>{
          const orgId = Number(this.cookieService.getUserMainOrganizationId());
          this.getAllSupplies(orgId);
         this.onCloseUpdateSupplyOrganizationApplicantTemplate();
           }
       
      });
    }
  }


  updateSupplyOrganizationApplicantShow(){
    var check = this.findedSupply?.dependecyApplicantOrganizationId;
    if(typeof check !== 'undefined'){
    this.supplyOrganizationApplicantForm.patchValue({
      id:this.findedSupply.dependecyApplicantOrganizationId
    });
  }
  }
  onCloseUpdateSupplyOrganizationApplicantTemplate(){
    this.updateSuplyOrganizationApplicantTemplateRef.close();
  }
  
  findedSupplyId!:number;
  private updateSuplyOrganizationApplicantTemplateRef!:MatDialogRef<DialogTemplateComponent>
  openUpdateSupplyOrganizationApplicantTemplate(template:TemplateRef<any>,supplyId:number){
   this.getSupplybyId(supplyId);
   this.updateSupplyOrganizationApplicantShow();
   this.getAllOrganizations();
   this.findedSupplyId = supplyId;
   this.updateSuplyOrganizationApplicantTemplateRef = this.dialogService.openDialogCreation({
    template
   });
   this.updateSuplyOrganizationApplicantTemplateRef.afterClosed().subscribe();
  }

  private supplyTableTemplateRef!: MatDialogRef<DialogTemplateComponent>;

 
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

  findedSupply!: SupplyDto;
  getSupplybyId(supplyId: number) {
    this.depositControlService.findSupplyById(supplyId).subscribe({
      next: (supplyData) => {
        this.findedSupply = supplyData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    })
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
    this.getSupplybyId(supplyId);
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
    const depoId = Number(this.cookieService.getCurrentDepositSelectedId());
    this.depositControlService.createSupplyCorrectionNote(supplyId, depoId).subscribe({
      next: (noteData) => {
        this.supplyCorrectionNote = noteData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }

  supplyConfirmData!: boolean;
  confirmDeleteSupply(supplyId: number) {
    var confirmText = "Desea eliminar el suministro?";
    this.confirmDialogService.confirmDialog(confirmText).subscribe({
      next: (confirmData) => {
        this.supplyConfirmData = confirmData;
        if (this.supplyConfirmData) {
          this.deleteSupplyById(supplyId);
        } else {
          this.snackBar.openSnackBar('Se cancelo la operacion.', 'Cerrar', 3000);
        }
      }

    });
  }
  deleteSupplyById(supplyId: number) {
    this.depositControlService.deleteSupplyById(supplyId).subscribe({
      next: (supplyNumberDetetedData) => {
        this.snackBar.openSnackBar('Se elimino el suministro numero: ' + supplyNumberDetetedData, 'Cerrar', 3000);
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      },
      complete:()=>{
        this.suppliesDto.forEach((item,index)=>{
          if(item.id==supplyId){
            this.suppliesDto.splice(index,1);
          }
        });
      }
    });
  }

  /**DEPOSIT */
  depositControlDtos: DepositControlDto[] = [];

  getDepositControlsByDeposit() {
    const depoId = Number(this.cookieService.getCurrentDepositSelectedId());
    this.depositControlService.findAllDepositControlsByDeposit(depoId).subscribe({
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
    this.getDepositControlsByDeposit();
    this.depositControlListMatDialogRef = this.dialogService.openDialogDepositListCreation({
      template
    });
    this.depositControlListMatDialogRef.afterClosed().subscribe();
  }



  depositFormbuilder = this.formBuilder.group({
    name: ['', Validators.required],
    streetName: ['', Validators.required],
    houseNumber: ['', Validators.required],
  });

  depositDto!: DepositDto;

  onCloseDepositCreationTemplate() {
    this.depositCreateMatDialogRef.close();

  }

  private depositCreateMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  openDialogDepositCreation(template: any) {

    this.depositCreateMatDialogRef = this.dialogService.openSupplyCorrectionNoteCreation({
      template
    });
    this.depositCreateMatDialogRef.afterClosed().subscribe();

  }

  createDeposit() {
    if (this.depositFormbuilder.valid) {
      this.depositDto = new DepositDto();
      this.depositDto = Object.assign(this.depositDto, this.depositFormbuilder.value);
      const organizationId = Number(this.cookieService.getUserMainOrganizationId());
       this.depositDto.organizationId = organizationId;
     this.depositControlService.createDeposit(this.depositDto).subscribe({
        next: (depositNameData) => {
          this.snackBar.openSnackBar('Se creo el deposito: ' + depositNameData, 'Cerrar', 3000);
        },
        error: (errorData) => {
          this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);

        }, complete: () => {
          this.getAllDepositsByOrganization();
          this.onCloseDepositCreationTemplate();
        }
      });
    }
  }
  depositDtos: DepositDto[] = [];
  getAllDepositsByOrganization(): DepositDto[] {
    const organizationId = Number(this.cookieService.getUserMainOrganizationId());
    this.depositControlService.findAllDepositsByOrganization(organizationId).subscribe({
      next: (depositsData) => {
        this.depositDtos = depositsData;

      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
    return this.depositDtos;
  }
  deleteCurrentDepositSelected() {
    this.cookieService.deleteCurrentDepositSelectedId();
  }


  onCloseDepositSelectionTemplate() {
    this.depositSelectionMatDialogRef.close();

  }

  private depositSelectionMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  openDialogDepositSelection(template: any) {
    this.getAllDepositsByOrganization();
    this.depositSelectionMatDialogRef = this.dialogService.openSupplyCorrectionNoteCreation({
      template
    });
    this.depositSelectionMatDialogRef.afterClosed().subscribe({
      complete: () => {
        this.getCurrentDeposit();
      }
    });
  }



  setCurrentDeposit(depositId: number) {
    const userId = Number(this.cookieService.getCurrentUserId());
    const organizationId = Number(this.cookieService.getUserMainOrganizationId())
    this.depositControlService.setCurrentDeposit(userId, organizationId, depositId).subscribe({
      next: (depositIdData) => {
        this.cookieService.setCurrentDepositSelectedId(JSON.stringify(depositIdData.id));
        this.snackBar.openSnackBar("Se asigno el deposito: " + depositIdData.name, "Cerrar", 3000);

      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }, complete: () => {
        this.onCloseDepositSelectionTemplate();
      }
    });
  }

  getCurrentDeposit() {
    const userId = Number(this.cookieService.getCurrentUserId());
    const organizationId = Number(this.cookieService.getUserMainOrganizationId());
    this.depositControlService.getCurrentDeposit(userId, organizationId).subscribe({
      next: (depositIdData) => {
        if (depositIdData.id === null || undefined || depositIdData.id === 0) {
          this.selectedDepositBol = false;
          this.snackBar.openSnackBar('No hay ningun deposito asignado', 'Cerrar', 3000);
        } else {
          this.selectedDepositBol = true;
          this.cookieService.setCurrentDepositSelectedId(JSON.stringify(depositIdData.id));
          this.selectedDepositStr = depositIdData.name;
        }
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }


  deopsitControlConfirmData!: boolean;
  confirmDeleteDepositControl(depositControlId: number) {
    const controlIndex = this.depositControlDtos.findIndex(item => item.id == depositControlId);
    const controlCode = this.depositControlDtos[controlIndex].itemCode;
    var confirmText = "Desea eliminar el item de deposito numero: " + controlCode;
    this.confirmDialogService.confirmDialog(confirmText).subscribe({
      next: (confirmData) => {
        this.deopsitControlConfirmData = confirmData;
        if (this.deopsitControlConfirmData) {
          this.deleteDepositControlById(depositControlId);
        } else {
          this.snackBar.openSnackBar('Se cancelo la operacion.', 'Cerrar', 3000);
        }
      }

    });
  }
  depositControlUpdateForm = this.formBuilder.group({
    id: [0],
    supplyNumber: [''],
    itemDescription: [''],
    itemCode: [''],
    quantity: [0, Validators.required],
    expirationDate: [''],
    provider: [''],
    measureUnit: [''],
    itemUnitPrice: [0],
    itemTotalPrice: [0]
  });
  get quantity() {
    return this.depositControlUpdateForm.controls.quantity;
  }

  findedDepositControlDto!: DepositControlDto;
  updateDepositControlShow(findedDepositControlDto: DepositControlDto): void {
    this.depositControlUpdateForm.patchValue({
      id: this.findedDepositControlDto.id,
      supplyNumber: this.findedDepositControlDto.supplyNumber,
      itemCode:this.findedDepositControlDto.itemCode,
      itemDescription: this.findedDepositControlDto.itemDescription,
      quantity: this.findedDepositControlDto.quantity,
      expirationDate: JSON.stringify(this.findedDepositControlDto.expirationDate),
      provider: this.findedDepositControlDto.provider,
      measureUnit: this.findedDepositControlDto.measureUnit,
      itemUnitPrice: this.findedDepositControlDto.itemUnitPrice,
      itemTotalPrice: this.findedDepositControlDto.itemTotalPrice
    });
  }
  onCloseUpdateDepositControlTemplate() {
    this.updateDepositControlMatDialogRef.close();

  }


  private updateDepositControlMatDialogRef!: MatDialogRef<DialogTemplateComponent>

  openDialogDepositControlUpdate(depositControlId: number, template: TemplateRef<any>) {
    
    this.getDepositControlById(depositControlId);
    this.updateDepositControlMatDialogRef = this.dialogService.openDialogCreation({
      template
    })
    this.updateDepositControlMatDialogRef.afterClosed().subscribe();
  }

  updatedDepositControl!: DepositControlDto;
  updateDepositControl() {
    if (this.depositControlUpdateForm.valid) {
     
      this.findedDepositControlDto = Object.assign(this.findedDepositControlDto, this.depositControlUpdateForm.value);
      const depositId = Number(this.cookieService.getCurrentDepositSelectedId());
      this.depositControlService.updateDepositControl(this.findedDepositControlDto, depositId).subscribe({
        next: (controlData) => {
          this.updatedDepositControl = controlData;
        },
        error: (errorData) => {
          this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
        },
        complete:()=>{
          this.onCloseUpdateDepositControlTemplate();
          this.getDepositControlsByDeposit()
        }
      });
    }
  }

  editAllDepositControlfields(disableSelect:boolean):void{
    this.disableSelect=disableSelect;
  }


  getDepositControlById(depositControlId: number) {
    this.depositControlService.findDepositControlbyId(depositControlId).subscribe({
      next: (controlData) => {
        this.findedDepositControlDto = controlData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      },
      complete: () => {
        console.log("Item code"+this.findedDepositControlDto.itemCode)
        this.updateDepositControlShow(this.findedDepositControlDto);
      }
    });
  }



  deleteDepositControlById(depositControlId: number) {
    this.depositControlService.deleteDepositControlById(depositControlId).subscribe({
      next: (deletedData) => {
        this.snackBar.openSnackBar('Se elimino el item codigo: ' + deletedData + '.', 'Cerrar', 3000);
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      },
      complete:()=>{
        this.depositControlDtos.forEach((item,index)=>{
          if(item.id==depositControlId){
            this.depositControlDtos.splice(index,1);
          }
        })
      }
    });
  }




  get name() {
    return this.depositFormbuilder.controls.name;
  }
  get streetName() {
    return this.depositFormbuilder.controls.streetName;
  }
  get houseNumber() {
    return this.depositFormbuilder.controls.houseNumber;
  }




}

