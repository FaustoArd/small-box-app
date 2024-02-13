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
  filteredWorkTemplates: WorkTemplateDto[] = [];
  filters!: Array<string>;
  myArrayObservable$!: Observable<WorkTemplateDto>;

  constructor(private workTemplateService: WorkTemplateService, private cookieService: CookieStorageService
    , private snackBarService: SnackBarService, private formBuilder: FormBuilder,) {

  }

  ngOnInit(): void {
    this.getAllWorkTemplatesByUserId();
    this.filters = ['Fecha', 'Producido por', 'Corresponde', 'Numero', 'Destino', 'Ref','Items', 'Texto'];
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
    filter: ['']
  });

 

  filterWorktemplate() {
    if (this.findByExampleFormBuilder.valid) {
      const value = Object.assign(this.findByExampleFormBuilder.value);
      if (value.filter === 'Fecha') {
        console.log('date')
        this.workTemplates = this.workTemplates.filter(wt => wt.date.toString().includes(value.example.toLowerCase())).map(wt => wt);

      } else if (value.filter === 'Producido por') {
        console.log('porducedBY')
        this.workTemplates = this.workTemplates.filter(wt => wt.producedBy.toLowerCase().includes(value.example.toLowerCase())).map(wt => wt);

      }  else if (value.filter === 'Corresponde') {
        console.log('correspond')
        this.workTemplates = this.workTemplates.filter(wt => wt.correspond.toLowerCase().includes(value.example.toLowerCase())).map(wt => wt);

      } else if (value.filter == 'Numero') {
        console.log('correspondNumber')
        this.workTemplates = this.workTemplates.filter(wt => wt.correspondNumber.toLowerCase().includes(value.example.toLowerCase())).map(wt => wt);

      }else if (value.filter == 'Destino') {
        console.log('destinations')
        this.workTemplates = this.workTemplates.filter(wt => wt.destinations.map(wt => wt).toString().toLowerCase().includes(value.example.toLowerCase())).map(wt => wt);

      }
      else if (value.filter == 'Ref') {
        console.log('refs')
        this.workTemplates = this.workTemplates.filter(wt => wt.refs.map(wt => wt).toString().toLowerCase().includes(value.example.toLowerCase())).map(wt => wt);

      }
      else if (value.filter == 'Texto') {
        console.log('text')
        this.workTemplates = this.workTemplates.filter(wt => wt.text.toLowerCase().includes(value.example.toLowerCase())).map(wt => wt);

      }
      else if (value.filter == 'Items') {
        console.log('items')
        this.workTemplates = this.workTemplates.filter(wt => wt.items.map(wt => wt).toString().toLowerCase().includes(value.example.toLowerCase())).map(wt => wt);

      }
    } else {
      this.getAllWorkTemplatesByUserId();
    }
  }

  get example() {
    return this.findByExampleFormBuilder.controls.example;
  }


}

