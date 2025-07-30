import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export abstract class AbstractRestService<T> {
  constructor(protected _http: HttpClient, protected actionUrl:string){
  }

  getAll():Observable<T[]> {
    return this._http.get<T[]>(this.actionUrl);
  }
  get(id:number):Observable<T> {
    return this._http.get<T>(`${this.actionUrl}/${id}`);
  }
  delete(id:number):Observable<T> {
    return this._http.delete<T>(`${this.actionUrl}/${id}`);
  }

  post(data: T): Observable<T> {
    return this._http.post<T>(this.actionUrl, data);
  }

  put(id:number, data: T): Observable<T> {
    return this._http.put<T>(`${this.actionUrl}/${id}`, data);
  }
}
