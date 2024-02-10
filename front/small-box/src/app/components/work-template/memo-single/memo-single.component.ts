import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Destination } from 'src/app/models/destination';
import { OrganizationDto } from 'src/app/models/organizationDto';
import { Ref } from 'src/app/models/ref';
import { WorkTemplate } from 'src/app/models/workTemplate';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { WorkTemplateService } from 'src/app/services/work-template.service';

@Component({
  selector: 'app-memo-single',
  templateUrl: './memo-single.component.html',
  styleUrls: ['./memo-single.component.css']
})
export class MemoSingleComponent implements OnInit {

  workTemplate!: WorkTemplate;
  returnedWorkTemplate!: WorkTemplate;
  destinationsList!: Array<string>
  destinations: Array<string> = [];
  refsList!: Array<string>;
  refs: Array<string> = [];
  refPartial!: Ref;
  destinationPartial!: Destination;
  organizations: OrganizationDto[] = [];

  constructor(private workTemplateService: WorkTemplateService, private cookieService: CookieStorageService
    , private formBuilder: FormBuilder, private snackBarService: SnackBarService
    , private organizationService: OrganizationService, private router: Router) { }


  ngOnInit(): void {
    this.getAllOrganizationsByUser();
    this.getDestinationsList();
    this.getRefsList();
  }

  getRefsList(): void {
    this.refsList = ["MEMO", "NOTA", "EXP", "OC"];
  }

  getDestinationsList(): void {
    this.destinationsList = ["Direccion de pagos", "Contaduria-Contable", "Direccion de compras", "Secretaria de administracion"];

  }

  addSelectedRef() {
    this.refPartial = new Ref();
    this.refPartial = Object.assign(this.refPartial,this.refFormBuilder.value);
    
    let result = this.refs.filter(ref => ref == this.refPartial.ref + ' ' + this.refPartial.refNumber).toString();
   
    if(result === this.refPartial.ref + ' ' + this.refPartial.refNumber){
      this.snackBarService.openSnackBar('La Referencia ya ha sido agregada', 'Cerrar', 3000);
    }else{
     
      let finalResult = this.refPartial.ref+ ' ' + this.refPartial.refNumber
      console.log(finalResult);
      this.refs.push(finalResult);
     this.getRefsList();
    }
  }

  deleteSelectedRef(ref:string){
this.refs.forEach((item,index) =>{
  if(item == ref){
    this.refs.splice(index,1);
    this.snackBarService.openSnackBar('Se elimino' + ref,'Cerrar', 3000);
  }
})
  }

  addSelectedDestination() {
    this.destinationPartial = new Destination();
    this.destinationPartial = Object.assign(this.destinationPartial, this.destinationsFormBuilder.value);
    let result = this.destinations.filter(des => des == this.destinationPartial.destination).toString();
    if (result === this.destinationPartial.destination) {
      this.snackBarService.openSnackBar('La dependencia ya ha sido agregada', 'Cerrar', 3000);
    } else {
      this.destinations.push(this.destinationPartial.destination)
      //this.cookieService.setDestinationsList(this.destinations);
      console.log(this.cookieService.getDestinationsList());
      this.getDestinationsList();
    }
  }

  deleteSelectedDestination(destination: string) {
    this.destinations.forEach((item, index) => {
      if (item == destination) {
        this.destinations.splice(index, 1);
       // this.cookieService.setDestinationsList(this.destinations);
        this.getDestinationsList();
        this.snackBarService.openSnackBar('Se elimino: ' + destination, 'Cerrar', 3000);
      }
    });

  }

  memoFormBuilder = this.formBuilder.group({
    date: ['', Validators.required],
    corresponds: ['', Validators.required],
    correspondsNumber: ['', Validators.required],
    text: ['', Validators.required],
    organizationId: [0]
  });

  destinationsFormBuilder = this.formBuilder.group({
    destination: ['', Validators.required]
  });

  refFormBuilder = this.formBuilder.group({
    ref:['', Validators.required],
    refNumber:['',Validators.required]
  });

  createMemo() {
    if (this.memoFormBuilder.valid) {
      this.workTemplate = new WorkTemplate();
      this.workTemplate = Object.assign(this.workTemplate, this.memoFormBuilder.value);
      this.workTemplate.destinations = this.destinations;
      this.workTemplate.refs = this.refs;
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



  get date() {
    return this.memoFormBuilder.controls.date;
  }
  get corresponds() {
    return this.memoFormBuilder.controls.corresponds;
  }
  get ref() {
    return this.refFormBuilder.controls.ref;
  }
  get refNumber(){
    return this.refFormBuilder.controls.refNumber;
  }

  get destination() {
    return this.destinationsFormBuilder.controls.destination;
  }
  get correspondsNumber(){
    return this.memoFormBuilder.controls.correspondsNumber;
  }
  



}
