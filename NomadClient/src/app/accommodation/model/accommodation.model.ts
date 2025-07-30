import {Amenity} from "../../amenity/amenity.model";

export interface Accommodation {
  id:number;
  name: string;
  description: string;
  address: string,
  minGuests: number,
  maxGuests: number,
  amenities: Amenity[],
  images: string[],
  comments: string[],
  status: string,
}

