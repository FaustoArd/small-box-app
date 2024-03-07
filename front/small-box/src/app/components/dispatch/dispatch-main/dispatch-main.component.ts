import { Component, OnInit, TemplateRef } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { DestinationDto } from 'src/app/models/destinationDto';
import { DispatchControlDto } from 'src/app/models/dispatchControlDto';
import { OrganizationDto } from 'src/app/models/organizationDto';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { DialogService } from 'src/app/services/dialog.service';
import { DispatchService } from 'src/app/services/dispatch.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { TemplateDestinationService } from 'src/app/services/template-destination.service';
import { DialogTemplateComponent } from '../../dialog/dialog-template/dialog-template.component';

@Component({
  selector: 'app-dispatch-main',
  templateUrl: './dispatch-main.component.html',
  styleUrls: ['./dispatch-main.component.css']
})
export class DispatchMainComponent implements OnInit {

  destinationDto!:DestinationDto;
  destinationsDtoList:Array<DestinationDto> = [];
  organization:Array<OrganizationDto> = [];
dispatchControl!:DispatchControlDto;
dispatchs:Array<DispatchControlDto> = [];
strResponse!:string;
dispatchTypeList:Array<string> =[];

  constructor(private dispatchService: DispatchService, private snackBarService: SnackBarService,
    private cookieService: CookieStorageService, private dialogService: DialogService,
    private formBuilder: FormBuilder,
    private organizationService: OrganizationService,private templateDestinationService:TemplateDestinationService) { }

    

    ngOnInit(): void {
       this.dispatchTypeList =  this.getTypeList();
       this.getAllTemplateDestinationsList();
    }
    getTypeList():Array<string>{
      var list = ['MEMO','EXP','SUM','CORR EXP','CORR MEMO','COMP','MOD PRESUP','NOTA']
      return list;
    }

    dispatchCreateForm = this.formBuilder.group({
      date:['',Validators.required],
      type:['',Validators.required],
      docNumber:['',Validators.required],
      volumeNumber:['',Validators.required],
      description:['',Validators.required],
      toDependency:['',Validators.required],
      
    });


    createDispatch(){
      if(this.dispatchCreateForm.valid){
        this.dispatchControl = new DispatchControlDto();
        this.dispatchControl = Object.assign(this.dispatchControl,this.dispatchCreateForm.value);
        this.dispatchControl.organizationId = 2;
        this.dispatchService.createDispatch(this.dispatchControl).subscribe({
          next:(dispatchData)=>{
            this.snackBarService.openSnackBar(dispatchData,'Cerrar',3000);
          },
          error:(errorData)=>{
            this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
          },
          complete:()=>{
            this.getAllDispatchByUserId();
          }
        });
      }else{
        this.snackBarService.openSnackBar('Algo esta mal escrito','Cerrar',3000);
      }
    }

    getAllDispatchByUserId(){
      const userId = Number (this.cookieService.getCurrentUserId());
      console.log(userId)
      this.dispatchService.findAllDispatchsByUserId(userId).subscribe({
        next:(dispatchsData)=>{
          this.dispatchs = dispatchsData;
        },
        error:(errorData)=>{
          this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
        }
      })
    }



    getAllOrganizationsByUser() {
      this.organizationService.getAllOrganizationsByUser(Number(this.cookieService.getCurrentUserId())).subscribe({
        next: (orgData) => {
          this.organization = orgData;
        },
        error: (errorData) => {
          this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
        }
      });
    }

    createDestinationFormBuilder = this.formBuilder.group({
      destination: ['', Validators.required]
    })


    get destination(){
      return this.createDestinationFormBuilder.controls.destination;
    }

    private matDialogRef!: MatDialogRef<DialogTemplateComponent>
    openDialogTemplateDestination(template: TemplateRef<any>) {
      this.matDialogRef = this.dialogService.openDialogCreation({
        template
      });
    }
    createTemplateDestinaton() {
      if (this.createDestinationFormBuilder.valid) {
        this.destinationDto = new DestinationDto();
        this.destinationDto = Object.assign(this.destinationDto, this.createDestinationFormBuilder.value);
  
        this.templateDestinationService.createTemplateDestination(this.destinationDto).subscribe({
          next: (responseData) => {
            this.snackBarService.openSnackBar('Se agrego el destino: ' + responseData, 'Cerrar', 3000);
          },
          error: (errorData) => {
            this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
          },
          complete: () => {
            this.update();
            this.getAllTemplateDestinationsList();
          }
        });
      }
    }
    update(): void {
      this.createDestinationFormBuilder.reset();
      this.matDialogRef.close();
    }

    getAllTemplateDestinationsList() {
      this.templateDestinationService.getAllWorkTemplateDestinations().subscribe({
        next: (templateDestinationData) => {
          this.destinationsDtoList = templateDestinationData;
        },
        error: (errorData) => {
          this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
        }
      });
    }



    get date(){
      return this.dispatchCreateForm.controls.date;
    }
    get type(){
      return this.dispatchCreateForm.controls.type;
    }

    get docNumber(){
      return this.dispatchCreateForm.controls.docNumber;
    }

    get volumeNumber(){
      return this.dispatchCreateForm.controls.volumeNumber;
    }
    get description(){
      return this.dispatchCreateForm.controls.description;
    }
    get toDependency(){
      return this.dispatchCreateForm.controls.toDependency;
    }
    
  

}
