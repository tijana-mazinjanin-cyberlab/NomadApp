import {Injectable} from '@angular/core';
import {Review} from "./model/accommodationDetails.model";
import {Reservation} from "./model/reservation.model";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private _http: HttpClient) { }

  addAccommodationComment(comment: Review){
    return this._http.post<Reservation>(`http://localhost:8080/api/accommodation-ratings`, comment)
  }
  getAccommodationComments(accommodationId: number){
    return this._http.get<Review[]>(`http://localhost:8080/api/accommodation-ratings/for-accommodation/${+accommodationId}`)
  }

  addHostComment(comment: Review){
    return this._http.post<Reservation>(`http://localhost:8080/api/host-ratings`, comment)
  }
  getHostComments(accommodationId: number){
    return this._http.get<Review[]>(`http://localhost:8080/api/host-ratings/host/${+accommodationId}`)
  }
  getComment(commentId: number){
    return this._http.get<Review>(`http://localhost:8080/api/accommodation-ratings/${+commentId}`)
  }
  canRate(accommodationId: number, userId: number){
    return this._http.get<Boolean>(`http://localhost:8080/api/accommodation-ratings/can-rate/${+accommodationId}/${+userId}`)
  }
  delete(commentId:number){
    return this._http.delete<void>(`http://localhost:8080/api/accommodation-ratings/${+commentId}`)
  }
}
