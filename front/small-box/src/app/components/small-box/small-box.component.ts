import { Component, OnInit, TemplateRef } from '@angular/core';
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
  updatedData!:string;
  formatedDate!:Date;


  constructor(private smallBoxService: SmallBoxService, private inputService: InputService
    , private formBuilder: FormBuilder, private snackBar: MatSnackBar, private router: Router,
    private storageService: StorageService, private dialogService: DialogService) { }



  ngOnInit() {
    this.getAllInputs();
    this.getAllSmallBoxesByContainerId();
  }
  smallBoxForm = this.formBuilder.group({
    date: ['', Validators.required],
    ticketNumber: [0, Validators.required],
    provider: ['', Validators.required],
    inputId: [0],
    description: [''],
    ticketTotal: [0, Validators.required],
    inputNumber: [0],
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
  get inputNumber() {
    return this.smallBoxForm.controls.inputNumber
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
      this.smallBoxService.addSmallBox(this.smallbox, Number(this.storageService.getCurrentContainerId())).subscribe({

        next: (smallBoxData) => {
          this.smallbox = smallBoxData;
        },
        error: (errorData) => {
          this.errorData = errorData;
          this.onSnackBarMessage("Debe ingresar una descripcion o un input!");
        },
        complete: () => {
          this.onSnackBarMessage("Se agrego el ticket!")
          this.smallBoxForm.reset();
          this.getAllSmallBoxesByContainerId();
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
      ticketTotal: this.updatedSmallBox.ticketTotal,
      inputNumber: this.updatedSmallBox.inputNumber,
    });
  }

  private matDialogRef!: MatDialogRef<DialogTemplateComponent>

  openDialogSmallBoxUpdate(id:number,template: TemplateRef<any>) {
    this.getSmallBoxById(id);
    this.matDialogRef = this.dialogService.openDialogCreation({
      template
    })

    this.matDialogRef.afterClosed().subscribe();
    this.updateSmallBoxForm.reset();

  }

  updateSmallBox():void{
    if(this.updateSmallBoxForm.valid){
      this.updatedSmallBox = Object.assign(this.updatedSmallBox,this.updateSmallBoxForm.value);
      this.smallBoxService.updateSmallBox(this.updatedSmallBox).subscribe({
        next:(smData)=>{
         this.onSnackBarMessage(smData);
        },
        error:(errorData)=>{
          this.onSnackBarMessage(errorData);
        },
        complete:()=>{
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
        this.errorData = errorData;
        this.onSnackBarMessage(this.errorData);
      }
    })
  }

  getAllSmallBoxesByContainerId(): void {
    this.smallBoxService.findSmallBoxesByContainerId(Number(this.storageService.getCurrentContainerId())).subscribe({
      next: (smallData) => {

        this.smallboxes = smallData;
      },
      error: (errorData) => {
        this.errorData = errorData;
        this.onSnackBarMessage(this.errorData);
      }

    });
  }


  getAllInputs(): void {
    this.inputService.findAllInputs().subscribe({
      next: (inputData) => {
        this.inputs = inputData;
      },
      error: (errorData) => {
        this.errorData = errorData;
      }
    })
  }

  convertDate():void{
   
  }

  getSmallBoxById(id: number): void {
    this.smallBoxService.getSmallBoxById(id).subscribe({
      next: (smData) => {
        this.updatedSmallBox = smData;
     this.onUpdateSmallBoxShow();

      },
      error:(errorData)=>{
        this.errorData = errorData;
        this.onSnackBarMessage(this.errorData);
      }
    });
  }



  onSnackBarMessage(message: any) {
    this.snackBar.open(message, 'Cerrar', {
      duration: 3000,
      verticalPosition: 'top',
      horizontalPosition: 'center',

    });
  }



}
