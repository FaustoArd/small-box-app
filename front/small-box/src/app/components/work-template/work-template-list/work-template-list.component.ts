import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { WorkTemplateDto } from 'src/app/models/workTemplateDto';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { WorkTemplateService } from 'src/app/services/work-template.service';

@Component({
  selector: 'app-work-template-list',
  templateUrl: './work-template-list.component.html',
  styleUrls: ['./work-template-list.component.css']
})
export class WorkTemplateListComponent implements OnInit {

  

    workTemplates:WorkTemplateDto[]=[];
    filteredWorkTemplates:WorkTemplateDto[]=[];
    filters!:Array<string>;

    constructor(private workTemplateService:WorkTemplateService,private cookieService:CookieStorageService
      ,private snackBarService:SnackBarService,private formBuilder:FormBuilder,){
        
      }

    ngOnInit(): void {
        this.getAllWorkTemplatesByUserId();
        this.filters = ['date','producedBy','correspond','correspondNumber','destination','ref','text'];
    }

    getAllWorkTemplatesByUserId(){
      const userId = Number(this.cookieService.getCurrentUserId());
     this.workTemplateService.findAllWorkTemplatesByUserId(userId).subscribe({
        next:(templateData)=>{
          this.workTemplates = templateData;
        },
        error:(errorData)=>{
          this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
        }
      });
    }


    findByExampleFormBuilder = this.formBuilder.group({
      example:['', Validators.required],
      filter:['']
    });

    findWorkTemplateByExample(){
    
      if(this.findByExampleFormBuilder.valid){
       
      const value = Object.assign(this.findByExampleFormBuilder.value);
     
        if(value.filter==='date'){
          console.log('date')
          this.workTemplates =  this.workTemplates.filter(wt => wt.date.toString().includes(value.example.toLowerCase())).map(wt => wt);

        }else if(value.filter==='producedBy'){
          console.log('porducedBY')
          this.workTemplates =  this.workTemplates.filter(wt => wt.producedBy.toLowerCase().includes(value.example.toLowerCase())).map(wt => wt);

        }else if(value.filter=='correspondNumber'){
          console.log('correspondNumber')
          this.workTemplates =  this.workTemplates.filter(wt => wt.correspondNumber.toLowerCase().includes(value.example.toLowerCase())).map(wt => wt);

        }
      }
    }

    get example(){
      return this.findByExampleFormBuilder.controls.example;
    }
    
   
  }

