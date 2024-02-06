import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { WorkTemplate } from 'src/app/models/workTemplate';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
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
destinations!:Array<string>
  constructor(private workTemplateService:WorkTemplateService,private cookieService:CookieStorageService
    ,private formBuilder:FormBuilder,private snackBarService:SnackBarService){}


ngOnInit(): void {
    this.getDestinationsList();
}

    getDestinationsList():void{
      this.destinationsList =["Direccion de pagos", "Contaduria-Contable", "Direccion de compras", "Secretaria de administracion"];
    
    }

    addDestination(destination:string){
      this.destinations.push(destination);
    }

    memoFormBuilder = this.formBuilder.group({
      date:['',Validators.required],
      corresponds:['', Validators.required],
      producedBy:['', Validators.required],
     text:[''],
      organizationId:[0]
    });

    destinationsFormBuilder = this.formBuilder.group({
      destinations:[this.destinations,Validators.required]
    })

    createMemo(){
      if(this.memoFormBuilder.valid){
        this.workTemplate = Object.assign(this.workTemplate,this.memoFormBuilder.value);
        this.workTemplateService.createWorkTemplate(this.workTemplate).subscribe({
          next:(memoData)=>{
            this.returnedWorkTemplate = memoData;
          },
          error:(errorData)=>{
            this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
          }
        });
      }
    }


    get date(){
      return this.memoFormBuilder.controls.date;
    }
    get corresponds(){
      return this.memoFormBuilder.controls.corresponds;
    }
    get producedBy(){
      return this.memoFormBuilder.controls.producedBy;
    }
    get destination(){
      return this.destinationsFormBuilder.controls.destinations;
    }
    
}
