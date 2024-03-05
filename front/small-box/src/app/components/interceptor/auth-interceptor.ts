import { Injectable } from "@angular/core";
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable, catchError, throwError } from "rxjs";
import { CookieStorageService } from "src/app/services/cookie-storage.service";
import { Router } from "@angular/router";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    httpError!: HttpErrorResponse;
    constructor(private cookieService: CookieStorageService, private router: Router) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const token = this.cookieService.getToken();
        if (token !== '') {
            const authReq = req.clone({
                headers: req.headers.set('Authorization', 'Bearer ' + token)
            })
            return next.handle(authReq)
        }
        const authReq = req.clone({
            //headers: req.headers.set('Content-Type' ,'application/json')
           // headers: req.headers.set('Authorization', 'Bearer ' + token)
        });

        return next.handle(authReq);
    }


}
