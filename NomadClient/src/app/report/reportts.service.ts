import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {TokenStorage} from "../infrastructure/auth/jwt/token.service";
import {Observable} from "rxjs";
import {User} from "../account/model/user.model";
import {environment} from "../../env/env";
import {AccommodationReport} from "./model/accommodation-report.model";
import {ReportModel} from "./model/report.model";

@Injectable({
  providedIn: 'root',
})

export class ReportsService{
  constructor(private httpClient: HttpClient, private tokenStorage: TokenStorage) {}

  getAccommodationsHost(): Observable<AccommodationReport[]> {
    return this.httpClient.get<AccommodationReport[]>(environment.apiHost + "accommodations/host-reports/" + this.tokenStorage.getId());
  }
  getDateRangeReport(startDate:Date, finishDate:Date): Observable<ReportModel[]> {
    return this.httpClient.get<ReportModel[]>(environment.apiHost + "reports/date-range/"
      + this.tokenStorage.getId()+"?from="+startDate.toLocaleString('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
      })+"&to="+finishDate.toLocaleString('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
      }));
  }
  generateDateRangeReport(startDate:Date, finishDate:Date): Observable<ReportModel[]> {
    return this.httpClient.get<ReportModel[]>(environment.apiHost + "reports/generate-pdf/date-range/"
      + this.tokenStorage.getId()+"?from="+startDate.toLocaleString('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
      })+"&to="+finishDate.toLocaleString('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
      }));
  }
  getMonthlyReport(accommodationId:number, year:number): Observable<ReportModel[]> {
    return this.httpClient.get<ReportModel[]>(environment.apiHost + "reports/accommodation/"+ this.tokenStorage.getId()+"/"+accommodationId+"/" + year);
  }
}
