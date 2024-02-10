import { Component, OnInit } from '@angular/core';
import { WorkTemplate } from 'src/app/models/workTemplate';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { WorkTemplateService } from 'src/app/services/work-template.service';

@Component({
  selector: 'app-work-template-list',
  templateUrl: './work-template-list.component.html',
  styleUrls: ['./work-template-list.component.css']
})
export class WorkTemplateListComponent implements OnInit {

  constructor(private workTemplateService:WorkTemplateService,private cookieService:CookieStorageService
    ,private snackBarService:SnackBarService){}

    workTemplates:WorkTemplate[]=[];

    ngOnInit(): void {
        this.getAllWorkTemplatesByUserId();
    }

    getAllWorkTemplatesByUserId(){
      const userId = Number(this.cookieService.getCurrentUserId());
      console.log(userId)
      this.workTemplateService.findAllWorkTemplatesByUserId(userId).subscribe({
        next:(templateData)=>{
          this.workTemplates = templateData;
        },
        error:(errorData)=>{
          this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
        }
      });
    }

  }

