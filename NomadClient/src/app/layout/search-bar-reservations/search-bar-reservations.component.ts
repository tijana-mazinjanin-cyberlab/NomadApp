import {Component} from '@angular/core';
import {SnackBarComponent} from "../../shared/snack-bar/snack-bar.component";
import {SnackBarService} from "../../shared/snack-bar.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {SearchFilterService} from "../search-filter.service";
import {MapService} from "../../shared/map/map.service";
import {FormControl, FormGroup} from "@angular/forms";
import {faFilter, faLocationArrow, faPeopleGroup, faSearch} from "@fortawesome/free-solid-svg-icons";
import {SearchFIlterFormReservations} from "../model/searchFIlterFormReservations";
import {Reservation} from "../../accommodation-detail-view/model/reservation.model";
import {MatRadioChange} from '@angular/material/radio';

@Component({
  selector: 'app-search-bar-reservations',
  templateUrl: './search-bar-reservations.component.html',
  styleUrls: ['./search-bar-reservations.component.css']
})
export class SearchBarReservationsComponent {
  protected readonly faLocationArrow = faLocationArrow;
  protected readonly faPeopleGroup = faPeopleGroup;
  protected readonly faSearch = faSearch;
  protected readonly faFilter = faFilter;

  openSnackBar() {
    this._snackBar.openFromComponent(SnackBarComponent, {
      duration: 2000,
    });
    this.snackService.errorMessage$.next("Some search fields are empty")
  }

  constructor(private snackService: SnackBarService, private _snackBar: MatSnackBar,public dialog: MatDialog, private searchFilterService: SearchFilterService, private mapService: MapService) {
  }

  searchFilterForm: SearchFIlterFormReservations = {
    name: '',
    startDate: '',
    finishDate: '',
    status: ""
  };
  searchForm = new FormGroup({
    name: new FormControl(),
    startDate: new FormControl(),
    finishDate: new FormControl(),
    status:new FormControl("")
  });

  isValidForSearch():boolean{
    if(this.searchForm.value.name == '' || this.searchForm.value.startDate == null || this.searchForm.value.finishDate==null){
      return false;
    }
    return true;
  }
  radioChange(data: MatRadioChange) {
    console.log(data.value);
    if(data.value == "all"){
      this.searchFilterForm.status = "";
      if(this.isValidForSearch()){
        this.search();
      }else{
        this.searchFilterService.getReservations().subscribe({
          next: (data: Reservation[]) => {
            this.searchFilterService.reservations$.next(data);
          },
        });
      }

    }else if(this.isValidForSearch()){
      this.searchFilterForm.status = data.value||"";
      this.search();
    }else{
      this.searchFilterService.filterReservations(data.value).subscribe({
        next: (data: Reservation[]) => {
          this.searchFilterService.reservations$.next(data);
        },
      });
    }
  }

  search(): void {
    if(!this.isValidForSearch()){
      this.openSnackBar()
      return
    }

    this.searchFilterForm.name = this.searchForm.value.name;

    this.searchFilterForm.startDate = this.searchForm.value.startDate.toLocaleString('en-US', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
    });
    this.searchFilterForm.finishDate = this.searchForm.value.finishDate.toLocaleString('en-US', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
    });

    this.searchFilterService.searchReservations(this.searchFilterForm).subscribe({
      next: (data: Reservation[]) => {
        this.searchFilterService.reservations$.next(data);
      },
    });


  }
}
