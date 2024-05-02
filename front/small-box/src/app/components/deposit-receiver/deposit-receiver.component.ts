import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { DepositReceiverDto } from 'src/app/models/depositReceiverDto';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { DepositControlService } from 'src/app/services/deposit-control.service';
import { DepositReceiverService } from 'src/app/services/deposit-receiver.service';
import { DialogService } from 'src/app/services/dialog.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';

@Component({
  selector: 'app-deposit-receiver',
  templateUrl: './deposit-receiver.component.html',
  styleUrls: ['./deposit-receiver.component.css']
})
export class DepositReceiverComponent implements OnInit {

  constructor(private depositReceiverService: DepositReceiverService, private snackBar: SnackBarService
    , private organizationService: OrganizationService, private depositControlService: DepositControlService
    , private formBuilder: FormBuilder, private cookieService: CookieStorageService, private dialogService: DialogService
  ) { }

  ngOnInit(): void {
      this.getAllReceiversByOrganization();
  }

  depositReceiverDtos:DepositReceiverDto[]=[];
  getAllReceiversByOrganization(){
    const organizationId = Number(this.cookieService.getUserMainOrganizationId());
    this.depositReceiverService.findAllReceiversByOrganization(organizationId).subscribe({
      next:(receiverDatas)=>{
        this.depositReceiverDtos = receiverDatas;
      },
      error:(errorData)=>{
        this.snackBar.openSnackBar(errorData,'Cerrar',3000);
      }
    })
  }
}
