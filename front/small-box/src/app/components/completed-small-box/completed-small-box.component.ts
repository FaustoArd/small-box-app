import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ContainerDto } from 'src/app/models/containerDto';
import { SmallBoxUnifierDto } from 'src/app/models/smallBoxUnifierDto';
import { ContainerService } from 'src/app/services/container.service';
import { SmallBoxService } from 'src/app/services/small-box.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { FormBuilder, Validators } from '@angular/forms';



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
  totalWriteCheck:string = '';



  constructor(private smallBoxService: SmallBoxService, private containerService: ContainerService,
    private cookieService: CookieStorageService, private route: ActivatedRoute, private router: Router, 
    private snackBarService:SnackBarService,private formBuilder:FormBuilder) { }


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
      this.totalWriteValueShow();
      console.log('container data' + containerData.totalWrite)
      if(containerData.totalWrite!=null){
      this.totalWriteCheck = containerData.totalWrite;
      }else{
        this.totalWriteCheck =  '';
      }
      //this.cookieService.setCurrentContainerId(JSON.stringify(containerData.id));
      this.smallBoxCreated = containerData.smallBoxCreated;
      //Delete current unified smallboxes by container id.
      this.deleteAllUnifiedSmallBoxByContainerId(Number(this.cookieService.getCurrentContainerId()));
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

  deleteAllUnifiedSmallBoxByContainerId(containerId: number): void {
    this.smallBoxService.deleteAllUnifiedSamllBoxByContainerId(containerId).subscribe();
  }

  navigateAssociates() {
    const url = this.router.serializeUrl(
      this.router.createUrlTree(['/presentation'])
    );
  
    window.open(url, '_blank');
  }

 

  totalWriteForm = this.formBuilder.group({
    totalWrite:['',Validators.required]
  });

  get totalWrite(){
    return this.totalWriteForm.controls.totalWrite;
  }

  totalWriteValueShow(){
    this.totalWriteForm.patchValue({
      totalWrite:this.container.totalWrite
    });
  }
 
//Set the total letter write
  setTotalWrite():void{
    if(this.totalWriteForm.valid){
      //const objTotalWrite = Object.assign(this.totalWriteForm.value);
      const totalWrite = this.totalWriteForm.value.totalWrite;
      //const totalWriteFixed = totalWrite?.replace('"','');
    this.containerService.setContainerTotalWrite(Number(this.cookieService.getCurrentContainerId()), JSON.stringify(totalWrite).replaceAll('"','')).subscribe({
      next:(totalData)=>{
        this.totalWriteCheck = totalData;
        this.snackBarService.openSnackBar('Se guardo el importe: ' +totalData,'Cerrar', 3000);
      },error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar', 3000);
      },
      complete:()=>{
        this.checkMaxAmount();
      }
    });
  }else{
    this.snackBarService.openSnackBar('Debe ingresar un importe en letras','Cerrar',3000);
  }
  };
  
  //check if total amount is valid
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
