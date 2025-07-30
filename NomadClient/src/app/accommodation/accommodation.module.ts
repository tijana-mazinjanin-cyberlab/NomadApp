import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccommodationCardComponent} from './accommodation-card/accommodation-card.component';
import {AccommodationCardsComponent} from './accommodation-cards/accommodation-cards.component';
import {RouterModule} from '@angular/router';
import {LayoutModule} from "../layout/layout.module";
import {CreateAccommodationComponent} from './create-accommodation/create-accommodation.component';
import {SharedModule} from "../shared/shared.module";
import {FormsModule} from "@angular/forms";
import {AccommodationDetailViewModule} from "../accommodation-detail-view/accommodation-detail-view.module";
import {AccommodationCardsHostComponent} from './accommodation-cards-host/accommodation-cards-host.component';
import {AccommodationCardHostComponent} from './accommodation-card-host/accommodation-card-host.component';
import {ImageUploadComponent} from './image-upload/image-upload.component';
import {AccommodationAvailabilityComponent} from './accommodation-availability/accommodation-availability.component';
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {FavoritesComponent} from './favorites/favorites.component';


@NgModule({
  declarations: [
    AccommodationCardComponent,
    AccommodationCardsComponent,
    CreateAccommodationComponent,
    AccommodationCardsHostComponent,
    AccommodationCardHostComponent,
    ImageUploadComponent,
    AccommodationAvailabilityComponent,
    FavoritesComponent
  ],
  imports: [
    CommonModule, RouterModule, SharedModule, FormsModule, AccommodationDetailViewModule, LayoutModule, MatDatepickerModule, MatButtonModule, MatFormFieldModule
  ],
  exports: [
    AccommodationCardComponent,
    AccommodationCardsComponent,
    CreateAccommodationComponent,
    AccommodationCardsHostComponent,
    AccommodationCardHostComponent
  ]
})
export class AccommodationModule { }
