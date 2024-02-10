import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { WorkTemplate } from '../models/workTemplate';

const WORK_TEMPLATE_BASE_URL = 'http://localhost:8080/api/v1/small-box/work-templates';

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
    return throwError(()=> new Error(error.error));
  }

  createWorkTemplate(workTeamplate:WorkTemplate):Observable<WorkTemplate>{
    return this.http.post<WorkTemplate>(`${WORK_TEMPLATE_BASE_URL}/create`,workTeamplate,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  findWorkTemplateById(id:number):Observable<WorkTemplate>{
    return this.http.get<WorkTemplate>(`${WORK_TEMPLATE_BASE_URL}/by_id/${id}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  findAllWorkTemplatesByUserId(userId:number):Observable<WorkTemplate[]>{
    return this.http.get<WorkTemplate[]>(`${WORK_TEMPLATE_BASE_URL}/by_user_id/${userId}`,this.httpOptions).pipe(catchError(this.handleError));
  }
}
