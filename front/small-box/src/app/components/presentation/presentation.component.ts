import { Component, OnInit, ViewChild } from '@angular/core';
import { ContainerDto } from 'src/app/models/containerDto';
import { SmallBoxUnifierDto } from 'src/app/models/smallBoxUnifierDto';
import { ContainerService } from 'src/app/services/container.service';
import { SmallBoxService } from 'src/app/services/small-box.service';
import { StorageService } from 'src/app/services/storage.service';
import { NgxCaptureService } from 'ngx-capture';
import { pipe, tap } from 'rxjs';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';




@Component({
  selector: 'app-presentation',
  templateUrl: './presentation.component.html',
  styleUrls: ['./presentation.component.css']
})


export class PresentationComponent implements OnInit {

@ViewChild ('screen', { static:true})  screen: any;

imgBase64 = '';
downloaded!:boolean;

  completedSmallBox: SmallBoxUnifierDto[] = [];
  errorData!: string;
  container!: ContainerDto;
  smallBoxCreated!:boolean;

constructor(private cookieService:CookieStorageService,private containerService:ContainerService,
  private smallBoxService:SmallBoxService,private captureService:NgxCaptureService, private snackBarService:SnackBarService){}
  


ngOnInit(): void {
    this.getContainerById();
   
}

  getContainerById(): void {
    this.containerService.getContainerById(Number(this.cookieService.getCurrentContainerId())).subscribe({
      next: (containerData) => {
        this.container = new ContainerDto();
        this.container = containerData;
        this.smallBoxCreated = containerData.smallBoxCreated;
        this.getSmallBoxCompleteByContainerId(this.container.id);

       
      }, error: (errorData) => {
        this.errorData = errorData;
      }
     
    });
  }
  getSmallBoxCompleteByContainerId(containerId:number):void{
    this.smallBoxService.getCompletedSmallBoxByContainerId(containerId).subscribe({
      next:(smData)=>{
        this.completedSmallBox = smData;
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar', 3000);
      }
    })
  }

  captureScreen():void{
   
    this.captureService
    .getImage(this.screen.nativeElement, true)
    .pipe(
    
      tap((img) => this.captureService.downloadImage(img))
    )
    .subscribe();
      this.snackBarService.openSnackBar('Se descargo la caja chica!', 'Cerrar', 3000);
      this.downloaded = true;
  }

  
}
