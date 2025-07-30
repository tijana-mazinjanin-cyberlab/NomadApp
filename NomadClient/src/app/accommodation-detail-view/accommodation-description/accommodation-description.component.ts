import {Component, Input} from '@angular/core';
import {AccommodationDetails} from '../model/accommodationDetails.model';

@Component({
  selector: 'app-accommodation-description',
  templateUrl: './accommodation-description.component.html',
  styleUrls: ['./accommodation-description.component.css'],

})
export class AccommodationDescriptionComponent {
  @Input() accommodation:AccommodationDetails = {} as AccommodationDetails;

}
