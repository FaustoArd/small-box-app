import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { DispatchControlDto } from '../models/dispatchControlDto';

const DISPATCH_CONTROL_BASE_URL = 'http://localhost:8080/api/v1/smallbox/dispatchs';

@Injectable({
  providedIn: 'root'
})
export class DispatchService {

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

  createDispatch(dispatch:DispatchControlDto):Observable<string>{
    return this.http.post<string>(`${DISPATCH_CONTROL_BASE_URL}/create_dispatch`,dispatch,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  findDispatchById(id:number):Observable<DispatchControlDto>{
    return this.http.get<DispatchControlDto>(`${DISPATCH_CONTROL_BASE_URL}/find_dispatch/${id}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }
  findAllDispatchsByOrganizationId(organizationId:number):Observable<DispatchControlDto[]>{
    return this.http.get<DispatchControlDto[]>(`${DISPATCH_CONTROL_BASE_URL}/find_all_dispatch_by_org?organizationId=${organizationId}`).pipe(catchError(this.handleError));
  }

  deleteDispatchById(id:number):Observable<string>{
    return this.http.delete<string>(`${DISPATCH_CONTROL_BASE_URL}/delete_dispatch/${id}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  dispatchWorkTemplate(workTemplateId:number):Observable<string>{
    return this.http.post<string>(`${DISPATCH_CONTROL_BASE_URL}/dispatch_work_template?workTemplateId=${workTemplateId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

}
