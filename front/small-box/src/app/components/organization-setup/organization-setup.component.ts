import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { OrganizationDto } from 'src/app/models/organizationDto';
import { OrganizationResponsibleDto } from 'src/app/models/organizationResponsibleDto';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { DialogTemplateComponent } from '../dialog/dialog-template/dialog-template.component';
import { DialogService } from 'src/app/services/dialog.service';
import { ParentOrganizationDto } from 'src/app/models/parentOrganization';

@Component({
  selector: 'app-organization-setup',
  templateUrl: './organization-setup.component.html',
  styleUrls: ['./organization-setup.component.css']
})

/**This component is to create Organizations and Organization responsibles */
export class OrganizationSetupComponent implements OnInit {

  constructor(private organizationService: OrganizationService, private snackBarService: SnackBarService
    , private formBuilder: FormBuilder, private dialogService: DialogService) { }

  organizationDto!: OrganizationDto;
  updateOrganizationDto!: OrganizationDto;
  organizationUpdateDto!: OrganizationDto;
  responsibleDto!: OrganizationResponsibleDto;
  updateResponsibleDto!: OrganizationResponsibleDto;
  responsiblesOrgs: OrganizationResponsibleDto[] = [];
  organizationsDto: OrganizationDto[] = [];
  responsiblesDto: OrganizationResponsibleDto[] = [];
  private matDialogRef!: MatDialogRef<DialogTemplateComponent>;

  ngOnInit(): void {
    this.getOrganizations();
    this.getResponsibles();

  }

  //Organization Form Builder
  organizationForm = this.formBuilder.group({
    organizationName: ['', Validators.required],
    organizationNumber: ['', Validators.required],
    maxRotation: [0, Validators.required],
    maxAmount: [0, Validators.required],
    responsibleId: [0, Validators.required]
  });

  //Getters
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
  get responsible() {
    return this.organizationForm.controls.responsibleId;
  }

  //Create organization method
  newOrganization() {
    //Is form valid
    if (this.organizationForm.valid) {
      //Assign form builder to OrganizationDto class
      this.organizationDto = new OrganizationDto();
      this.organizationDto = Object.assign(this.organizationDto, this.organizationForm.value);
      //POST method recieves OrganizationDto class
      this.organizationService.newOrganization(this.organizationDto).subscribe({
        next: (orgData) => {
          this.snackBarService.openSnackBar('Se a creado la Organizacion: ' + orgData.organizationName, 'Cerrar', 3000);
        },
        error: (errorData) => {
          this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
        },
        complete: () => {
          //Get Organization List
          this.getOrganizations();
        }
      });
    }
  }

  //Update Organization form Builder
  updateOrganizationForm = this.formBuilder.group({
    id: [0],
    organizationName: ['', Validators.required],
    organizationNumber: [0, Validators.required],
    maxRotation: [0, Validators.required],
    maxAmount: [0, Validators.required],
    responsibleId: [0, Validators.required]

  });

  // this method  Patch values of the organization item to be updated in update Form.
  onUpdateOrganizationShow(org: OrganizationDto): void {

    this.updateOrganizationForm.patchValue({
      id: this.organizationUpdateDto.id,
      organizationName: this.organizationUpdateDto.organizationName,
      organizationNumber: this.organizationUpdateDto.organizationNumber,
      maxRotation: this.organizationUpdateDto.maxRotation,
      maxAmount: this.organizationUpdateDto.maxAmount,
      responsibleId: this.organizationUpdateDto.responsibleId

    });
  }

  updateOrganizationMatDialogRef!:MatDialogRef<DialogTemplateComponent>;
  // This method open the template dialog form
  openDialogUpdateOrganization(id: number, template: TemplateRef<any>) {
    //Get organization By Id from selection
    this.getOrganizationbyId(id);
    //Get responsibles to show in form
    this.getResponsibles();
    this.updateOrganizationMatDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.updateOrganizationMatDialogRef.afterClosed().subscribe();
    this.updateOrganizationForm.reset();
  }
  //this method reset the update form and close the template dialog
  create(): void {
    this.matDialogRef.close();
    this.updateOrganizationForm.reset();
  }

  //Update form method
  updateOrganization(): void {
    if (this.updateOrganizationForm.valid) {
      //Assign form to OrganizationDto class
      this.organizationUpdateDto = new OrganizationDto();
      this.organizationUpdateDto = Object.assign(this.organizationUpdateDto, this.updateOrganizationForm.value);
      //Organization service update POST method, recieves OrganizationDto class
      this.organizationService.updateOrganization(this.organizationUpdateDto).subscribe({
        //Recieves Organization values
        next: (orgData) => {
          this.snackBarService.openSnackBar('Se actializo la organizacion: ' + orgData.organizationName, 'Cerrar', 3000);
        },
        error: (errorData) => {
          this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
        }
      });
    }
  }

  //Find one organization By id.
  getOrganizationbyId(id: number): void {
    this.organizationService.getOrganizationById(id).subscribe({
      next: (orgData) => {
        this.organizationUpdateDto = orgData;
        this.onUpdateOrganizationShow(this.organizationUpdateDto);

      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
      },
    });
  }

  //Get all Organizations.
  getOrganizations() {
    this.organizationService.getAllOrganizations().subscribe({
      next: (orgsData) => {
        this.organizationsDto = orgsData;
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  };

  responsibleForm = this.formBuilder.group({
    name: ['', Validators.required],
    lastname: ['', Validators.required],


  });

  //Organization Responsible Getters
  get name() {
    return this.responsibleForm.controls.name;
  }
  get lastname() {
    return this.responsibleForm.controls.lastname;
  }


  //This method is to create new organization responsible
  newResponsible() {
    if (this.responsibleForm.valid) {
      this.responsibleDto = new OrganizationResponsibleDto();
      this.responsibleDto = Object.assign(this.responsibleDto, this.responsibleForm.value);
      //Organization Service POST method
      this.organizationService.newResponsible(this.responsibleDto).subscribe({
        next: (respData) => {
          this.snackBarService.openSnackBar('Se a creado el responsable: ' + respData.name + ' ' + respData.lastname, 'Cerrar', 3000);
        },
        error: (errorData) => {
          this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
        },
        complete: () => {
          this.getResponsibles();
        }
      });
    }
  };

  //Update Resposible form builder
  updateResponsibleForm = this.formBuilder.group({
    id: [0],
    name: ['', Validators.required],

    lastname: ['', Validators.required],

  })

  //this method patch values from responsible selected by id to update dialog Form
  updateResponsibleShow(responsible: OrganizationResponsibleDto): void {
    this.updateResponsibleForm.patchValue({
      id: this.updateResponsibleDto.id,
      name: this.updateResponsibleDto.name,
      lastname: this.updateResponsibleDto.lastname
    });
  }

  onCloseDialogUpdateResponsible() {
    this.updateResponsibleMatDialogRef.close();
  }
  updateResponsibleMatDialogRef!:MatDialogRef<DialogTemplateComponent>;
  //This method open update responsible template dialog form
  openDialogUpdateResponsible(id: number, template: TemplateRef<any>) {
    this.getResponsibleById(id);
    this.updateResponsibleMatDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.updateResponsibleMatDialogRef.afterClosed().subscribe();
    this.updateResponsibleForm.reset();

  }

  //This method update responsible to backend
  updateResponsible(): void {
    if (this.updateResponsibleForm.valid) {
      //Assign values from form to OrganizationResponsible class
      this.updateResponsibleDto = new OrganizationResponsibleDto();
      this.updateResponsibleDto = Object.assign(this.updateResponsibleDto, this.updateResponsibleForm.value);
      //Organization Service PUT method 
      this.organizationService.updateResponsible(this.updateResponsibleDto).subscribe({
        next: (respData) => {
          //Returns Responsible name and lastname
          this.snackBarService.openSnackBar('Se actualizo el responsable: ' + respData.name + ' ' + respData.lastname, 'Cerrar', 3000);
        },
        error: (errorData) => {
          this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
        },
        complete: () => {
          //Get all Organization and Responsibles
          this.getOrganizations();
          this.getResponsibles();
        }
      });
    }

  }

  //Get one responsible by id
  getResponsibleById(id: number): void {
    this.organizationService.getResponsibleById(id).subscribe({
      next: (respData) => {
        this.updateResponsibleDto = respData;
        console.log(this.updateResponsibleDto);
        this.updateResponsibleShow(this.updateResponsibleDto);
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
      }
    })

  }

  //Get all responsibles
  getResponsibles() {
    this.organizationService.getAllResponsibles().subscribe({
      next: (responsiblesData) => {
        this.responsiblesDto = responsiblesData;
        this.responsiblesOrgs = responsiblesData;
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
      },
      complete: () => {
        this.responsiblesOrgs.map(res => {
          res.name = res.name + ' ' + res.lastname;
        });

      }
    })
  }
  selectedMainOrgId!: number;
  parentOrganizationForm = this.formBuilder.group({
    mainOrganizationId: [this.selectedMainOrgId, Validators.required],
  })
  get mainOrganization() {
    return this.parentOrganizationForm.controls.mainOrganizationId;
  }

  onCloseParentOrganizationTemplate() {

    this.setParentOrganizationTemplateMatDialogRef.close();
    this.selectedParentOrganizationIds = [];
    this.selectedParentOrganizationNames = [];
  }

  private setParentOrganizationTemplateMatDialogRef!: MatDialogRef<DialogTemplateComponent>

  openSetParentOrganizationTemplate(mainOrgId: number, template: TemplateRef<any>) {
    this.selectedMainOrgId = mainOrgId;
    this.getParentOrganizationByMainOrganizationId(this.selectedMainOrgId);
    this.getOrganizatiosnByMainOrganizationId(this.selectedMainOrgId)
    this.setParentOrganizationTemplateMatDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.setParentOrganizationTemplateMatDialogRef.afterClosed().subscribe();
  }
  parentOrganizationDto!: ParentOrganizationDto;
  savedParentOrganization!: ParentOrganizationDto;
  parentOrganizationId!: number;
  setParentOrganization() {
    console.log("main org Id: " + this.selectedMainOrgId)

    this.parentOrganizationDto = new ParentOrganizationDto();
    this.parentOrganizationDto.mainOrganizationId = this.selectedMainOrgId;
    this.parentOrganizationDto.parentOrganizationIds = this.selectedParentOrganizationIds;

    this.parentOrganizationDto.id = this.parentOrganizationId;
    console.log("P ID: " + this.parentOrganizationId)
    this.organizationService.setParentOrganizations(this.parentOrganizationDto).subscribe({
      next: (parentData) => {
        this.savedParentOrganization = parentData;
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
      },
      complete: () => {
        this.onCloseParentOrganizationTemplate();
        this.openSavedParentOrganizationTemplate();
      }
    });
  }

  getParentOrganizationByMainOrganizationId(mainOrganizationId: number) {
    var result = 0;
    this.organizationService.getParentOrganizationByMainOrganizationId(mainOrganizationId).subscribe({
      next: (parentData) => {
        this.parentOrganizationId = parentData.id;
        console.log("ID::::" + this.parentOrganizationId)
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });

  }
  getOrganizatiosnByMainOrganizationId(mainOrganizationId: number) {
    this.organizationService.getOrganizationsByMainOrganizationId(mainOrganizationId).subscribe({
      next: (orgDatas) => {
        orgDatas.forEach(org => {
          this.onSelectParentOrganization(org.id);
          console.log(this.selectedParentOrganizationIds);
          console.log(this.selectedParentOrganizationNames);
        });
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }


  selectedParentOrganizationIds: number[] = [];
  selectedParentOrganizationNames: OrganizationDto[] = [];
  onSelectParentOrganization(organizationId: number) {
    const checkParent = this.selectedParentOrganizationIds.findIndex(p => p == organizationId);
    if (checkParent > -1) {
      const repeatedOrg = this.onFindOrg(organizationId);
      this.snackBarService.openSnackBar("La organizacion: " + repeatedOrg.organizationName + " ya fue seleccionada.", 'Cerrar', 3000);
    } else {
      const selectedOrg = this.onFindOrg(organizationId);
      this.selectedParentOrganizationIds.push(organizationId);
      this.selectedParentOrganizationNames.push(selectedOrg);
      this.snackBarService.openSnackBar("Selecciono la organizacion: " + selectedOrg.organizationName, 'Cerrar', 3000);
    }
  }
  onDeleteParentOrganization(organizationId: number) {
    this.selectedParentOrganizationIds.forEach((item, index) => {
      if (item == organizationId) {
        this.selectedParentOrganizationIds.splice(index, 1);
        this.selectedParentOrganizationNames.splice(index, 1);
      }
      const deletedOrg = this.onFindOrg(organizationId);
      this.snackBarService.openSnackBar("Se elimino la organizacion " + deletedOrg.organizationName, 'Cerrar', 3000);
    });
  }

  private onFindOrg(organizationId: number): OrganizationDto {
    const orgIndex = this.organizationsDto.findIndex(org => org.id == organizationId);
    return this.organizationsDto[orgIndex];
  }

  onCloseSavedParentOrganizationTemplate() {

    this.savedParentOrganizationTemplateMatDialogRef.close();
  }

  private savedParentOrganizationTemplateMatDialogRef!: MatDialogRef<DialogTemplateComponent>
  @ViewChild('savedParentOrganizationTemplate') savedParentOrganizationTemplate!: TemplateRef<any>;
  openSavedParentOrganizationTemplate() {
    const template = this.savedParentOrganizationTemplate;
    this.savedParentOrganizationTemplateMatDialogRef = this.dialogService.openSupplyCorrectionNoteCreation({
      template
    });
    this.savedParentOrganizationTemplateMatDialogRef.afterClosed().subscribe();
  }

  onCloseOrganizationListTemplate() {
    this.organizationListTemplateMatDialogRef.close();
  }

  private organizationListTemplateMatDialogRef!: MatDialogRef<DialogTemplateComponent>
  openOrganizationListTemplate(template: TemplateRef<any>) {
    this.organizationListTemplateMatDialogRef = this.dialogService.openSupplyCorrectionNoteCreation({
      template
    });
    this.organizationListTemplateMatDialogRef.afterClosed().subscribe();
  }

  onCloseResponsibleListTemplate() {
    this.responsibleListTemplateMatDialogRef.close();
  }

  private responsibleListTemplateMatDialogRef!: MatDialogRef<DialogTemplateComponent>
  openResponsibleListTemplate(template: TemplateRef<any>) {
    this.responsibleListTemplateMatDialogRef = this.dialogService.openSupplyCorrectionNoteCreation({
      template
    });
    this.responsibleListTemplateMatDialogRef.afterClosed().subscribe();
  }

}
