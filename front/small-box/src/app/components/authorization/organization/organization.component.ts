import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
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
  selectedUserId!: number;
  organizationsId: Array<number> = new Array<number>;
  organizationsDto: OrganizationDto[] = [];
  orgsSelected: OrganizationDto[] = [];
  organizationDto!:OrganizationDto;
 
  constructor(private appUserService: AppUserService, private organizationService: OrganizationService,
    private snackBarService: SnackBarService, private cookieService: CookieStorageService,private router:Router) { }

  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      if(event instanceof NavigationEnd){
        this.organizationsId = [];
        this.orgsSelected = [];
        this.userSelected = new AppUserDto();
      }
    })
    this.getUsers();
    this.getOrganizations();
  }

  assignOrganizationToUser(orgId: number) {
    if(this.selectedUserId==null){
      this.snackBarService.openSnackBar('Debe seleccionar un usuario','Close',3000);
    }else{
      var checkId = this.organizationsId.indexOf(orgId);
     if(checkId > -1){
        this.snackBarService.openSnackBar('Esa dependencia ya fue seleccionada', 'Cerrar', 3000);
      }else{
      this.organizationsId.push(orgId);
      this.organizationsDto.filter(org => org.id == orgId)
      .map(org => this.snackBarService.openSnackBar('Dependencia agregada: ' + org.organizationName, 'Cerrar',3000));
     this.getAllOrganizationsById(this.organizationsId);
      }
    }
}

deleteOrganizationFromUser(id: number) {
  this.organizationsId.forEach((item,index) => {
    if(item == id){
      this.organizationsId.splice(index ,1);
    }
  });
  this.getAllOrganizationsById(this.organizationsId);
    this.orgsSelected.filter(org => org.id==id).map(org => this.snackBarService.openSnackBar('Eliminada: ' + org.organizationName,'Cerrar',3000));
  
  }

getAllOrganizationsById(organizationsId:Array<number>):void{
  this.organizationService.getAllOrganizationsById(organizationsId).subscribe({
    next:(orgsData)=>{
      this.orgsSelected = orgsData;
    },
    error:(errorData)=>{
      this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
    }
  })
}

  


  getUsers(): void {
    this.appUserService.getUsers().subscribe({
      next: (usersData) => {
        this.usersDto = usersData;
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar',3000);
      }
    })
  }

  getOrganizations(): void {
    this.organizationService.getAllOrganizations().subscribe({
      next: (orgData) => {
        this.organizationsDto = orgData;
      },
      error: (errorData) => {
        this.snackBarService.openSnackBar(errorData, 'Cerrar',3000);
      }
    })
  }

  selectUser(userId:number){
    this.selectedUserId = userId;
    this.userSelected = new AppUserDto();
     this.usersDto.filter(user => user.id===userId).map(user => this.userSelected = user);
     this.organizationService.getAllOrganizationsByUser(this.selectedUserId).subscribe({
      next:(orgsData)=>{
        this.orgsSelected = orgsData;
        orgsData.map(org => {
          this.organizationsId.push(org.id);
        });
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      }
    })
  }

  addOrganizationsToUser(){

    this.organizationService.addOrganizationToUser(this.selectedUserId,this.organizationsId).subscribe({
      next:(orgsData)=>{
      this.snackBarService.openSnackBar(orgsData,'Cerrar',5000)
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',5000);
      },complete:()=>{
        console.log("hola")
       this.router.navigateByUrl('/org')
      }
    });
    
  }

  getAllOrganizationsByUser(userId:number):void{
    this.organizationService.getAllOrganizationsByUser(userId).subscribe({
      next:(orgsData)=>{
        this.orgsSelected = orgsData;
      },
      error:(errorData)=>{
        this.snackBarService.openSnackBar(errorData,'Cerrar',3000);
      }
    })
  }
}
