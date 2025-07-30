import {HttpClient} from "@angular/common/http";
import {AccommodationDetails, Review} from "./model/accommodationDetails.model";
import {environment} from "../../env/env";
import {Injectable} from "@angular/core";
import {AbstractRestService} from "../abstract.service";
import {AccommodationVerificationRequest} from "./model/accommodationVerificationRequest.model";
import {Observable} from "rxjs";
import { Reservation } from "./model/reservation.model";
import { CommentReport } from "./model/commentReport.model";

import {User} from "../account/model/user.model";
import {PriceDateRange} from "./model/priceDateRange.model";
import {DateRangeModel} from "./model/dateRange.model";

@Injectable({
  providedIn: 'root',
})

export class AccommodationDetailsService extends AbstractRestService<AccommodationDetails>{
  constructor(private httpClient: HttpClient) {
    super(httpClient, environment.apiHost + "accommodations");
  }
  getRequests():Observable<AccommodationVerificationRequest[]> {
    return this._http.get<AccommodationVerificationRequest[]>(`${this.actionUrl}`);
  }
  getUnverifiedRequests():Observable<AccommodationVerificationRequest[]> {
    return this._http.get<AccommodationVerificationRequest[]>(`${this.actionUrl}/unverified`);
  }
  getTakenDates(id:number|undefined):Observable<string[]>{
    if(id == undefined){return new Observable<string[]>;}
    return this._http.get<string[]>(`${this.actionUrl}/taken-dates/${+id}`, {})
  }
  accept(id:number):Observable<AccommodationVerificationRequest>{
    return this._http.put<AccommodationVerificationRequest>(`${this.actionUrl}/verify/${+id}`, {})
  }
  decline(id:number):Observable<AccommodationVerificationRequest>{
    return this._http.put<AccommodationVerificationRequest>(`${this.actionUrl}/decline/${+id}`, {})
  }
  getPrice(id:number, date:String){
    return this._http.get<number>(`${this.actionUrl}/price/${+id}/${date}`);
  }

  setPriceInterval(accommodationId:number, body:PriceDateRange){
    return this._http.post<string>(`${this.actionUrl}/price/${+accommodationId}`, body);
  }

  setUnavailableForInterval(accommodationId:number, body:DateRangeModel){
    return this._http.post<string>(`${this.actionUrl}/unavailable/${+accommodationId}`, body);
  }
  setAvailableForInterval(accommodationId:number, body:DateRangeModel){
    return this._http.post<string>(`${this.actionUrl}/available/${+accommodationId}`, body);
  }

  reserve(reservation: {
    numGuests: number;
    accommodation: number;
    finishDate: Date;
    user: number;
    startDate: Date;
    status: string
  }){
    return this._http.post<Reservation>(`http://localhost:8080/api/reservations`, reservation)
  }
  getReservationsForUser(id:number){
    return this._http.get<Reservation[]>(`http://localhost:8080/api/reservations/with-host/${+id}`)
  }
  getAccommodation(id:number){
    return this._http.get<AccommodationDetails>(`http://localhost:8080/api/accommodations/${+id}`)
  }
  getUser(id:number){
    return this._http.get<User>(`http://localhost:8080/api/users/${+id}`)
  }
  getReservationsForGuest(id:number){
    return this._http.get<Reservation[]>(`http://localhost:8080/api/reservations/with-guest/${+id}`)
  }

  getReservation(id: number) {
    return this._http.get<Reservation>(`http://localhost:8080/api/reservations/${+id}`)
  }
  confirmReservation(id:number){
    return this._http.put<number>(`http://localhost:8080/api/reservations/confirm/${+id}`, {})
  }
  rejectReservation(id:number){
    return this._http.put<number>(`http://localhost:8080/api/reservations/reject/${+id}`, {})
  }
  deleteReservation(id: number){
    return this._http.delete<number>(`http://localhost:8080/api/reservations/${+id}`, {})
  }
  cancelReservation(id: number){
    return this._http.put<number>(`http://localhost:8080/api/reservations/cancel/${+id}`, {})
  }
  addCommentReport(report: CommentReport){
    return this._http.post<Review>(`http://localhost:8080/api/comment-reports`, report)
  }

}
