import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormArray, FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {FilterData} from "../model/filterData.model";
import {SearchFilterForm} from "../model/searchFilterForm.model";
import {AccommodationSearch} from "../model/accommodation-search.model";
import {SearchFilterService} from "../search-filter.service";
import {Amenity} from "../model/amenity.model";
import {AccommodationDetails} from "../../accommodation-detail-view/model/accommodationDetails.model";

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.css']
})
export class FilterComponent {
  ngOnInit() {
    this.searchFilterService.searchFilterForm$.subscribe(data => {
      this.searchFilterForm = data;
    });

    this.searchFilterService.getAllAmenities().subscribe({
      next: (data: Amenity[]) => { this.amenities = data;
        this.amenities.forEach(amenity => {
          this.amenitiesArray.push(new FormControl(false))
        })},
      error: () => { console.log("Error while reading amenities!"); }
    })

  }
  searchFilterForm: SearchFilterForm = {
    city: '',
    startDate: '',
    finishDate: '',
    peopleNum:-1,
    amenities: [],
    minPrice:-1,
    maxPrice:-1,
    accommodationType:''
  };
  constructor(private _formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<FilterComponent>,
    @Inject(MAT_DIALOG_DATA) public data: FilterData,
              private searchFilterService: SearchFilterService
  ) {

  }
  amenities: Amenity[] = [];

  filterForm = new FormGroup({
    options: new FormControl(""),
    minPrice: new FormControl(0),
    maxPrice: new FormControl(1000),
    amenities: new FormArray([])
  });
  get amenitiesArray() {
    return (this.filterForm.get('amenities') as FormArray);
  }
  getAmenityControls(): FormControl[] {
    return this.amenitiesArray.controls.map(control => control as FormControl);
  }

  isSearchEmpty(){
    if(this.searchFilterForm.peopleNum==-1){
      return true;
    }
    return false;
  }

  filter(): void {
    this.searchFilterForm.minPrice = this.filterForm.value.minPrice||0;
    this.searchFilterForm.maxPrice = this.filterForm.value.maxPrice||1000;
    this.searchFilterForm.accommodationType = this.filterForm.value.options||"";
    let iterator:number = 0;
    for (let amenity of this.filterForm.value.amenities||[]) {
      if(amenity) {
        this.searchFilterForm.amenities.push(this.amenities[iterator].id);
      }
      iterator++;
    }
    if(this.isSearchEmpty()){
      this.searchFilterService.filter(this.searchFilterForm).subscribe({
        next: (data: AccommodationDetails[]) => { this.searchFilterService.accommodationsFilter$.next(data); },
      });
    }else{
      this.searchFilterService.searchFilter(this.searchFilterForm).subscribe({
        next: (data: AccommodationSearch[]) => { this.searchFilterService.accommodations$.next(data); },
      });
    }

    this.searchFilterForm.amenities = []
  }

  formatLabel(value: number): string {
    if (value >= 1000) {
      return Math.round(value / 1000) + 'k';
    }

    return `${value}`;
  }
  onCancel(): void {
     this.dialogRef.close();
   }

  onFilter(): void {
    this.filter();
    this.dialogRef.close({
    });
  }
}

