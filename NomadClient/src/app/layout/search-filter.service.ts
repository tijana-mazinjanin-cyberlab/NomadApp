import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {TokenStorage} from "../infrastructure/auth/jwt/token.service";
import {Router} from "@angular/router";
import {environment} from "../../env/env";
import {SearchFilterForm} from "./model/searchFilterForm.model";
import {AccommodationSearch} from "./model/accommodation-search.model";
import {Amenity} from "./model/amenity.model";
import {AccommodationDetails} from "../accommodation-detail-view/model/accommodationDetails.model";
import {SearchFIlterFormReservations} from "./model/searchFIlterFormReservations";
import {Reservation} from "../accommodation-detail-view/model/reservation.model";

@Injectable({
  providedIn: 'root'
})
export class SearchFilterService{

  reservations$ = new BehaviorSubject<Reservation[]>([]);
  accommodations$ = new BehaviorSubject<AccommodationSearch[]>([]);
  accommodationsFilter$ = new BehaviorSubject<AccommodationDetails[]>([]);
  searchFilterForm$ = new BehaviorSubject<SearchFilterForm>({
    city: '',
    startDate: '',
    finishDate: '',
    peopleNum:-1,
    amenities: [],
    minPrice:-1,
    maxPrice:-1,
    accommodationType:""
  });
  constructor(private http: HttpClient,
              private router: Router, private tokenStorage: TokenStorage){}

  getAllAmenities(): Observable<Amenity[]>{
    return this.http.get<Amenity[]>(environment.apiHost + "amenities");
  }
  searchReservations(searchForm: SearchFIlterFormReservations){
    let parameters: string = "";
    if(searchForm.status != ""){
      parameters += "&status=" +searchForm.status;
    }
    if(this.tokenStorage.getRole() == "GUEST"){
      return this.http.get<Reservation[]>(environment.apiHost + "reservations/search-guest/"+this.tokenStorage.getId()+"?name="
        +searchForm.name+"&minimumDate="+searchForm.startDate+"&maximumDate="+searchForm.finishDate+parameters);
    }else{
      return this.http.get<Reservation[]>(environment.apiHost + "reservations/search-host/"+this.tokenStorage.getId()+"?name="
        +searchForm.name+"&minimumDate="+searchForm.startDate+"&maximumDate="+searchForm.finishDate+parameters);
    }

  }
  getReservations(){
    if(this.tokenStorage.getRole() == "GUEST") {
      return this.http.get<Reservation[]>(`http://localhost:8080/api/reservations/with-guest/` + this.tokenStorage.getId())
    }else{
      return this.http.get<Reservation[]>(`http://localhost:8080/api/reservations/with-host/` + this.tokenStorage.getId())

    }
  }
  filterReservations(status: string){
    if(this.tokenStorage.getRole() == "GUEST") {
      return this.http.get<Reservation[]>(environment.apiHost + "reservations/filter-guest/" + this.tokenStorage.getId() + "?status=" + status);
    }else{
      return this.http.get<Reservation[]>(environment.apiHost + "reservations/filter-host/" + this.tokenStorage.getId() + "?status=" + status);
    }
  }
  search(searchForm: SearchFilterForm): Observable<AccommodationSearch[]> {
    return this.http.get<AccommodationSearch[]>(environment.apiHost + "accommodations/search-filter?city="
      +searchForm.city+"&from="+searchForm.startDate+"&to="+searchForm.finishDate+"&peopleNum="+searchForm.peopleNum);
  }
  searchFilter(searchForm: SearchFilterForm): Observable<AccommodationSearch[]> {

    let parameters: string = "";
    if(searchForm.minPrice != -1){
      parameters += "&minimumPrice=" +searchForm.minPrice;
    }
    if(searchForm.maxPrice != -1){
      parameters += "&maximumPrice=" +searchForm.maxPrice;
    }
    if(searchForm.accommodationType != ""){
      parameters += "&type=" + searchForm.accommodationType;
    }
    for (let amenity of searchForm.amenities) {
      //alert(parameters)
      parameters +="&amenity="+amenity;
    }
    //alert("filtering.."+ parameters)
    return this.http.get<AccommodationSearch[]>(environment.apiHost + "accommodations/search-filter?city="
      +searchForm.city+"&from="+searchForm.startDate+"&to="+searchForm.finishDate+"&peopleNum="+searchForm.peopleNum+parameters);
  }
  filter(searchForm: SearchFilterForm): Observable<AccommodationDetails[]> {

    let parameters: string = "";
    if(searchForm.minPrice != -1){
      parameters += "&minimumPrice=" +searchForm.minPrice;
    }
    if(searchForm.maxPrice != -1){
      parameters += "&maximumPrice=" +searchForm.maxPrice;
    }
    if(searchForm.accommodationType != ""){
      parameters += "&type=" + searchForm.accommodationType;
    }
    for (let amenity of searchForm.amenities) {
      parameters +="&amenity="+amenity;
    }
    return this.http.get<AccommodationDetails[]>(environment.apiHost + "accommodations/filter?"+searchForm.peopleNum+parameters);
  }


}
