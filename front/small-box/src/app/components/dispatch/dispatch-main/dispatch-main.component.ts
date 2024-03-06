import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { DispatchControlDto } from 'src/app/models/dispatchControlDto';
import { OrganizationDto } from 'src/app/models/organizationDto';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { DialogService } from 'src/app/services/dialog.service';
import { DispatchService } from 'src/app/services/dispatch.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';

@Component({
  selector: 'app-dispatch-main',
  templateUrl: './dispatch-main.component.html',
  styleUrls: ['./dispatch-main.component.css']
})
export class DispatchMainComponent implements OnInit {

  organization:Array<OrganizationDto> = [];
dispatchControl!:DispatchControlDto;
dispatchs:Array<DispatchControlDto> = [];
strResponse!:string;
  constructor(private dispatchService: DispatchService, private snackBarService: SnackBarService,
    private cookieService: CookieStorageService, private dialogService: DialogService,
    private formBuilder: FormBuilder,
    private organizationService: OrganizationService) { }

    ngOnInit(): void {
        
    }

    dispatchCreateForm = this.formBuilder.group({
      date:['',Validators.required],
      type:['',Validators.required],
      number:['',Validators.required],
      volumeNumber:['',Validators.required],
      description:['',Validators.required],
      toDependency:['',Validators.required],
      organizationId:[[],Validators.required]
    });


    createDispatch(){
      if(this.dispatchCreateForm.valid){
        this.dispatchControl = new DispatchControlDto();
        this.dispatchControl = Object.assign(this.dispatchControl,this.dispatchCreateForm.value);
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
      }
    }

    getAllDispatchByUserId(){
      const userId = Number (this.cookieService.getCurrentUserId());
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

    get date(){
      return this.dispatchCreateForm.controls.date;
    }
    get type(){
      return this.dispatchCreateForm.controls.type;
    }

    get number(){
      return this.dispatchCreateForm.controls.number;
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
    
    get organizationId(){
      return this.dispatchCreateForm.controls.organizationId;
    }

}
