import {Component, Input} from '@angular/core';
import {AccommodationDetails} from "../../accommodation-detail-view/model/accommodationDetails.model";

@Component({
  selector: 'app-accommodation-card-host',
  templateUrl: './accommodation-card-host.component.html',
  styleUrls: ['./accommodation-card-host.component.css']
})
export class AccommodationCardHostComponent {
  @Input() accommodation: AccommodationDetails = {} as AccommodationDetails;
}
