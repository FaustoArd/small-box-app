import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { Destination } from 'src/app/models/destination';
import { OrganizationDto } from 'src/app/models/organizationDto';
import { Ref } from 'src/app/models/ref';
import { WorkTemplateDto } from 'src/app/models/workTemplateDto';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { WorkTemplateService } from 'src/app/services/work-template.service';

@Component({
  selector: 'app-manual-refer',
  templateUrl: './manual-refer.component.html',
  styleUrls: ['./manual-refer.component.css']
})
export class ManualReferComponent {


  workTemplate!: WorkTemplateDto;
  returnedWorkTemplate!: WorkTemplateDto;
  destinationsList!: Array<string>
  destinations: Array<string> = [];
  refsList!: Array<string>;
  refs: Array<string> = [];
  refPartial!: Ref;
  destinationPartial!: Destination;
  organizations: OrganizationDto[] = [];

  constructor(private workTemplateService: WorkTemplateService, private cookieService: CookieStorageService
    , private formBuilder: FormBuilder, private snackBarService: SnackBarService
    , private organizationService: OrganizationService, private router: Router) { }


    
}
