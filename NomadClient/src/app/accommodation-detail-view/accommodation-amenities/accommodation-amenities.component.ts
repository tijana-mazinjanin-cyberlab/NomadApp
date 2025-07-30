import {Component, Input} from '@angular/core';
import {Amenity} from "../../amenity/amenity.model";

@Component({
  selector: 'app-accommodation-amenities',
  templateUrl: './accommodation-amenities.component.html',
  styleUrls: ['./accommodation-amenities.component.css']
})
export class AccommodationAmenitiesComponent {
  @Input() amenities: Amenity[];
  constructor(){
    this.amenities = [];
  }
}
