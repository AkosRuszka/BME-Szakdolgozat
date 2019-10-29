import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {TokenService} from "./token.service";

@Injectable({
  providedIn: 'root'
})
export class Tokeninterceptor implements HttpInterceptor {

  constructor(public tokenService: TokenService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    var token = null;
    if (this.tokenService.isLogged()) {
      token = this.tokenService.getToken();
      return next.handle(this.addToken(req, token));
    }
    return next.handle(req);
  }

  private addToken(request: HttpRequest<any>, token: string) {
    return request.clone({
      withCredentials: true,
      setHeaders: {
        'Authorization': `Bearer ${token}`
      }
    });
  }
}
