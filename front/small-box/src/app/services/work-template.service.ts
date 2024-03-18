import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { WorkTemplateDto } from '../models/workTemplateDto';
import { Destination } from '../models/destination';
import { DestinationDto } from '../models/destinationDto';

const WORK_TEMPLATE_BASE_URL = 'http://localhost:8080/api/v1/smallbox/work-templates';

@Injectable({
  providedIn: 'root'
})
export class WorkTemplateService {


  
  constructor(private http:HttpClient) { }

httpOptions = {
  headers : new HttpHeaders({
    'Content-Type':'application/json'
  })
}

  handleError(error:HttpErrorResponse){
    if(error.status===403){
      return throwError(()=> new Error('Debe ser administrador para eliminar documentos'))
    }
    return throwError(()=> new Error(error.error));
  }

  createWorkTemplate(workTeamplate:WorkTemplateDto):Observable<WorkTemplateDto>{
    return this.http.post<WorkTemplateDto>(`${WORK_TEMPLATE_BASE_URL}/create`,workTeamplate,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  findWorkTemplateById(id:number):Observable<WorkTemplateDto>{
    return this.http.get<WorkTemplateDto>(`${WORK_TEMPLATE_BASE_URL}/by-id/${id}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  findAllWorkTemplatesByUserId(userId:number):Observable<WorkTemplateDto[]>{
    return this.http.get<WorkTemplateDto[]>(`${WORK_TEMPLATE_BASE_URL}/by-user-id/${userId}`,this.httpOptions).pipe(catchError(this.handleError));
  }

  
  deleteWorkTemplateById(id:number):Observable<string>{
    return this.http.delete<string>(`${WORK_TEMPLATE_BASE_URL}/delete-work-template-by-id/${id}`)
    .pipe(catchError(this.handleError));
  }

}
