import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SmallBoxDto } from 'src/app/models/smallBoxDto';
import { InputService } from 'src/app/services/input.service';
import { SmallBoxService } from 'src/app/services/small-box.service';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms'
import { InputDto } from 'src/app/models/inputDto';
import { Router } from '@angular/router';
import { StorageService } from 'src/app/services/storage.service';

import { DialogService } from 'src/app/services/dialog.service';
import { MatDialogRef } from '@angular/material/dialog';
import { DialogTemplateComponent } from '../dialog/dialog-template/dialog-template.component';
import { formatDate } from '@angular/common';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { FileDetails } from 'src/app/models/fileDetails';
import { ReceiptDto } from 'src/app/models/receiptDto';

@Component({
  selector: 'app-small-box',
  templateUrl: './small-box.component.html',
  styleUrls: ['./small-box.component.css']
})

export class SmallBoxComponent implements OnInit {
  
  inputs: InputDto[] = [];
  smallboxes: SmallBoxDto[] = [];
  smallbox!: SmallBoxDto;
  updatedSmallBox!: SmallBoxDto;
  errorData!: string;
  updatedData!: string;
  formatedDate!: Date;
  receiptDto!:ReceiptDto;
 


  constructor(private smallBoxService: SmallBoxService, private inputService: InputService
    , private formBuilder: FormBuilder, private snackBar: SnackBarService, private router: Router,
    private cookieService: CookieStorageService, private dialogService: DialogService) { }



  ngOnInit() {
    this.getAllInputs();
    this.getAllSmallBoxesByContainerId();
  }

  smallBoxForm = this.formBuilder.group({
    date: ['', Validators.required],
    ticketNumber: ['', Validators.required],
    provider: ['', Validators.required],
    inputId: [0],
    description: [''],
    ticketTotal: [0, Validators.required],

  });
  get date() {

    return this.smallBoxForm.controls.date
  }
  get ticketNumber() {
    return this.smallBoxForm.controls.ticketNumber
  }
  get provider() {
    return this.smallBoxForm.controls.provider
  }

  get description() {
    return this.smallBoxForm.controls.description
  }
  get ticketTotal() {
    return this.smallBoxForm.controls.ticketTotal
  }


  onAddSmallBox(): void {
    if (this.smallBoxForm.valid) {
      this.smallbox = new SmallBoxDto();
      this.smallbox = Object.assign(this.smallbox, this.smallBoxForm.value);
      this.smallBoxService.addSmallBox(this.smallbox, Number(this.cookieService.getCurrentContainerId())).subscribe({

        next: (smallBoxData) => {
          this.smallbox = smallBoxData;
        },
        error: (errorData) => {
          this.errorData = errorData;
          this.snackBar.openSnackBar(errorData, 'Cerrar', 3000);
        },
        complete: () => {
          this.snackBar.openSnackBar("Se agrego el ticket!", 'Cerrar', 3000);
          this.smallBoxForm.reset();
          this.getAllSmallBoxesByContainerId();
          this.matDialogRef.close();
        }
      });
    }
  }

  updateSmallBoxForm = this.formBuilder.group({
    id: [0],
    date: ['', Validators.required],
    ticketNumber: ['', Validators.required],
    provider: ['', Validators.required],
    inputId: [0],
    description: ['', Validators.required],
    ticketTotal: [0, Validators.required],
    inputNumber: [''],
  })

  update(): void {
    this.updateSmallBoxForm.reset();
    this.matDialogRef.close();
  }

  onUpdateSmallBoxShow(): void {
    this.updateSmallBoxForm.patchValue({
      id: this.updatedSmallBox.id,
      date: this.updatedSmallBox.date,
      ticketNumber: this.updatedSmallBox.ticketNumber,
      provider: this.updatedSmallBox.provider,
      inputId: this.updatedSmallBox.inputId,
      description: this.updatedSmallBox.description,
      ticketTotal: this.updatedSmallBox.ticketTotal

    });
  }

  private matDialogRef!: MatDialogRef<DialogTemplateComponent>

  openDialogSmallBoxUpdate(id: number, template: TemplateRef<any>) {
    this.getSmallBoxById(id);
    this.matDialogRef = this.dialogService.openDialogCreation({
      template
    })

    this.matDialogRef.afterClosed().subscribe();
    this.updateSmallBoxForm.reset();

  }

  updateSmallBox(): void {
    if (this.updateSmallBoxForm.valid) {
      this.updatedSmallBox = Object.assign(this.updatedSmallBox, this.updateSmallBoxForm.value);
      this.smallBoxService.updateSmallBox(this.updatedSmallBox).subscribe({
        next: (smData) => {
          this.snackBar.openSnackBar(smData, 'Close', 3000);
        },
        error: (errorData) => {
          this.snackBar.openSnackBar(errorData, 'Close', 3000);
        },
        complete: () => {
          this.getAllSmallBoxesByContainerId();
          this.update();
        }
      });
    }
  }


  getAllSmallBoxes(): void {
    this.smallBoxService.findSmallBoxes().subscribe({
      next: (smallData) => {
        this.smallboxes = smallData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Close', 3000);
      }
    })
  }

  getAllSmallBoxesByContainerId(): void {
    this.smallBoxService.findSmallBoxesByContainerId(Number(this.cookieService.getCurrentContainerId())).subscribe({
      next: (smallData) => {
        this.smallboxes = smallData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Close', 3000);
      }

    });
  }


  getAllInputs(): void {
    this.inputService.findAllInputs().subscribe({
      next: (inputData) => {
        this.inputs = inputData;
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Close', 3000);
      }
    })
  }

  convertDate(): void {

  }

  getSmallBoxById(id: number): void {
    this.smallBoxService.getSmallBoxById(id).subscribe({
      next: (smData) => {
        this.updatedSmallBox = smData;
        this.onUpdateSmallBoxShow();

      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Close', 3000);
      }
    });
  }

  deleteSmallBoxById(id: number): void {
    this.smallBoxService.deleteSmallBoxById(id).subscribe({
      next: (smData) => {
        this.snackBar.openSnackBar(smData, 'Close', 3000);
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Close', 3000);
      },
      complete: () => {
        this.getAllSmallBoxesByContainerId();
      }

    })
  }
  file!:File;
  fileDetails!: FileDetails;
  fileUris:Array<string> = [];
  //Upload file last
  selectFile(event:any){
    console.log("select file: " + event.target.files.item(0))
    this.file = event.target.files.item(0);
  }

   template:any

  uploadFile(){
    this.smallBoxService.sendFileToBackEnd(this.file).subscribe({
      next:(receiptData) =>{
        this.receiptDto = receiptData;
       console.log(this.receiptDto)
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      },
      complete:()=>{
        this.snackBar.openSnackBar("Se ejecuto el analisis de la factura: " + this.receiptDto.receipt_code,'Cerrar',3000);
        this.openDialogSmallBoxDocumentAI(this.receiptDto);
      }
    });
   
  }
  

  @ViewChild('documentAISmallBoxTemplate') docAITemplateRef!: TemplateRef<any>

  openDialogSmallBoxDocumentAI(receipt:ReceiptDto) {
    this.getAllInputs();
    const template = this.docAITemplateRef;
    
    this.onDocumentAISmallBoxShow(receipt);
    this.matDialogRef = this.dialogService.openDialogCreation({
      template
    })

    this.matDialogRef.afterClosed().subscribe();
    
   
   
  }

  onDocumentAISmallBoxShow(receipt:ReceiptDto): void {
    this.smallBoxForm.patchValue({
     
      date: receipt.receipt_date,
      ticketNumber:receipt.receipt_code,
      provider: receipt.supplier_name.map(e => e).toString(),
      
      ticketTotal:Number (receipt.total_price),

    });
  }
  
  
}
