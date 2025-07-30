import {Component, Input} from '@angular/core';
import {AccommodationSearch} from "../../layout/model/accommodation-search.model";
import {AccommodationDetails} from "../../accommodation-detail-view/model/accommodationDetails.model";
import {FavouriteService} from "../favourite.service";
import {User} from "../../account/model/user.model";
import {SearchFilterForm} from "../../layout/model/searchFilterForm.model";

@Component({
  selector: 'app-accommodation-card',
  templateUrl: './accommodation-card.component.html',
  styleUrls: ['./accommodation-card.component.css']
})
export class AccommodationCardComponent {

  @Input() isSearchMode: boolean = false;
  @Input() accommodationSearch: AccommodationSearch = {} as AccommodationSearch;
  @Input() accommodation: AccommodationDetails = {} as AccommodationDetails;
  @Input() user: User|undefined;
  @Input() searchFilterForm: SearchFilterForm = {} as SearchFilterForm;

  liked: boolean = false;
  isGuest:boolean = false;

  constructor(private favouriteService: FavouriteService) {
  }

  ngOnInit(): void {
    if(this.user != undefined) {
      console.log(this.user)
      if (this.user.roles[0] == 'GUEST') {this.isGuest = true;}
    }


    if (this.isGuest) {
      this.favouriteService.isLiked(this.accommodation.id, this.user!.id).subscribe({
        next: (result: boolean) => {
          this.liked = result;
        },
        error: () => {
          console.log("Error");
        }
      })
    }

  }

  onLikeClick() {
    console.log(this.user!.id);
    this.favouriteService.likeOrDislike(this.accommodation.id, this.user!.id).subscribe({
      next: (result:boolean) => {
        this.liked = result;
      },
      error: (err) => { console.log(err); }
    })
  }
}
