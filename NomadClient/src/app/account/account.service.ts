import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../env/env";
import {User} from "./model/user.model";
import {TokenStorage} from "../infrastructure/auth/jwt/token.service";

@Injectable({
  providedIn: 'root',
})

export class AccountService{
  constructor(private httpClient: HttpClient, private tokenStorage: TokenStorage) {}

  getLoggedUser(): Observable<User> {
    //return this.httpClient.get<User>(environment.apiHost + "users/1");
    return this.httpClient.get<User>(environment.apiHost + "users/" + this.tokenStorage.getId());
  }
  getUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(environment.apiHost + "users");
  }
  deleteAccount(id: number): Observable<void> {
    return this.httpClient.delete<void>(environment.apiHost + "users/"+ id);
  }
  editUser(user: User){
    return this.httpClient.put<User>(environment.apiHost + "users/" +user.id, user)
  }
  suspendUser(userId: number){
    return this.httpClient.put<User>(environment.apiHost + "users/suspend/" +userId, {})
  }
  unSuspendUser(userId: number){
    return this.httpClient.put<User>(environment.apiHost + "users/un-suspend/" +userId, {})
  }
}
