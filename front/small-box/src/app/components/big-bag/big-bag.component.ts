import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { BigBagDto } from 'src/app/models/bigBagDto';
import { BigBagItemDto } from 'src/app/models/bigBagItemDto';
import { DepositControlDto } from 'src/app/models/depositControlDto';
import { ConfirmDialogService } from 'src/app/services/confirm-dialog.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { DepositControlService } from 'src/app/services/deposit-control.service';
import { DialogService } from 'src/app/services/dialog.service';
import { FileUploadService } from 'src/app/services/file-upload.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { DialogTemplateComponent } from '../dialog/dialog-template/dialog-template.component';

@Component({
  selector: 'app-big-bag',
  templateUrl: './big-bag.component.html',
  styleUrls: ['./big-bag.component.css']
})
export class BigBagComponent implements OnInit {

  constructor(private dialogService: DialogService,
    private fileUploadService: FileUploadService, private snackBar: SnackBarService
    , private depositControlService: DepositControlService, private cookieService: CookieStorageService,
    private confirmDialogService: ConfirmDialogService, private formBuilder: FormBuilder
    , private router: Router
  ) { }
  ngOnInit(): void {
    this.getCurrentDeposit();
    this.getAllDepositControls();
  }
  async reloadPage(){
    const currentUrl = this.router.url;
    await this.router.navigate(['reload-component']);
    await this.router.navigate([currentUrl])
  }

  createdEnabled!: boolean;
  createBigBagSwitch() {
    this.createdEnabled = true;
  }

  depositControlDtos: DepositControlDto[] = [];
  getAllDepositControls() {
    const depositId = Number(this.cookieService.getCurrentDepositSelectedId());
    this.depositControlService.findAllDepositControlsByDeposit(depositId).subscribe({
      next: (controlsData) => {
        this.depositControlDtos = controlsData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }
  bigBagForm = this.formBuilder.group({
    name: ['', Validators.required]
  })
  get name() {
    return this.bigBagForm.controls.name;
  }

 

  bigBagItemSelectedList: BigBagItemDto[] = [];

 



  selectDepositItem(code: string, measureUnit: string, description: string, depositControlId: number) {
    const check = this.bigBagItemSelectedList.filter(f => f.depositControlId == depositControlId).map(m => m.code);
    if (check.length > 0) {
      this.snackBar.openSnackBar('el  item: ' + check + ' Ya fue seleccionado', 'Cerrar', 3000);
    } else {
      this.bigBagItemSelectedList.push(new BigBagItemDto(code, measureUnit, description, depositControlId));
      const result = this.bigBagItemSelectedList.filter(f => f.depositControlId == depositControlId).map(m => m.code);
      this.snackBar.openSnackBar('Selecciono el  item: ' + result, 'Cerrar', 3000);

    }
  }
  deleteDepositeItemFromSelection(depositControlId: number): void {
    this.bigBagItemSelectedList.forEach((item, index) => {
      if (item.depositControlId === depositControlId) {
        this.bigBagItemSelectedList.splice(index, 1);

      }
    });
  }
  bigBagItemValue: BigBagItemDto[] = [];
  applyAllBigBagItemQuantity() {
    if (this.bigBagItemForm.valid) {
      console.log(this.bigBagItemForm.value)
      this.bigBagItemValue = Object.assign(this.bigBagItemValue, this.bigBagItemForm.value);

    } else {
      this.snackBar.openSnackBar('Debe ingresar una cantidad antes de aplicar.', 'Cerrar', 3000);
    }

  }

  returnedBigBag!: BigBagDto;
  savedBigBagDto!: BigBagDto;
  
  createBigBag() {
    if(this.bigBagForm.valid){
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    this.savedBigBagDto = new BigBagDto();
      this.savedBigBagDto = Object.assign(this.savedBigBagDto,this.bigBagForm.value);
      this.savedBigBagDto.items = this.bigBagItemSelectedList;
      console.log(this.savedBigBagDto);
    this.depositControlService.createBigBag(this.savedBigBagDto, orgId).subscribe({
      next: (bigBagData) => {
        this.returnedBigBag = bigBagData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }, complete: () => {
        this.createdEnabled = false;
        this.snackBar.openSnackBar('Se creo el bolson: ' + this.returnedBigBag.name, 'Cerrar', 3000);
      }
    });
  }
  }
  

  currentDepositName!: string;
  getCurrentDeposit() {
    const userId = Number(this.cookieService.getCurrentUserId());
    const organizationId = Number(this.cookieService.getUserMainOrganizationId());
    this.depositControlService.getCurrentDeposit(userId, organizationId).subscribe({
      next: (depositData) => {
        this.currentDepositName = depositData.name;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }

  findedBigBagDtos:BigBagDto[]=[];
  getAllBigBagsByOrg(){
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    this.depositControlService.findAllBigBagsByOrg(orgId).subscribe({
      next:(bigBagsData)=>{
        this.findedBigBagDtos =bigBagsData; 
      },error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }

    });
  }

  bigBagTotalQuantity:number = -1;
  onCloseBigBagListTemplate(){
    this.bigBagListMatDialogRef.close();
    this.bigBagTotalQuantity = -1;
  }
  private bigBagListMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  openDialogSupplyList(template: any) {
    this.getAllBigBagsByOrg();
    this.bigBagListMatDialogRef = this.dialogService.openCustomDialogCreation({
      template
    },'50%','60%',true,false);
    this.bigBagListMatDialogRef.afterClosed().subscribe();

  }
 
  calculateBigBagTotalQuantity(bigBagId:number){
    
    const depositId = Number(this.cookieService.getCurrentDepositSelectedId());
    this.depositControlService.calculateBigBagTotalquantity(bigBagId,depositId).subscribe({
      next:(totalquantityData)=>{
        this.bigBagTotalQuantity = totalquantityData;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    });

  }
  onCloseBigBagItemListTemplate(){
    this.bigBagItemsListMatDialogRef.close();
  }
  @ViewChild('bigBagItemsListTemplate')bigBagItemsTableTemplate !: TemplateRef<any>
  private bigBagItemsListMatDialogRef!: MatDialogRef<DialogTemplateComponent>;
  openDialogBigBagItemsList(bigBagId:number):void{
   this.getAllBigBagItems(bigBagId);
   const template = this.bigBagItemsTableTemplate;
    this.bigBagItemsListMatDialogRef = this.dialogService.openCustomDialogCreation({
      template
    },'60%','60%',true,true);
    this.bigBagItemsListMatDialogRef.afterClosed().subscribe();

  }
  bigBagItemForm = this.formBuilder.group({
    quantity: [0, Validators.required],
  
  });

 

  get quantity() {
    return this.bigBagItemForm.controls.quantity;
  }

  findedBigBagItems:BigBagItemDto[]=[];
  getAllBigBagItems(bigBagId:number){
    this.depositControlService.findAllBigBagItems(bigBagId).subscribe({
      next:(itemsData)=>{
        this.findedBigBagItems = itemsData;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    });
  }

  updatedBigBagItemDto!:BigBagItemDto;
  returnedBigBagItemDto!:BigBagItemDto;
  updateBigBagItemQuantity(bigBagItemId:number){
    this.updatedBigBagItemDto = new BigBagItemDto('','','',0);
   this.updatedBigBagItemDto = Object.assign(this.updatedBigBagItemDto, this.bigBagItemForm.value);
   this.depositControlService.updateBigBagItemQuantity(bigBagItemId,this.updatedBigBagItemDto.quantity).subscribe({
    next:(bigBagItemData)=>{
      this.returnedBigBagItemDto = bigBagItemData;
    },
    error:(errorData)=>{
      this.snackBar.openSnackBar(errorData,'Cerrar',3000);
    },
    complete:()=>{
      this.snackBar.openSnackBar('Se actualizo la cantidad del item:  '
       + this.returnedBigBagItemDto.code + ', Cantidad:' + this.returnedBigBagItemDto.quantity,'Cerrar',3000);
       this.getAllBigBagItems(this.returnedBigBagItemDto.bigBagId);
      this.bigBagItemForm.reset();
    }
   });
  }

 
}


