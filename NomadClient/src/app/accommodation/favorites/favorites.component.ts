import {Component} from '@angular/core';
import {User} from "../../account/model/user.model";
import {AccountService} from "../../account/account.service";
import {FavouriteService} from "../favourite.service";
import {AccommodationDetails} from "../../accommodation-detail-view/model/accommodationDetails.model";
import {FavouriteAccommodation} from "../model/favouriteAccommodation.model";

@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.css']
})
export class FavoritesComponent {
  accommodations: AccommodationDetails[] = [];
  user:User = {} as User;

  constructor(private accountService: AccountService,
              private favouriteService: FavouriteService) {}

  ngOnInit(){
    this.accountService.getLoggedUser().subscribe({
      next: (data: User) => {
        this.user = data;
        console.log(this.user.id)

        this.favouriteService.getFavouritesForGuest(this.user.id).subscribe({
          next: (data: AccommodationDetails[]) => {
            //this.accommodations = data.map(fa => fa.accommodation);
            this.accommodations = data;
          },
          error: () => {console.log("Error")}
        })
      },
      error: () => { console.log("Error while reading logged user!"); }
    })
  }


}
