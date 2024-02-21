import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { LocationContractDto } from 'src/app/models/locationContractDto';
import { LocationContractService } from 'src/app/services/location-contract.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';

@Component({
  selector: 'app-location-contract',
  templateUrl: './location-contract.component.html',
  styleUrls: ['./location-contract.component.css']
})
export class LocationContractComponent {

locationContract!:LocationContractDto;

  constructor(private locationContractService:LocationContractService,private snackBarService:SnackBarService,
    private formBuilder:FormBuilder){}

    locationContractFormBuilder = this.formBuilder.group({
      
    })

}
