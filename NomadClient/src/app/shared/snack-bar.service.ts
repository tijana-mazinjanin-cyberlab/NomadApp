import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SnackBarService{
  errorMessage$ = new BehaviorSubject<string>('');

}
