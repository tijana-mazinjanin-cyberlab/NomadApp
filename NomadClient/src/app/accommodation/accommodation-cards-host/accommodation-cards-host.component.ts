import {Component} from '@angular/core';
import {AccommodationService} from "../accommodation.service";
import {AccommodationDetails} from "../../accommodation-detail-view/model/accommodationDetails.model";
import {TokenStorage} from "../../infrastructure/auth/jwt/token.service";

@Component({
  selector: 'app-accommodation-cards-host',
  templateUrl: './accommodation-cards-host.component.html',
  styleUrls: ['./accommodation-cards-host.component.css']
})
export class AccommodationCardsHostComponent {
  accommodations: AccommodationDetails[] = [];

  constructor(private service: AccommodationService, private tokenStorage: TokenStorage) {
  }

  ngOnInit(): void {

    this.service.getAccommodationsForHost(+this.tokenStorage.getId()!).subscribe({
      next: (data: AccommodationDetails[]) => { console.log(this.tokenStorage.getId());  this.accommodations = data; console.log(this.accommodations)},
      error: () => { console.log("Error while reading accommodations!"); }
    })
  }
}
