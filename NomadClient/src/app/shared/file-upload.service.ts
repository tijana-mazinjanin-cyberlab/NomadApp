import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  private server:string = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  upload(formData: FormData){
    return this.http.post<string[]>(`${this.server}/images/upload`, formData, {
      reportProgress: true,
      observe: 'events'
    });
  }
}
