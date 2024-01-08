import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders,HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { ContainerDto } from '../models/containerDto';
import { InputDto } from '../models/inputDto';
import { SmallBoxTypeDto } from '../models/smallBoxTypeDto';
import { Router } from '@angular/router';
import { SnackBarService } from './snack-bar.service';

const CONTAINER_BASE_URL = 'http://localhost:8080/api/v1/small-box/containers'

@Injectable({
  providedIn: 'root'
})
export class ContainerService {

  constructor(private http:HttpClient,private snackBar:SnackBarService) { }

  httpOptions = { 
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

  handleError(error:HttpErrorResponse):Observable<any>{
    if(error.status===500){
       throw catchError(error.error)
    }else if(error.status===404){
      throw catchError(error.error)
    }else if(error.status===401){
      return throwError(() => error.status);
   
    }
    return throwError(() => new Error('Error en el servidor...'));
  }

  createContainer(container:ContainerDto):Observable<ContainerDto>{
    return this.http.post<ContainerDto>(`${CONTAINER_BASE_URL}/`,container,this.httpOptions).pipe(catchError(this.handleError));
  }
  
  getContainerById(id:number):Observable<ContainerDto>{
    return this.http.get<ContainerDto>(`${CONTAINER_BASE_URL}/${id}`,this.httpOptions).pipe(catchError(this.handleError));
  }

  getAllContainersByOrganizationsbyUser(userId:number):Observable<ContainerDto[]>{
    return this.http.get<ContainerDto[]>(`${CONTAINER_BASE_URL}/all-by-organizations-by-user?userId=${userId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  getAllContainers():Observable<ContainerDto[]>{
    return this.http.get<ContainerDto[]>(`${CONTAINER_BASE_URL}/all`,this.httpOptions).pipe(catchError(this.handleError));
  }

  getAllSmallBoxesTypes():Observable<SmallBoxTypeDto[]>{
    return this.http.get<SmallBoxTypeDto[]>(`${CONTAINER_BASE_URL}/all-types`,this.httpOptions).pipe(catchError(this.handleError));
  }

  setContainerTotalWrite(containerId:number,totalWrite:string):Observable<string>{
    return this.http.put<string>(`${CONTAINER_BASE_URL}/set-total-write?containerId=${containerId}&totalWrite=${totalWrite}`, this.httpOptions)
    .pipe(catchError(this.handleError));
  }


}
