import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ContainerDto } from 'src/app/models/containerDto';
import { SmallBoxUnifierDto } from 'src/app/models/smallBoxUnifierDto';
import { ContainerService } from 'src/app/services/container.service';
import { SmallBoxService } from 'src/app/services/small-box.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';



/**This component take charge of the completed SmallBox. in this component the user
can check the tickets for errors and write a letter total amount**/
@Component({
  selector: 'app-completed-small-box',
  templateUrl: './completed-small-box.component.html',
  styleUrls: ['./completed-small-box.component.css']
})
export class CompletedSmallBoxComponent implements OnInit {

  completedSmallBox: SmallBoxUnifierDto[] = [];
  errorData!: string;
  container!: ContainerDto;
  smallBoxCreated!: boolean;




  constructor(private smallBoxService: SmallBoxService, private containerService: ContainerService,
    private cookieService: CookieStorageService, private route: ActivatedRoute, private router: Router, 
    private snackBarService:SnackBarService) { }


  ngOnInit(): void {
    const containerId = Number(this.cookieService.getCurrentContainerId());
    this.getContainerByIdForInit(containerId);
   
 }

 //This method find the container that keep all the current smallbox
  getContainerByIdForInit(containerId:number){
  this.containerService.getContainerById(containerId).subscribe({
    next: (containerData) => {
      this.container = new ContainerDto();
      this.container = containerData;
      //this.cookieService.setCurrentContainerId(JSON.stringify(containerData.id));
      this.smallBoxCreated = containerData.smallBoxCreated;
      //Delete current unified smallboxes by container id.
      this.deleteAllUnifiedSamllBoxByContainerId(Number(this.cookieService.getCurrentContainerId()));
        this.onCompleteSmallBox();

      
    }, error: (errorData) => {
      this.errorData = errorData;
    }
  });
 }

 //This method do the smallbox calculations and show the final results.
  onCompleteSmallBox(): void {
    this.smallBoxService.completeSmallBox(Number(this.cookieService.getCurrentContainerId())).subscribe({
      next: (compData) => {
        this.completedSmallBox = [];
        this.completedSmallBox = compData;
      },
      error: (errorData) => {
        this.errorData = errorData;
      }, complete: () => {
        this.getContainerById();
      }
    });
  }
  getContainerById(): void {
    this.containerService.getContainerById(Number(this.cookieService.getCurrentContainerId())).subscribe({
      next: (containerData) => {
        this.container = containerData;
        this.smallBoxCreated = containerData.smallBoxCreated;

      }, error: (errorData) => {
        this.errorData = errorData;
      }
    });
  }

  getCompletedSmallboxByContainerId(id: number): void {
    this.smallBoxService.getCompletedSmallBoxByContainerId(id).subscribe({
      next: (completedData) => {
        this.completedSmallBox = [];
        this.completedSmallBox = completedData;
      },
      error: (erorrData) => {
        this.errorData = erorrData;
      },
      complete: () => {
        this.router.navigateByUrl("/completed");
      }
    });
  }

  deleteAllUnifiedSamllBoxByContainerId(containerId: number): void {
    this.smallBoxService.deleteAllUnifiedSamllBoxByContainerId(containerId).subscribe();
  }

  navigateAssociates() {
    const url = this.router.serializeUrl(
      this.router.createUrlTree(['/presentation'])
    );
  
    window.open(url, '_blank');
  }

 
  

  setTotalWrite(totalWrite:string):void{
    console.log(totalWrite);
    this.containerService.setContainerTotalWrite(Number(this.cookieService.getCurrentContainerId()), totalWrite).subscribe({
      next:(totalData)=>{
        this.snackBarService.openSnackBar(totalData,'Cerrar', 3000);
      },error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar', 3000);
      }
    });
  };

  checkMaxAmount():void{
    const containerId = Number(this.cookieService.getCurrentContainerId());
   // const userId = Number(this.cookieService.getCurrentUserId());
    this.smallBoxService.checkMaxAmount(containerId).subscribe({
      next:(checkData)=>{
        this.snackBarService.openSnackBar(checkData,'Cerrar',4000);
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',4000);
      },
      complete:()=>{
        this.navigateAssociates();
      }
    })
  }


}