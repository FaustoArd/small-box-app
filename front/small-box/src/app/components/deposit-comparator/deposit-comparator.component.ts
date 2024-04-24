import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { DepositItemComparatorDto } from 'src/app/models/depositItemComparatorDto';
import { FileDetails } from 'src/app/models/fileDetails';
import { ConfirmDialogService } from 'src/app/services/confirm-dialog.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { DepositControlService } from 'src/app/services/deposit-control.service';
import { DialogService } from 'src/app/services/dialog.service';
import { FileUploadService } from 'src/app/services/file-upload.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';

@Component({
  selector: 'app-deposit-comparator',
  templateUrl: './deposit-comparator.component.html',
  styleUrls: ['./deposit-comparator.component.css']
})
export class DepositComparatorComponent {

  constructor(private dialogService: DialogService,
    private fileUploadService: FileUploadService, private snackBar: SnackBarService
    , private depositControlService: DepositControlService, private cookieService: CookieStorageService,
    private confirmDialogService: ConfirmDialogService, private formBuilder: FormBuilder
    , private router: Router
  ) { }

  file!: File;
  fileDetails!: FileDetails;
  fileUris: Array<string> = [];
  selectFile(event: any) {
    console.log("select file: " + event.target.files.item(0))
    this.file = event.target.files.item(0);
  }

  depositComparators!:DepositItemComparatorDto[];
  uploadDepositExcelFile():void{
    console.log("Hola 1")
    const orgId = Number(this.cookieService.getUserMainOrganizationId());
    this.fileUploadService.sendDepositItemExcelFileToBackEnd(this.file,orgId).subscribe({
      next:(comparatorDatas)=>{
        this.depositComparators = comparatorDatas;
        
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      },
      complete:()=>{
        this.depositComparators.map(m => console.log(m.excelItemDto))
      }
    });
  }
}
