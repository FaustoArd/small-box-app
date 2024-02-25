import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
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
      homeDependency:['',Validators.required],
      decree:['',Validators.required],
      contractedName:['', Validators.required],
      contractedCountry:['', Validators.required],
      contractedDni:['',Validators.required],
      contractedAfipRank:['', Validators.required],
      contractedCuit:['',Validators.required],
      contractedAddress:['',Validators.required],
      contractedAddressFloor:['',Validators.required],
      contractedAddressDptoNumber:['',Validators.required],
      contractedCity:['',Validators.required],
      contractedCounty:['',Validators.required],
      contractedLegalAddress:['',Validators.required],
      contractedTask:['',Validators.required],
      vigencyPeriodStart:['',Validators.required],
      vigencyPeriodEnd:['',Validators.required],
      training:['',Validators.required],
      trainingHour:['',Validators.required],
      contractedPrice:['',Validators.required],
      contractedDate:['',Validators.required]

    });

    get homeDependency(){
      return this.locationContractFormBuilder.controls.homeDependency;
    }

    get decree(){
      return this.locationContractFormBuilder.controls.decree;
    }
    get contractedName(){
      return this.locationContractFormBuilder.controls.contractedName;
    }

    get contractedCountry(){
      return this.locationContractFormBuilder.controls.contractedCountry;
    }
    get contractedDni(){
      return this.locationContractFormBuilder.controls.contractedDni;
    }

}
