import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
  userSelected!:AppUserDto;
  userId!: number;
  organizationsId: Array<number> = new Array<number>;
  organizationsDto: OrganizationDto[] = [];
  orgsSelected: OrganizationDto[] = [];
 
  constructor(private appUserService: AppUserService, private organizationService: OrganizationService,
    private snackBarService: SnackBarService, private cookieService: CookieStorageService,private router:Router) { }

  ngOnInit(): void {
    this.getUsers();
    this.getOrganizations();
  }

  assignOrganizationToUser(id: number) {
    if(this.userId==null){
      this.snackBarService.openSnackBar('Debe seleccionar un usuario','Close');
    }else{
    const index = this.organizationsId.indexOf(id);
    if(index > -1){
      this.snackBarService.openSnackBar('Esa dependencia ya ha sido asignada','Close');
    }else{
    this.organizationsId.push(id);
    this.organizationsDto.filter(org => org.id === id).map(org => this.orgsSelected.push(org));
    }
  }
  }

  deleteOrganizationFromUser(id: number) {
    const index = this.organizationsId.indexOf(id);
    if (index > -1) {
      this.organizationsId.splice(index, 1);
    }
    this.orgsSelected = this.orgsSelected.filter(org => org.id != id);
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

  selectUser(userId:number){
    this.userId = userId;
    this.userSelected = new AppUserDto();
     this.usersDto.filter(user => user.id===userId).map(user => this.userSelected = user);
  }

  addOrganizationsToUser(){

    this.organizationService.addOrganizationToUser(this.userId,this.organizationsId).subscribe({
      next:(orgsData)=>{
      this.snackBarService.openSnackBar(orgsData,'Cerrar')
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar');
      },complete:()=>{
        console.log("hola")
        this.router.navigate([this.router.url]);
      }
    });
    
  }

  getAllOrganizationsByUser(userId:number):void{
    this.organizationService.getAllOrganizationsByUser(userId).subscribe({
      next:(orgsData)=>{
        this.orgsSelected = orgsData;
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar');
      }
    })
  }
}
