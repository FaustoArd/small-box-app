import { Component, OnInit, TemplateRef } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Destination } from 'src/app/models/destination';
import { OrganizationDto } from 'src/app/models/organizationDto';
import { Ref } from 'src/app/models/ref';
import { WorkTemplateDto } from 'src/app/models/workTemplateDto';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { DialogService } from 'src/app/services/dialog.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { WorkTemplateService } from 'src/app/services/work-template.service';
import { DialogTemplateComponent } from '../../dialog/dialog-template/dialog-template.component';
import { DestinationDto } from 'src/app/models/destinationDto';
import { BeforeBy } from 'src/app/models/beforeBy';

@Component({
  selector: 'app-memo-single',
  templateUrl: './memo-single.component.html',
  styleUrls: ['./memo-single.component.css']
})
export class MemoSingleComponent implements OnInit {

  workTemplate!: WorkTemplateDto;
  returnedWorkTemplate!: WorkTemplateDto;
  strDestination: string = '';
  destinationsList!: Array<string>
  destinations: Array<string> = [];
  refsList!: Array<string>;
  textTemplateList:Array<string> = [];
  refs: Array<string> = [];
  refPartial!: Ref;
  destinationPartial!: Destination;
  destinationsDtoList: DestinationDto[] = [];
  destinationDto!: DestinationDto;
  organizations: OrganizationDto[] = [];
  beforeBys: Array<string> = [];
  beforeByPartial!:BeforeBy;

  constructor(private workTemplateService: WorkTemplateService, private cookieService: CookieStorageService
    , private formBuilder: FormBuilder, private snackBarService: SnackBarService
    , private organizationService: OrganizationService, private router: Router, private dialogService: DialogService) { }


  ngOnInit(): void {
    this.getAllOrganizationsByUser();
    this.getAllTemplateDestinationsList();
    this.getRefsList();
  }

  getTextTemplates():void{

  }

 

  refFormBuilder = this.formBuilder.group({
    ref: ['', Validators.required],
    refNumber: ['', Validators.required]
  });

  addSelectedRef() {
    this.refPartial = new Ref();
    this.refPartial = Object.assign(this.refPartial, this.refFormBuilder.value);
    let result = this.refs.filter(ref => ref == this.refPartial.ref + ' ' + this.refPartial.refNumber).toString();
    if (result === this.refPartial.ref + ' ' + this.refPartial.refNumber) {
      this.snackBarService.openSnackBar('La Referencia ya ha sido agregada', 'Cerrar', 3000);
    } else {
      let finalResult = this.refPartial.ref + ' ' + this.refPartial.refNumber
      console.log(finalResult);
      this.refs.push(finalResult);
      this.getRefsList();
    }
  }

  deleteSelectedRef(ref: string) {
    this.refs.forEach((item, index) => {
      if (item == ref) {
        this.refs.splice(index, 1);
        this.snackBarService.openSnackBar('Se elimino' + ref, 'Cerrar', 3000);
      }
    })
  }

  getRefsList(): void {
    this.refsList = ["MEMO", "NOTA", "EXP", "OC"];
  }

  beforeByFormBuilder = this.formBuilder.group({
    beforeBy:['',Validators.required]
  });

  addSelectedBeforeBy(){
    if(this.beforeByFormBuilder.valid){
      this.beforeByPartial = new BeforeBy();
      this.beforeByPartial = Object.assign(this.beforeByPartial,this.beforeByFormBuilder.value);
     let result = this.beforeBys.filter(bef => bef == this.beforeByPartial.beforeBy).toString();
      console.log(this.beforeByPartial)
      if(result === this.beforeByPartial.beforeBy){
        this.snackBarService.openSnackBar('La dependencia ya ha sido agregada', 'Cerrar', 3000);
      }else{
        this.beforeBys.push(this.beforeByPartial.beforeBy);
        console.log(this.beforeBys);
        this.beforeByFormBuilder.reset();
        this.snackBarService.openSnackBar('Se agrego la Dependencia: ' + this.beforeByPartial.beforeBy, 'Cerrar', 3000);
        this.getAllTemplateDestinationsList();
      }
    }
  }
  deleteSelectedBeforeBy(beforeBy:string):void{
    this.beforeBys.forEach((item,index)=>{
      if(item == beforeBy){
        this.beforeBys.splice(index,1);
        this.snackBarService.openSnackBar('Se elimino: ' + beforeBy, 'Cerrar', 3000);
        this.getAllTemplateDestinationsList();
      }
    })
  }



destinationsFormBuilder = this.formBuilder.group({
    destination: ['', Validators.required]
  });

  addSelectedDestination() {
    if(this.destinationsFormBuilder.valid){
    this.destinationPartial = new Destination();
    this.destinationPartial = Object.assign(this.destinationPartial, this.destinationsFormBuilder.value);
    let result = this.destinations.filter(des => des == this.destinationPartial.destination).toString();
    if (result === this.destinationPartial.destination) {
      this.snackBarService.openSnackBar('La dependencia ya ha sido agregada', 'Cerrar', 3000);
    } else {
      this.destinations.push(this.destinationPartial.destination);
      this.destinationsFormBuilder.reset();
     this.getAllTemplateDestinationsList();
     
    }
  }else{
    this.destinationsFormBuilder.reset();
    this.snackBarService.openSnackBar('Debe seleccionar un destino','Cerrar',3000);
  }
  }

  deleteSelectedDestination(destination: string) {
    this.destinations.forEach((item, index) => {
      if (item == destination) {
        this.destinations.splice(index, 1);
        this.snackBarService.openSnackBar('Se elimino: ' + destination, 'Cerrar', 3000);
        this.getAllTemplateDestinationsList();
      }
    });
  }

  getAllTemplateDestinationsList() {
    this.workTemplateService.getAllWorkTemplateDestinations().subscribe({
      next: (templateDestinationData) => {
        this.destinationsDtoList = templateDestinationData;
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }

  memoFormBuilder = this.formBuilder.group({
    date: ['', Validators.required],
    correspond: ['', Validators.required],
    correspondNumber: ['', Validators.required],
    text: ['', Validators.required],
    organizationId: [0]
  });

  createMemo() {
    if (this.memoFormBuilder.valid) {
      this.workTemplate = new WorkTemplateDto();
      this.workTemplate = Object.assign(this.workTemplate, this.memoFormBuilder.value);
      this.workTemplate.destinations = this.destinations;
      this.workTemplate.refs = this.refs;
      this.workTemplate.beforeBy = this.beforeBys;
      this.workTemplateService.createWorkTemplate(this.workTemplate).subscribe({
        next: (memoData) => {
          this.returnedWorkTemplate = memoData;
          this.cookieService.setCurrentWorkTemplateId(JSON.stringify(this.returnedWorkTemplate.id));
          this.snackBarService.openSnackBar('Listo', 'Cerrar', 3000);
        },
        error: (errorData) => {
          this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
        },
        complete: () => {
          this.router.navigateByUrl('memo-show');
        }
      });
    }
  }

  getAllOrganizationsByUser() {
    this.organizationService.getAllOrganizationsByUser(Number(this.cookieService.getCurrentUserId())).subscribe({
      next: (orgData) => {
        this.organizations = orgData;
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
      }
    })
  }


  private matDialogRef!: MatDialogRef<DialogTemplateComponent>

  openDialogTemplateDestination(template: TemplateRef<any>) {
    
    this.matDialogRef = this.dialogService.openDialogCreation({
      template
    })

    this.matDialogRef.afterClosed().subscribe();
    this.createDestinationFormBuilder.reset();

  }


  createDestinationFormBuilder = this.formBuilder.group({
    destination: ['', Validators.required]
  });

  update(): void {
    this.createDestinationFormBuilder.reset();
    this.matDialogRef.close();
  }

  createTemplateDestinaton() {
    if (this.createDestinationFormBuilder.valid) {
      this.destinationDto = new DestinationDto();
      this.destinationDto = Object.assign(this.destinationDto, this.createDestinationFormBuilder.value);
      this.workTemplateService.createTemplateDestination(this.destinationDto).subscribe({
        next: (responseData) => {
          this.snackBarService.openSnackBar(responseData, 'Cerrar', 3000);
        },
        error: (errorData) => {
          this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
        },
        complete:()=>{
        this.update();
        this.getAllTemplateDestinationsList();
        }
      });
    }
  }




  get date() {
    return this.memoFormBuilder.controls.date;
  }
  get correspond() {
    return this.memoFormBuilder.controls.correspond;
  }
  get ref() {
    return this.refFormBuilder.controls.ref;
  }
  get refNumber() {
    return this.refFormBuilder.controls.refNumber;
  }

  get beforeBy(){
    return this.beforeByFormBuilder.controls.beforeBy;
  }

  get destination() {
    return this.destinationsFormBuilder.controls.destination;
  }
  get correspondNumber() {
    return this.memoFormBuilder.controls.correspondNumber;
  }




}
