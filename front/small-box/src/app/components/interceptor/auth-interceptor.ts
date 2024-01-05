import { Injectable, Injectable } from "@angular/core";
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable } from "rxjs";
import { CookieStorageService } from "src/app/services/cookie-storage.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor{

    constructor(private cookieStorage:CookieStorageService){}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>{
    
       
    }
}
