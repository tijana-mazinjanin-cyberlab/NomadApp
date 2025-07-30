import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TokenStorage } from '../infrastructure/auth/jwt/token.service';
import { CommentReport } from './model/comment-report.model';
import { Observable } from 'rxjs';
import {environment} from "../../env/env";
@Injectable({
  providedIn: 'root'
})
export class CommentReportsService{
  constructor(private httpClient: HttpClient, private tokenStorage: TokenStorage) {}

  getReports(): Observable<CommentReport[]> {
    //return this.httpClient.get<User>(environment.apiHost + "users/1");
    return this.httpClient.get<CommentReport[]>(environment.apiHost + "comment-reports");
  }
  createReport(commentReport: CommentReport): Observable<void>{
    return this.httpClient.post<void>(environment.apiHost + "comment-reports", commentReport);
  }
  archive(id:number): Observable<ArrayBuffer>{
    return this.httpClient.put<ArrayBuffer>(environment.apiHost + "comment-reports/archive/" +id, {});
  }
  accept(id:number): Observable<ArrayBuffer>{
    return this.httpClient.put<ArrayBuffer>(environment.apiHost + "comment-reports/accept/" +id, {});
  }
}
