import {Injectable} from "@angular/core";
import {AbstractRestService} from "../abstract.service";
import {Amenity} from "./amenity.model";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../env/env";

@Injectable({
    providedIn: 'root',
})

export class AmenityService extends AbstractRestService<Amenity>{
    constructor(private httpClient: HttpClient) {
        super(httpClient, environment.apiHost + "amenities");
    }
}
