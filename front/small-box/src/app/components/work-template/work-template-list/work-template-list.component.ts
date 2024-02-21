import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Observable, startWith, switchMap } from 'rxjs';
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



  workTemplates: WorkTemplateDto[] = [];
  workTemplate!:WorkTemplateDto;
  workTemplateCopy!:WorkTemplateDto;
  workTemplatesFilter: WorkTemplateDto[] = [];
  filteredWorkTemplates: WorkTemplateDto[] = [];
  filters!: Array<string>;
  myArrayObservable$!: Observable<WorkTemplateDto>;
  obj!: object;

  constructor(private workTemplateService: WorkTemplateService, private cookieService: CookieStorageService
    , private snackBarService: SnackBarService, private formBuilder: FormBuilder,) {

  }

  ngOnInit(): void {
    this.getAllWorkTemplatesByUserId();
    this.filters = ['Fecha', 'Producido por', 'Corresponde', 'Numero', 'Destino', 'Ref', 'Items', 'Texto'];
  }

  getAllWorkTemplatesByUserId() {
    const userId = Number(this.cookieService.getCurrentUserId());
    this.workTemplateService.findAllWorkTemplatesByUserId(userId).subscribe({
      next: (templateData) => {
        this.workTemplates = templateData;
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }


  findByExampleFormBuilder = this.formBuilder.group({
    example: ['', Validators.required],
    //filter: ['']
  });

deleteWorkTemplateById(id:number){
  this.workTemplateService.deleteWorkTemplateById(id).subscribe({
    next:(strData)=>{
      this.snackBarService.openSnackBar(strData,'Cerrar',3000);
    },
    error:(errorData)=>{
      this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
    },
    complete:()=>{
      this.getAllWorkTemplatesByUserId();
    }
  })
}

copyWorkTemplate(id:number){
  this.workTemplateService.findWorkTemplateById(id).subscribe({
    next:(wtData)=>{
      this.workTemplateCopy = wtData;
     this.workTemplateCopy.id = 0;
      this.workTemplateService.createWorkTemplate(this.workTemplateCopy).subscribe();
    },
    error:(errorData)=>{
      this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
    },
    complete:()=>{
      this.snackBarService.openSnackBar('Se creo copia del documento: ' + this.workTemplateCopy.correspond + ' ' + 
      this.workTemplateCopy.correspondNumber,'Cerrar',3000);
      this.getAllWorkTemplatesByUserId();
    }
  })
}

  filterWorkTemplateTest() {

    if (this.findByExampleFormBuilder.valid) {
      this.workTemplatesFilter = this.workTemplates;
      this.workTemplates = [];
      const value = Object.assign(this.findByExampleFormBuilder.value);
      this.workTemplatesFilter.forEach(wt => {
        if (wt.date.toString().toLowerCase().includes(value.example.toLowerCase()) ||
          wt.producedBy.toLowerCase().includes(value.example.toLowerCase()) ||
          wt.correspond.toLowerCase().includes(value.example.toLowerCase()) ||
          wt.correspondNumber.toLowerCase().includes(value.example.toLowerCase()) ||
          wt.destinations.map(wt => wt).toString().toLowerCase().includes(value.example.toLowerCase()) ||
          wt.refs.map(ref => ref).toString().toLowerCase().includes(value.example.toLowerCase()) ||
          wt.text.toLowerCase().includes(value.example.toLowerCase()) ||
          wt.items.map(item => item).toString().toLowerCase().includes(value.example.toLowerCase())) {
          this.workTemplates.push(wt);
       
        }
      });
    } else {
      this.getAllWorkTemplatesByUserId();
    }



  }


 

  get example() {
    return this.findByExampleFormBuilder.controls.example;
  }


}

