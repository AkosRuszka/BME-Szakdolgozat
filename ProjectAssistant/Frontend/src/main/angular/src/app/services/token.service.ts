import {Injectable} from '@angular/core';
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  token = 'token';

  constructor(private cookieService: CookieService) {
  }

  public setToken(token: string) {
    this.cookieService.set(this.token, token);
  }

  public isLogged(): boolean {
    return this.cookieService.check(this.token);
  }

  public getToken() {
    return this.cookieService.get(this.token);
  }
}
