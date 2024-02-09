import { Component, OnInit } from '@angular/core';
import { WorkTemplate } from 'src/app/models/workTemplate';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { WorkTemplateService } from 'src/app/services/work-template.service';

@Component({
  selector: 'app-memo-single-show',
  templateUrl: './memo-single-show.component.html',
  styleUrls: ['./memo-single-show.component.css']
})
export class MemoSingleShowComponent implements OnInit {


  downloaded!:boolean;

  constructor(private cookieService:CookieStorageService,private workTemplateService:WorkTemplateService,
    private snackBarService:SnackBarService){}

  workTemplate!:WorkTemplate;


ngOnInit(): void {
    this.getWorkTemplateById();
}

  captureScreen(){

  }

  getWorkTemplateById(){
    const id = this.cookieService.getCurrentWorkTemplateId();
    this.workTemplateService.findWorkTemplateById(Number(id)).subscribe({
      next:(workData)=>{
        this.workTemplate = workData;
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      }
    })
  }
}
