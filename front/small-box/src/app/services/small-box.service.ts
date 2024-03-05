import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders,HttpErrorResponse} from '@angular/common/http'
import { Observable,catchError,throwError} from 'rxjs';
import { SmallBoxDto } from '../models/smallBoxDto';
import { SmallBoxUnifierDto } from '../models/smallBoxUnifierDto';
import { ReceiptDto } from '../models/receiptDto';

const SMALL_BOX_BASE_URL = 'http://localhost:8080/api/v1/small-box/smallboxes';

const DOCUMENTAI_BASE_URL = 'http://localhost:8080/api/v1/small-box/document_ai';


@Injectable({
  providedIn: 'root'
})
export class SmallBoxService {

  constructor(private http:HttpClient) { }

  httpOptions = { 
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

 httpOptionsForm = {
    headers: new HttpHeaders({'Content-Type': 'multipart/form-data'})
  }

  httpCustomForm = {
    headers: new HttpHeaders({'security-token':'mytoken'})
  }

  handleError(error:HttpErrorResponse):Observable<any>{
    return throwError(()=> new Error(error.error));
    
  }

  getSmallBoxById(id:number):Observable<SmallBoxDto>{
    return this.http.get<SmallBoxDto>(`${SMALL_BOX_BASE_URL}/smallbox/${id}`,this.httpOptions).pipe(catchError(this.handleError));
  }

  addSmallBox(smallBox:SmallBoxDto,containerId:number):Observable<SmallBoxDto>{
    return this.http.post<SmallBoxDto>(`${SMALL_BOX_BASE_URL}/new?containerId=${containerId}`,smallBox,this.httpOptions)
    .pipe(catchError(this.handleError));;
  }

  updateSmallBox(smallBox:SmallBoxDto):Observable<string>{
    return this.http.put<string>(`${SMALL_BOX_BASE_URL}/smallBox-update`,smallBox,this.httpOptions).pipe(catchError(this.handleError));
  }

  findSmallBoxes():Observable<SmallBoxDto[]>{
    return this.http.get<SmallBoxDto>(`${SMALL_BOX_BASE_URL}/all`, this.httpOptions).pipe(catchError(this.handleError));

  }

  findSmallBoxesByContainerId(containerId:number):Observable<SmallBoxDto[]>{
    return this.http.get<SmallBoxDto[]>(`${SMALL_BOX_BASE_URL}/all-by-container?containerId=${containerId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  deleteSmallBoxById(id:number):Observable<string>{
    return this.http.delete<string>(`${SMALL_BOX_BASE_URL}/smallbox-delete/${id}`,this.httpOptions).pipe(catchError(this.handleError));
  }

  completeSmallBox(containerId:number):Observable<SmallBoxUnifierDto[]>{
    return this.http.put<SmallBoxUnifierDto[]>(`${SMALL_BOX_BASE_URL}/complete?containerId=${containerId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  getCompletedSmallBoxByContainerId(containerId:number):Observable<SmallBoxUnifierDto[]>{
    return this.http.get<SmallBoxUnifierDto>(`${SMALL_BOX_BASE_URL}/unified/${containerId}`,this.httpOptions)
    .pipe(catchError(this.handleError));
  }

  deleteAllUnifiedSamllBoxByContainerId(containerId:number):Observable<any>{
  return  this.http.delete<any>(`${SMALL_BOX_BASE_URL}/unified-all-by-container/${containerId}`,this.httpOptions).pipe(catchError(this.handleError));
  }

  checkMaxAmount(containerId:number,userId:number):Observable<string>{
    
    return this.http.get<string>(`${SMALL_BOX_BASE_URL}/check-max-amount?containerId=${containerId}&userId=${userId}`, this.httpOptions).pipe(catchError(this.handleError));
  }

  sendFileToBackEnd(file:File):Observable<ReceiptDto>{
   const formData: FormData = new FormData;
   formData.append('file', file);
    return this.http.post<ReceiptDto>(`${DOCUMENTAI_BASE_URL}/upload_file`,formData).pipe(catchError(this.handleError));
  }

  
}
