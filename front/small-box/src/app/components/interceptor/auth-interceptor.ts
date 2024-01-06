import { Injectable } from "@angular/core";
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable } from "rxjs";
import { CookieStorageService } from "src/app/services/cookie-storage.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor{

    constructor(private cookieService:CookieStorageService){}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>{
    
       const token = this.cookieService.getToken();
       if(token !==''){
       
        const authReq = req.clone({
            headers: req.headers.set('Authorization', 'Bearer ' + token)
        })
        return next.handle(authReq);
       }
       const authReq = req.clone({
        headers: req.headers.set('Content-Type', 'application/json')
       });
      
       return next.handle(authReq);
    }
}
