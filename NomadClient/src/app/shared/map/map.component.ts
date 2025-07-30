import {AfterContentInit, Component, EventEmitter, Input, Output} from '@angular/core';
import * as L from 'leaflet';
import {MapService} from "./map.service";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterContentInit{
  private map:any;
  private marker: L.Marker = new L.Marker([1, 1]);
  inputLocation: string = "";
  suggestedLocations: string[] = [];

  //@ViewChild('locationsDatalist', { static: true }) locationsDatalist!: ElementRef;

  @Output() currentLocation = new EventEmitter<string> ();

  @Input() searchBox:boolean = true as boolean;

  @Input() defaultLocation: string = "";

  constructor(private mapService: MapService) {

  }

  displaySearchBox():boolean {
    return this.searchBox;
  }

  registerOnClick(): void {
      this.map.on('click', (e: any) => {
        this.map.removeLayer(this.marker);
        const coord = e.latlng;
        const lat = coord.lat;
        const lng = coord.lng;
        console.log(lat);
        console.log(lng);
        this.marker = new L.Marker([lat, lng]).addTo(this.map);
        this.mapService.reverseSearch(lat, lng).subscribe((res) => {
          this.marker.bindPopup(res.display_name).openPopup();
          this.inputLocation = res.display_name;
          console.log("tuss")
        });
      });


  }

  onLocationInputChanged(event: any) {
    this.mapService.search(this.inputLocation).subscribe({
      next: (results) => {
        results.forEach((result: { display_name: string; }) => {
          this.suggestedLocations.push(result.display_name);
        })
      },
      error: () => {},
    });
  }

  onSearchLocation() {
    this.mapService.search(this.inputLocation).subscribe({
          next: (result) => {
            if(result.length > 0) {
              this.map.removeLayer(this.marker);
              this.marker = new L.Marker([result[0].lat, result[0].lon]).addTo(this.map);
              this.marker.bindPopup(this.inputLocation).openPopup();
              this.currentLocation.emit(this.inputLocation);
            }
          },
          error: () => {},
        });
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: [45.2396, 19.8227],
      zoom: 13,
    });

    const tiles = L.tileLayer(
        'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
        {
          maxZoom: 18,
          minZoom: 3,
          attribution:
              '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
        }
    );
    tiles.addTo(this.map);

    if(this.searchBox) {
      this.registerOnClick();
    }

    if(this.defaultLocation != "") {

      this.inputLocation = this.defaultLocation;

      this.mapService.search(this.defaultLocation).subscribe({
        next: (result) => {
          if(result.length > 0) {
            this.map.removeLayer(this.marker);
            this.marker = new L.Marker([result[0].lat, result[0].lon]).addTo(this.map);
            this.marker.bindPopup(this.defaultLocation).openPopup();

            var latLngs = [ this.marker.getLatLng() ];
            var markerBounds = L.latLngBounds(latLngs);
            this.map.fitBounds(markerBounds);
          }
        },
        error: () => {},
      });
    }

  }

  ngAfterContentInit(): void {
    let DefaultIcon = L.icon({
      iconUrl: 'https://unpkg.com/leaflet@1.6.0/dist/images/marker-icon.png',
    });

    L.Marker.prototype.options.icon = DefaultIcon;
    this.initMap();


  }
}
