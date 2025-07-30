import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MapService {

  constructor(private httpClient: HttpClient) { }

    search(street: string): Observable<any> {
    return this.httpClient.get('https://nominatim.openstreetmap.org/search?format=json&q=' + street);
  }

  reverseSearch(lat: number, lon:number): Observable<any>{
    return this.httpClient.get(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}&<params>`);
  }
}
