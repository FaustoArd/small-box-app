import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders,HttpErrorResponse} from '@angular/common/http'
import { Observable,catchError,throwError} from 'rxjs';
import { SmallBoxDto } from '../models/smallBoxDto';
import { SmallBoxUnifierDto } from '../models/smallBoxUnifierDto';

const SMALL_BOX_BASE_URL = 'http://localhost:8080/api/v1/small_box/smallboxes';

@Injectable({
  providedIn: 'root'
})
export class SmallBoxService {

  constructor(private http:HttpClient) { }

  httpOptions = { 
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

  handleError(error:HttpErrorResponse):Observable<any>{
    if(error.status===500){
       throw catchError(error.error)
    }else if(error.status===404){
      throw catchError(error.error)
    }else{
      throw catchError(error.error)
    }
  }

  addSmallBox(smallBox:SmallBoxDto,containerId:number):Observable<SmallBoxDto>{
    return this.http.post<SmallBoxDto>(`${SMALL_BOX_BASE_URL}/new?containerId=${containerId}`,smallBox,this.httpOptions)
    .pipe(catchError(this.handleError));;
  }

  findSmallBoxes():Observable<SmallBoxDto[]>{
    return this.http.get<SmallBoxDto>(`${SMALL_BOX_BASE_URL}/all`, this.httpOptions).pipe(catchError(this.handleError));

  }

  findSmallBoxesByContainerId(containerId:number):Observable<SmallBoxDto[]>{
    return this.http.get<SmallBoxDto[]>(`${SMALL_BOX_BASE_URL}/by_container?containerId=${containerId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  completeSmallBox(containerId:number):Observable<SmallBoxUnifierDto[]>{
    return this.http.put<SmallBoxUnifierDto[]>(`${SMALL_BOX_BASE_URL}/complete?containerId=${containerId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  getCompletedSmallBoxByContainerId(containerId:number):Observable<SmallBoxUnifierDto[]>{
    return this.http.get<SmallBoxUnifierDto>(`${SMALL_BOX_BASE_URL}/unified/${containerId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  
}
