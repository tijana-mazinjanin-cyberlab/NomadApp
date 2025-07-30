import {AccommodationDetails} from "./accommodationDetails.model";
import {User} from "../../account/model/user.model";

export interface Reservation{
    "id"? : number,
    "user" : number,
    "accommodation" : number,
    "startDate" : Date,
    "finishDate" : Date,
    "numGuests" : number,
    "status" : String,
    "accommodationDetails"? : AccommodationDetails,
    "userDetails"? : User
}
