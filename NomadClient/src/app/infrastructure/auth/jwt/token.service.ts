import {Injectable} from "@angular/core";
import {AuthResponse} from "../model/auth.response.module";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root',
})
export class TokenStorage {
  constructor() {}
  saveAccessToken(token: AuthResponse): void {
    const helper = new JwtHelperService();
    localStorage.setItem('token', token.accessToken);
    localStorage.setItem('id', helper.decodeToken(token.accessToken).id);
    localStorage.setItem('role', helper.decodeToken(token.accessToken).role[0]);
  }

  getRole() {
    return localStorage.getItem('role');
  }
  getId() {
    return localStorage.getItem('id');
  }

  clear() {
    localStorage.removeItem('role');
    localStorage.removeItem('id');
    localStorage.removeItem('token');
  }
}
