import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { OrganizationDto } from '../models/organizationDto';
import { OrganizationResponsibleDto} from '../models/organizationResponsibleDto'
import { observableToBeFn } from 'rxjs/internal/testing/TestScheduler';

const ORGANIZATION_BASE_URL = 'http://localhost:8080/api/v1/small-box/organization';

@Injectable({
  providedIn: 'root'
})
export class OrganizationService {

  constructor(private http:HttpClient) { }


  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':'application/json'
    })
  }

  private handleError(error:HttpErrorResponse):Observable<any>{
    if(error.status===0){
      return throwError(() => new Error(error.error));
    }else if(error.status===500){
      return throwError(()=> new Error('Error en el servidor, intente nuevamente en unos segundos'))
    }
    return throwError(()=> new Error(error.error));
  }

  getAllOrganizations():Observable<OrganizationDto[]>{
    return this.http.get<OrganizationDto[]>(`${ORGANIZATION_BASE_URL}/all-orgs`,this.httpOptions).pipe(catchError(this.handleError));
  }

  getAllOrganizationsById(organizationsId:Array<number>):Observable<OrganizationDto[]>{
    return this.http.get<OrganizationDto[]>(`${ORGANIZATION_BASE_URL}/all-orgs-by-id?organizationsId=${organizationsId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  addOrganizationToUser(userId:number,organizationsId:Array<number>):Observable<string>{
    return this.http.put<string>(`${ORGANIZATION_BASE_URL}/add-organization?userId=${userId}&organizationsId=${organizationsId}`
    ,this.httpOptions).pipe(catchError(this.handleError));
   }

   getAllOrganizationsByUser(userId:number):Observable<OrganizationDto[]>{
    return this.http.get<OrganizationDto[]>(`${ORGANIZATION_BASE_URL}/all-orgs-by-user?userId=${userId}`,this.httpOptions).pipe(catchError(this.handleError));
   }

   newOrganization(organizationDto:OrganizationDto):Observable<OrganizationDto>{
    return this.http.post<OrganizationDto>(`${ORGANIZATION_BASE_URL}/new-organization`,organizationDto,this.httpOptions)
    .pipe(catchError(this.handleError));
   }

   newResponsible(responsibleDto:OrganizationResponsibleDto):Observable<OrganizationResponsibleDto>{
    return this.http.post<OrganizationResponsibleDto>(`${ORGANIZATION_BASE_URL}/new-responsible`,responsibleDto,this.httpOptions)
    .pipe(catchError(this.handleError));
   }

   getAllResponsibles():Observable<OrganizationResponsibleDto[]>{
    return this.http.get<OrganizationResponsibleDto[]>(`${ORGANIZATION_BASE_URL}/all-responsibles`,this.httpOptions)
    .pipe(catchError(this.handleError));
   }

}
