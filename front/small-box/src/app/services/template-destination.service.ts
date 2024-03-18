import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { DestinationDto } from '../models/destinationDto';

const WORK_TEMPLATE_BASE_URL = 'http://localhost:8080/api/v1/small-box/template_destination';

@Injectable({
  providedIn: 'root'
})
export class TemplateDestinationService {

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

getAllWorkTemplateDestinations():Observable<DestinationDto[]>{
  return this.http.get<DestinationDto[]>(`${WORK_TEMPLATE_BASE_URL}/all-template-destinations`,this.httpOptions)
  .pipe(catchError(this.handleError));
}

createTemplateDestination(destination:DestinationDto):Observable<string>{
  const result = destination.destination;
  return this.http.post<string>(`${WORK_TEMPLATE_BASE_URL}/create-template-destination?destination=${result}`,this.httpOptions)
  .pipe(catchError(this.handleError));
}

deleteTemplateDestinationById(id:number):Observable<string>{
  return this.http.get<string>(`${WORK_TEMPLATE_BASE_URL}/delete-template-destination/${id}`,this.httpOptions)
  .pipe(catchError(this.handleError));
}

}
