import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { content } from 'html2canvas/dist/types/css/property-descriptors/content';
import { DispatchControlDto } from 'src/app/models/dispatchControlDto';
import { DispatchService } from 'src/app/services/dispatch.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { DialogTemplateComponent } from '../../dialog/dialog-template/dialog-template.component';
import { DialogService } from 'src/app/services/dialog.service';
import { DestinationDto } from 'src/app/models/destinationDto';
import { OrganizationDto } from 'src/app/models/organizationDto';
import { FileDetails } from 'src/app/models/fileDetails';
import { FileUploadService } from 'src/app/services/file-upload.service';
import { TemplateDestinationService } from 'src/app/services/template-destination.service';

@Component({
  selector: 'app-dispatch-mat',
  templateUrl: './dispatch-mat.component.html',
  styleUrls: ['./dispatch-mat.component.css']
})
export class DispatchMatComponent implements OnInit {
  constructor(private dispatchControlService: DispatchService, private formBuilder: FormBuilder
    ,private snackBarService:SnackBarService,private dialogService:DialogService,
   private fileUploadService:FileUploadService,private snackBar:SnackBarService,
   private templateDestinationService: TemplateDestinationService) { }

  ngOnInit(): void {
    
    this.dispatchTypeList = this.getTypeList();
    //this.getAllTemplateDestinationsList();
    this.getDispatchList();
    this.saveFile();
  }

  p: number = 1;
  dispatchs: Array<DispatchControlDto> = [];
  getDispatchList() {
    this.dispatchControlService.findAllDispatchsByOrganizationId(2).subscribe({
      next: (data) => {
        this.dispatchs = data;

      },
      error: (errorData) => {
        console.log(errorData);
      }
    });
  }

 

  testArray: Array<String> = [];
  saveFile() {
    this.testArray.push("hola");
    this.testArray.push("que onda")
    this.testArray.push("fe")
    var fileObj = new File([JSON.stringify(this.testArray)], 'test_file.txt');
    console.log(fileObj.stream().getReader().read());

    var reader = new FileReader();

    reader.onload = () => {
      console.log('reader', reader.result)
        ;
    }
  }

  findByExampleFormBuilder = this.formBuilder.group({
    example: ['', Validators.required],
    //filter: ['']
  });

  get example() {
    return this.findByExampleFormBuilder.controls.example;
  }

  
  findByOrgExamplePaging(orgId:number,strExample:string){
    this.dispatchControlService.findAllDispatchsByOrganizationIdExamplePaging(orgId,strExample).subscribe({
      next:(dispatchData)=>{
        this.dispatchs = dispatchData;
       },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      }
     });
   
  }


  dispatchControlsFilter: DispatchControlDto[] = [];

  filterWorkTemplateTest() {
    
    if (this.findByExampleFormBuilder.valid) {
    
      const value = Object.assign(this.findByExampleFormBuilder.value);
      console.log(value)
      this.dispatchControlsFilter = this.dispatchs;
      this.findByOrgExamplePaging(2,JSON.stringify(value));
      
      // this.dispatchControlsFilter.forEach(dp => {
       
      //   if (dp.date.toString().toLowerCase().includes(value.example.toLowerCase()) ||
      //     dp.type.toLowerCase().includes(value.example.toLowerCase()) ||
      //     dp.docNumber.toLowerCase().includes(value.example.toLowerCase()) ||
      //     dp.volumeNumber.toLowerCase().includes(value.example.toLowerCase()) ||
      //     dp.description.toLowerCase().includes(value.example.toLowerCase()) ||
      //     dp.toDependency.toLowerCase().includes(value.example.toLowerCase())) {
      //     this.dispatchs.push(dp);
          
      //   }
      // });
  }
   
  }

  destinationDto!: DestinationDto;
  destinationsDtoList: Array<DestinationDto> = [];
  organizations: Array<OrganizationDto> = [];
  dispatchControl!: DispatchControlDto;
  strResponse!: string;
  dispatchTypeList: Array<string> = [];
  orgObj!: Object;


  private matDialogRef!: MatDialogRef<DialogTemplateComponent>;
  @ViewChild('organizationSelectionTemplate') orgTemplateRef !: TemplateRef<any>

  openDialogOrganizationSelection(){
    const template = this.orgTemplateRef;
    this.matDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.matDialogRef.afterClosed().subscribe();
  }


  currentOrganizationForm = this.formBuilder.group({
    organizationId: [0, Validators.required]
  });

  get organizationId() {
    return this.currentOrganizationForm.controls.organizationId;
  }

  selectCurrentUserOrganization() {
    if (this.currentOrganizationForm.valid) {
      this.orgObj = Object.assign(this.orgObj, this.currentOrganizationForm.value);
      console.log(this.orgObj);
    } else {
      this.snackBarService.openSnackBar('Debe seleccionar una dependencia', 'Cerrar', 3000);
    }
  }

  getTypeList(): Array<string> {
    var list = ['MEMO', 'EXP', 'SUM', 'CORR EXP', 'CORR MEMO', 'COMP', 'MOD PRESUP', 'NOTA']
    return list;
  }
  getAllTemplateDestinationsList() {
    this.templateDestinationService.getAllWorkTemplateDestinations().subscribe({
      next: (templateDestinationData) => {
        this.destinationsDtoList = templateDestinationData;
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
      }
    });
  }
  dispatchCreateForm = this.formBuilder.group({
    date: ['', Validators.required],
    type: ['', Validators.required],
    docNumber: ['', Validators.required],
    volumeNumber: ['', Validators.required],
    description: ['', Validators.required],
    toDependency: ['', Validators.required],

  });

  openDispatchCreationForm(template:any){
    this.dispatchTypeList = this.getTypeList();
    this.getAllTemplateDestinationsList();
   
    this.matDialogRef = this.dialogService.openDialogCreation({
      template
    });
    this.matDialogRef.afterClosed().subscribe();
  }

  update(): void {
    this.dispatchCreateForm.reset();
    this.matDialogRef.close();
  }
  createDispatch() {

    if (this.dispatchCreateForm.valid) {
      this.dispatchControl = new DispatchControlDto();
      this.dispatchControl = Object.assign(this.dispatchControl, this.dispatchCreateForm.value);
      this.dispatchControl.organizationId = 2;
      this.dispatchControlService.createDispatch(this.dispatchControl).subscribe({
        next: (dispatchData) => {
          this.snackBarService.openSnackBar(dispatchData, 'Cerrar', 3000);
        },
        error: (errorData) => {
          this.snackBarService.openSnackBar(errorData, 'Cerrar', 3000);
        },
        complete: () => {
          this.update();
          this.getDispatchList();
        }
      });
    } else {
      this.snackBarService.openSnackBar('Algo esta mal escrito', 'Cerrar', 3000);
   
    }

  }
  file!:File;
  fileDetails!: FileDetails;
  fileUris:Array<string> = [];
  //Upload file last
  selectFile(event:any){
    console.log("select file: " + event.target.files.item(0))
    this.file = event.target.files.item(0);
  }

  dispatchList:Array<DispatchControlDto> = [];

  uploadFile(){
    this.fileUploadService.sendFileToBackEnd(this.file,2).subscribe({
      next:(receiptData) =>{
        this.dispatchList = receiptData;
       console.log(this.dispatchList)
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      },
      complete:()=>{
       this.getDispatchList();
      }
    });
   
  }
  


  get date() {
    return this.dispatchCreateForm.controls.date;
  }
  get type() {
    return this.dispatchCreateForm.controls.type;
  }

  get docNumber() {
    return this.dispatchCreateForm.controls.docNumber;
  }

  get volumeNumber() {
    return this.dispatchCreateForm.controls.volumeNumber;
  }
  get description() {
    return this.dispatchCreateForm.controls.description;
  }
  get toDependency() {
    return this.dispatchCreateForm.controls.toDependency;
  }


}
