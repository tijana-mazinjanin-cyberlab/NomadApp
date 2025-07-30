import {HttpClient} from "@angular/common/http";
import {environment} from "../../env/env";
import {Injectable} from "@angular/core";
import {AbstractRestService} from "../abstract.service";
import {AccommodationDetails} from "../accommodation-detail-view/model/accommodationDetails.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root',
})

export class AccommodationService extends AbstractRestService<AccommodationDetails>{
  constructor(private httpClient: HttpClient) {
    super(httpClient, environment.apiHost + "accommodations");
  }

  getVerifiedAccommodations(): Observable<AccommodationDetails[]> {
    return this.httpClient.get<AccommodationDetails[]>(`${this.actionUrl}/verified`);
  }

  getAccommodationsForHost(hostId:number): Observable<AccommodationDetails[]> {
    return this.httpClient.get<AccommodationDetails[]>(`${this.actionUrl}/host/${+hostId}`);
  }
}
