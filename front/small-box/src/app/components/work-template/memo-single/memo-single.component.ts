import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Destination } from 'src/app/models/destination';
import { OrganizationDto } from 'src/app/models/organizationDto';
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

workTemplate!:WorkTemplate;
returnedWorkTemplate!:WorkTemplate;
destinationsList!:Array<string>
destinations:Array<string> = [];
dest!:Destination;
organizations:OrganizationDto[]=[];
  constructor(private workTemplateService:WorkTemplateService,private cookieService:CookieStorageService
    ,private formBuilder:FormBuilder,private snackBarService:SnackBarService ,private organizationService:OrganizationService){}


ngOnInit(): void {
  this.getAllOrganizationsByUser();
    this.getDestinationsList();
}

    getDestinationsList():void{
      this.destinationsList =["Direccion de pagos", "Contaduria-Contable", "Direccion de compras", "Secretaria de administracion"];
    
    }

    addDestination(){
      this.dest = new Destination();
      this.dest =  Object.assign(this.dest, this.destinationsFormBuilder.value)
      let result = this.destinations.filter(des=> des ==this.dest.destination).toString()
      console.log(result);
      if(result===this.dest.destination){
        this.snackBarService.openSnackBar('La dependencia ya ha sido agregada','Cerrar',3000);
      }else{
     
     
     this.destinations.push(this.dest.destination)
      this.cookieService.setDestinationsList(this.destinations);
    console.log(this.cookieService.getDestinationsList());
      this.getDestinationsList();
      }
    }

    memoFormBuilder = this.formBuilder.group({
      date:['',Validators.required],
      corresponds:['', Validators.required],
      text:[''],
      organizationId:[0]
    });

    destinationsFormBuilder = this.formBuilder.group({
      destination:['',Validators.required]
    })

    createMemo(){
      if(this.memoFormBuilder.valid){
        this.workTemplate = new WorkTemplate();
        this.workTemplate = Object.assign(this.workTemplate,this.memoFormBuilder.value);
        this.workTemplate.destinations = this.destinations;
        this.workTemplateService.createWorkTemplate(this.workTemplate).subscribe({
          next:(memoData)=>{
            this.returnedWorkTemplate = memoData;
            console.log(this.returnedWorkTemplate)
            this.snackBarService.openSnackBar('Listo','Cerrar',3000);
          },
          error:(errorData)=>{
            this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
          }
        });
      }
    }

    getAllOrganizationsByUser(){
      this.organizationService.getAllOrganizationsByUser(Number(this.cookieService.getCurrentUserId())).subscribe({
        next:(orgData)=>{
          this.organizations = orgData;
        },
        error:(errorData)=>{
          this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
        }
      })
    }



    get date(){
      return this.memoFormBuilder.controls.date;
    }
    get corresponds(){
      return this.memoFormBuilder.controls.corresponds;
    }
  
    get destination(){
      return this.destinationsFormBuilder.controls.destination;
    }
    
}
