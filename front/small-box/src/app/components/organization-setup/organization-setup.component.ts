import { Component, OnInit, TemplateRef } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { OrganizationDto } from 'src/app/models/organizationDto';
import { OrganizationResponsibleDto } from 'src/app/models/organizationResponsibleDto';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { DialogTemplateComponent } from '../dialog/dialog-template/dialog-template.component';
import { DialogService } from 'src/app/services/dialog.service';

@Component({
  selector: 'app-organization-setup',
  templateUrl: './organization-setup.component.html',
  styleUrls: ['./organization-setup.component.css']
})
export class OrganizationSetupComponent implements OnInit {

  constructor(private organizationService: OrganizationService, private snackBarService: SnackBarService
    , private formBuilder: FormBuilder,private dialogService:DialogService) { }

  organizationDto!: OrganizationDto;
  updateOrganizationDto!:OrganizationDto;
  organizationUpdateDto!:OrganizationDto;
  responsibleDto!: OrganizationResponsibleDto;
  responsiblesOrgs:OrganizationResponsibleDto[] = [];
  organizationsDto:OrganizationDto[]= [];
  responsiblesDto:OrganizationResponsibleDto[]=[];
  private matDialogRef!: MatDialogRef<DialogTemplateComponent>;

  ngOnInit(): void {
    this.getOrganizations();
    this.getResponsibles();

  }

  organizationForm = this.formBuilder.group({
    organizationName: ['', Validators.required],
    organizationNumber: ['', Validators.required],
    maxRotation: [0, Validators.required],
    maxAmount: [0, Validators.required],
    responsibleId:[0, Validators.required]
  });


  get organizationName() {
    return this.organizationForm.controls.organizationName;
  }
  get organizationNumber() {
    return this.organizationForm.controls.organizationNumber;
  }
  get maxRotation() {
    return this.organizationForm.controls.maxRotation;
  }
  get maxAmount() {
    return this.organizationForm.controls.maxAmount;
  }
  get responsible(){
    return this.organizationForm.controls.responsibleId;
  }

  newOrganization() {
    if (this.organizationForm.valid) {
      this.organizationDto = new OrganizationDto();
      this.organizationDto = Object.assign(this.organizationDto, this.organizationForm.value);
      console.log(this.organizationDto)
      this.organizationService.newOrganization(this.organizationDto).subscribe({
        next: (orgData) => {
          this.snackBarService.openSnackBar('Se a creado la Organizacion: ' + orgData.organizationName, 'Cerrar', 3000);
        },
        error: (errorData) => {
          this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
        },
        complete:()=>{
          this.getOrganizations();
        }
      });
    }
  }

  responsibleForm = this.formBuilder.group({


    name: ['', Validators.required],

    lastname: ['', Validators.required],

   
  });

  get name() {
    return this.responsibleForm.controls.name;
  }
  get lastname() {
    return this.responsibleForm.controls.lastname;
  }
 

  newResponsible() {
    if (this.responsibleForm.valid) {
      
      this.responsibleDto = new OrganizationResponsibleDto();
      this.responsibleDto = Object.assign(this.responsibleDto, this.responsibleForm.value);
      console.log("hola " + this.responsibleDto)
      this.organizationService.newResponsible(this.responsibleDto).subscribe({
        next: (respData) => {
          this.snackBarService.openSnackBar('Se a creado el responsable: ' + respData.name + ' ' + respData.lastname, 'Cerrar', 3000);
        },
        error: (errorData) => {
          this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
        },
        complete:()=>{
         this.getResponsibles();
        }
      });
    }
  };

  updateOrganizationForm = this.formBuilder.group({
    id:[0],
    organizationName: ['', Validators.required],
    organizationNumber: [0, Validators.required],
    maxRotation: [0, Validators.required],
    maxAmount: [0, Validators.required],
    responsibleId:[0, Validators.required]

  });

  onUpdateOrganizationShow():void{
    this.updateOrganizationForm.patchValue({
      id:this.organizationUpdateDto.id,
      organizationName:this.organizationUpdateDto.organizationName,
      organizationNumber:this.organizationUpdateDto.organizationNumber,
      maxRotation:this.organizationUpdateDto.maxRotation,
      maxAmount:this.organizationUpdateDto.maxAmount,
      responsibleId:this.organizationUpdateDto.responsibleId

    });
  }

  openDialogUpdateOrganization(id:number,template:TemplateRef<any>){
    this.getOrganizationbyId(id);
    this.getResponsibles();
    this.matDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.matDialogRef.afterClosed().subscribe();
    this.updateOrganizationForm.reset();
  }

  create():void{
    this.matDialogRef.close();
    this.updateOrganizationForm.reset();
   }

   updateOrganization():void{
    if(this.updateOrganizationForm.valid){
      this.updateOrganizationDto = Object.assign(this.updateOrganizationDto,this.updateOrganizationForm.value);
      this.organizationService.updateOrganization(this.updateOrganizationDto).subscribe({
        next:(orgData)=>{
          this.snackBarService.openSnackBar('Se actializo la organization: ' + orgData.organizationName,'Cerrar',3000);
        },
        error:(errorData)=>{
          this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
        }
      });
    }
   }


  getOrganizations(){
    this.organizationService.getAllOrganizations().subscribe({
      next:(orgsData)=>{
        this.organizationsDto = orgsData;
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      }
    });
  };

getResponsibles(){
  this.organizationService.getAllResponsibles().subscribe({
    next:(responsiblesData)=>{
      this.responsiblesDto = responsiblesData;
      responsiblesData.map(res =>{
        res.name = res.name + ' ' + res.lastname;
      });
      this.responsiblesOrgs = responsiblesData;
    },
    error:(errorData)=>{
      this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
    }
  })
}

getOrganizationbyId(id:number):void{
  this.organizationService.getOrganizationById(id).subscribe({
    next:(orgData)=>{
      this.organizationUpdateDto = orgData;
    },
    error:(errorData)=>{
      this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
    }
  });
}

}
