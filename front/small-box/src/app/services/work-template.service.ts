import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { WorkTemplateDto } from '../models/workTemplateDto';
import { Destination } from '../models/destination';
import { DestinationDto } from '../models/destinationDto';

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

  createWorkTemplate(workTeamplate:WorkTemplateDto):Observable<WorkTemplateDto>{
    return this.http.post<WorkTemplateDto>(`${WORK_TEMPLATE_BASE_URL}/create`,workTeamplate,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  findWorkTemplateById(id:number):Observable<WorkTemplateDto>{
    return this.http.get<WorkTemplateDto>(`${WORK_TEMPLATE_BASE_URL}/by_id/${id}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  findAllWorkTemplatesByUserId(userId:number):Observable<WorkTemplateDto[]>{
    return this.http.get<WorkTemplateDto[]>(`${WORK_TEMPLATE_BASE_URL}/by_user_id/${userId}`,this.httpOptions).pipe(catchError(this.handleError));
  }

  getAllWorkTemplateDestinations():Observable<DestinationDto[]>{
    return this.http.get<DestinationDto[]>(`${WORK_TEMPLATE_BASE_URL}/all_template_destinations`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  createTemplateDestination(destination:DestinationDto):Observable<string>{
    const result = destination.destination;
    return this.http.post<string>(`${WORK_TEMPLATE_BASE_URL}/create_template_destination?destination=${result}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  deleteTemplateDestinationById(id:number):Observable<string>{
    return this.http.get<string>(`${WORK_TEMPLATE_BASE_URL}/delete_template_destination/${id}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }
  deleteWorkTemplateById(id:number):Observable<string>{
    return this.http.delete<string>(`${WORK_TEMPLATE_BASE_URL}/delete_work_template_by_id/${id}`)
    .pipe(catchError(this.handleError));
  }

}
