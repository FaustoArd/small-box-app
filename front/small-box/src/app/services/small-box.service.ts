import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders,HttpErrorResponse} from '@angular/common/http'
import { Observable,catchError,throwError} from 'rxjs';
import { SmallBoxDto } from '../models/smallBoxDto';

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

  addSmallBox(smallBox:SmallBoxDto):Observable<SmallBoxDto>{
    return this.http.post<SmallBoxDto>(`${SMALL_BOX_BASE_URL}/new`,smallBox,this.httpOptions).pipe(catchError(this.handleError));;
  }

  findSmallBoxes():Observable<SmallBoxDto[]>{
    return this.http.get<SmallBoxDto>(`${SMALL_BOX_BASE_URL}/all`, this.httpOptions).pipe(catchError(this.handleError));

  }

  findSmallBoxesByContainerId(id:number):Observable<SmallBoxDto>{
    return this.http.get<SmallBoxDto>(`${SMALL_BOX_BASE_URL}/by_container`,this.httpOptions).pipe(catchError(this.handleError));
  }
}
