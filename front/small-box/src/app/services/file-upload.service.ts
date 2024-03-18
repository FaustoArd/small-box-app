import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { DispatchControlDto } from '../models/dispatchControlDto';
const PDF_STRING_BASE_URL = 'http://localhost:8080/api/v1/smallbox/dispatchs';





@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  constructor(private http:HttpClient) { }

  httpOptions = { 
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

httpOptionsForm = {
    headers: new HttpHeaders({'Content-Type': 'multipart/form-data'})
  }
  handleError(error:HttpErrorResponse):Observable<any>{
    return throwError(()=> new Error(error.error));
    
  }


  sendFileToBackEnd(file:File,organizationId:number):Observable<DispatchControlDto[]>{
    const formData: FormData = new FormData;
    formData.append('file', file);
     return this.http.post<DispatchControlDto[]>(`${PDF_STRING_BASE_URL}/upload-file?organizationId=${organizationId}`,formData).pipe(catchError(this.handleError));
   }
}
