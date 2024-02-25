import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SnackBarService } from './snack-bar.service';
import { Observable, catchError, throwError } from 'rxjs';
import { LocationContractDto } from '../models/locationContractDto';

const LOCATION_CONTRACT_BASE_URL = 'http://localhost:8080/api/v1/small-box/location-contracts';

@Injectable({
  providedIn: 'root'
})
export class LocationContractService {

  constructor(private http:HttpClient,private snackBar:SnackBarService) { }

  httpOptions = { 
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

  handleError(error:HttpErrorResponse):Observable<any>{
    return throwError(() => new Error(error.error));
  
  }

  createLocationContract(locationContract:LocationContractDto):Observable<string>{
    return this.http.post<string>(`${LOCATION_CONTRACT_BASE_URL}/create`,locationContract,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  findLocationContractById(id:number):Observable<LocationContractDto>{
    return this.http.get<LocationContractDto>(`${LOCATION_CONTRACT_BASE_URL}/${id}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }
  

  findAllLocationContractsByUserId(userId:number):Observable<LocationContractDto[]>{
    return this.http.get<LocationContractDto[]>(`${LOCATION_CONTRACT_BASE_URL}/find_all_by_org_by_user_id?userId=${userId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }
}
