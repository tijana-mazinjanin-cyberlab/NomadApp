import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccommodationImagesComponent } from './accommodation-images/accommodation-images.component';
import { AccommodationDescriptionComponent } from './accommodation-description/accommodation-description.component';
import { MatCardModule } from '@angular/material/card';
import { AccommodationReservationComponent } from './accommodation-reservation/accommodation-reservation.component';
import {MatDatepickerModule} from "@angular/material/datepicker"
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatNativeDateModule } from '@angular/material/core';
import {MatSelectModule} from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { AccommodationAmenitiesComponent } from './accommodation-amenities/accommodation-amenities.component';
import { AccommodationCommentsComponent } from './accommodation-comments/accommodation-comments.component';
import {FlexLayoutModule} from "@angular/flex-layout";
import { AccommodationCommentFormComponent } from './accommodation-comment-form/accommodation-comment-form.component';
import { AccommodationDetailsComponent } from './accommodation-details/accommodation-details.component';
import { MatInputModule } from '@angular/material/input';
import { AccommodationVerificationComponent } from './accommodation-verification/accommodation-verification.component';
import { RouterModule } from '@angular/router';
import {MatChipsModule} from '@angular/material/chips';
import {AmenityModule} from "../amenity/amenity.module";
import {SharedModule} from "../shared/shared.module";
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { ReservationVerificationComponent } from './reservation-verification/reservation-verification.component';
import { GuestReservationsComponent } from './guest-reservations/guest-reservations.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AccommodationHostComponent } from './accommodation-host/accommodation-host.component';
import { ReservationComponent } from './reservation/reservation.component';
import { CommentReportsComponent } from './comment-reports/comment-reports.component';
import {ReportCommentDialogComponent} from "./report-comment-dialog/report-comment-dialog.component";



@NgModule({
  declarations: [
    AccommodationImagesComponent,
    AccommodationDescriptionComponent,
    AccommodationReservationComponent,
    AccommodationAmenitiesComponent,
    AccommodationCommentsComponent,
    AccommodationCommentFormComponent,
    AccommodationDetailsComponent,
    AccommodationVerificationComponent,
    ReservationVerificationComponent,
    GuestReservationsComponent,
    ReportCommentDialogComponent,
    AccommodationHostComponent,
    ReservationComponent,
    CommentReportsComponent
  ],
  imports: [
    CommonModule, MatCardModule, MatDatepickerModule, MatFormFieldModule, MatNativeDateModule, MatSelectModule,
    MatButtonModule, FlexLayoutModule, MatInputModule, RouterModule, MatChipsModule, FormsModule, AmenityModule,
    SharedModule, FontAwesomeModule
  ],
  exports: [
    AccommodationDetailsComponent, AccommodationImagesComponent, AccommodationVerificationComponent, ReservationVerificationComponent
  ],
  providers: [
    { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'fill' } },
  ]
})

export class AccommodationDetailViewModule { }
