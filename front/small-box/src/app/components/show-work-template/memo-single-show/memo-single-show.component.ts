import { Component, OnInit, ViewChild } from '@angular/core';
import { NgxCaptureService } from 'ngx-capture';
import { tap } from 'rxjs';
import { WorkTemplateDto } from 'src/app/models/workTemplateDto';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { WorkTemplateService } from 'src/app/services/work-template.service';

@Component({
  selector: 'app-memo-single-show',
  templateUrl: './memo-single-show.component.html',
  styleUrls: ['./memo-single-show.component.css']
})
export class MemoSingleShowComponent implements OnInit {

  @ViewChild ('screen', { static:true})  screen: any;

  imgBase64 = '';
  downloaded!:boolean;

  constructor(private cookieService:CookieStorageService,private workTemplateService:WorkTemplateService,
    private snackBarService:SnackBarService,private captureService:NgxCaptureService){}

  workTemplate!:WorkTemplateDto;

  




ngOnInit(): void {
    this.getWorkTemplateById();
}

captureScreen():void{
   
  this.captureService
  .getImage(document.body, true)
  .pipe(
    tap((img) => {
      console.log(img);
    }),
    tap((img) => this.captureService.downloadImage(img))
  )
  .subscribe();
    this.snackBarService.openSnackBar('Se descargo el memo!', 'Cerrar', 3000);
    this.downloaded = true;
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
