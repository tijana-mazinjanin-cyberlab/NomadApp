import {AccommodationDetails} from "../../accommodation-detail-view/model/accommodationDetails.model";
import {User} from "../../account/model/user.model";

export interface FavouriteAccommodation {
  id: number;
  accommodation: AccommodationDetails;
  guest: User;
}
