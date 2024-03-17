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
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';

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


  constructor(private smallBoxService: SmallBoxService, private inputService: InputService
    , private formBuilder: FormBuilder, private snackBar: SnackBarService, private router: Router,
    private cookieService: CookieStorageService, private dialogService: DialogService) { }



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
  });

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


  
  getAllSmallBoxesByContainerId(): void {
    this.smallBoxService.findSmallBoxesByContainerId(Number(this.cookieService.getCurrentContainerId())).subscribe({
      next: (smallData) => {
        this.smallboxes = smallData;
        this.sumTickets(this.smallboxes)
      },
      error: (errorData) => {
        this.snackBar.openSnackBar(errorData, 'Close', 3000);
      }

    });
  }

  public ticketsTotal:number = 0;

  sumTickets(smallboxes:SmallBoxDto[]):void{
     this.ticketsTotal = 0;
    const result = this.smallboxes.forEach(e => {
        this.ticketsTotal = this.ticketsTotal + e.ticketTotal;
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

  navigateAssociates() {
    const url = this.router.serializeUrl(
      this.router.createUrlTree(['/completed'])
    );
  
    window.open(url, '_blank');
  }

  completeSmallBox(){
    const id = Number(this.cookieService.getCurrentContainerId())
    this.smallBoxService.checkMaxAmount(id).subscribe({
      next:(nextData)=>{
        this.snackBar.openSnackBar(nextData,'Cerrar',3000);
      },error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      },complete:()=>{
        this.navigateAssociates();
      }
    });
   
  }

 
}
