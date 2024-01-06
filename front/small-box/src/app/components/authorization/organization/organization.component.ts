import { Component, OnInit } from '@angular/core';
import { AppUserDto } from 'src/app/models/appUserDto';
import { OrganizationDto } from 'src/app/models/organizationDto';
import { AppUserService } from 'src/app/services/app-user.service';
import { AuthorizationService } from 'src/app/services/authorization.service';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';
import { OrganizationService } from 'src/app/services/organization.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';

@Component({
  selector: 'app-organization',
  templateUrl: './organization.component.html',
  styleUrls: ['./organization.component.css']
})
export class OrganizationComponent implements OnInit {

  usersDto: AppUserDto[] = [];
  userId!: number;
  organizationsId: Array<number> = new Array<number>;
  organizationsDto: OrganizationDto[] = [];

  constructor(private appUserService: AppUserService, private organizationService: OrganizationService,
    private snackBarService: SnackBarService, private cookieService: CookieStorageService) { }

  ngOnInit(): void {
    this.getUsers();
    this.getOrganizations();
  }

  assignOrganizationToUser(id: number) {
    this.organizationsId.push(id);
 
  }

  deleteOrganizationFromUser(id: number) {
    const index = this.organizationsId.indexOf(id);
    if (index > -1) {
      this.organizationsId.splice(index, 1);
     }
  }


  getUsers(): void {
    this.appUserService.getUsers().subscribe({
      next: (usersData) => {
        this.usersDto = usersData;
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar');
      }
    })
  }

  getOrganizations(): void {
    this.organizationService.getAllOrganizations().subscribe({
      next: (orgData) => {
        this.organizationsDto = orgData;
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar');
      }
    })
  }
}
