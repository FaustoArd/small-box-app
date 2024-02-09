import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
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
destinationsPartial!:Destination;
organizations:OrganizationDto[]=[];

  constructor(private workTemplateService:WorkTemplateService,private cookieService:CookieStorageService
    ,private formBuilder:FormBuilder,private snackBarService:SnackBarService 
    ,private organizationService:OrganizationService, private router:Router){}


ngOnInit(): void {
  this.getAllOrganizationsByUser();
    this.getDestinationsList();
}

    getDestinationsList():void{
      this.destinationsList =["Direccion de pagos", "Contaduria-Contable", "Direccion de compras", "Secretaria de administracion"];
    
    }

    addSelectedDestination(){
      this.destinationsPartial = new Destination();
      this.destinationsPartial =  Object.assign(this.destinationsPartial, this.destinationsFormBuilder.value)
      let result = this.destinations.filter(des=> des ==this.destinationsPartial.destination).toString()
      if(result===this.destinationsPartial.destination){
        this.snackBarService.openSnackBar('La dependencia ya ha sido agregada','Cerrar',3000);
      }else{
      this.destinations.push(this.destinationsPartial.destination)
      this.cookieService.setDestinationsList(this.destinations);
    console.log(this.cookieService.getDestinationsList());
      this.getDestinationsList();
      }
    }

    deleteSelectedDestination(destination:string){
      this.destinations.forEach((item,index)=>{
        if(item==destination){
          this.destinations.splice(index,1);
          this.cookieService.setDestinationsList(this.destinations);
          this.getDestinationsList();
          this.snackBarService.openSnackBar('Se elimino: ' + destination,'Cerrar',3000);
        }
      });

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
            this.cookieService.setCurrentWorkTemplateId(JSON.stringify(this.returnedWorkTemplate.id));
           this.snackBarService.openSnackBar('Listo','Cerrar',3000);
          },
          error:(errorData)=>{
            this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
          },
          complete:()=>{
            this.router.navigateByUrl('memo-show');
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
