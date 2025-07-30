import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UserReport} from "./model/userReport.model";

@Injectable({
  providedIn: 'root'
})
export class UserReportService {

  constructor(private _http: HttpClient) { }

  reportUser(report: UserReport){
    return this._http.post<UserReport>(`http://localhost:8080/api/user_reports`, report)
  }
}
