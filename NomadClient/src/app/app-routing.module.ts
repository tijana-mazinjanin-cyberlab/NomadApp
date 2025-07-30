import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "./account/login/login.component";
import {NavigationComponent} from "./layout/navigation/navigation.component";
import {RegisterComponent} from "./account/register/register.component";
import { AccommodationDetailsComponent } from './accommodation-detail-view/accommodation-details/accommodation-details.component';
import {AccommodationCardsComponent} from "./accommodation/accommodation-cards/accommodation-cards.component";
import {AuthGuard} from "./infrastructure/auth/auth.guard";
import {ProfileComponent} from "./account/profile/profile.component";
import { AccommodationVerificationComponent } from './accommodation-detail-view/accommodation-verification/accommodation-verification.component';
import {CreateAccommodationComponent} from "./accommodation/create-accommodation/create-accommodation.component";

import { ReservationVerificationComponent } from './accommodation-detail-view/reservation-verification/reservation-verification.component';
import { GuestReservationsComponent } from './accommodation-detail-view/guest-reservations/guest-reservations.component';
import {GuestGuard} from "./infrastructure/auth/guest.guard";
import {AdminGuard} from "./infrastructure/auth/admin.guard";
import {HostGuard} from "./infrastructure/auth/host.guard";
import {
  AccommodationCardHostComponent
} from "./accommodation/accommodation-card-host/accommodation-card-host.component";
import {
  AccommodationCardsHostComponent
} from "./accommodation/accommodation-cards-host/accommodation-cards-host.component";
import {FavoritesComponent} from "./accommodation/favorites/favorites.component";
import { AdminAccountsViewComponent } from './account/admin-accounts-view/admin-accounts-view.component';
import {ReservationComponent} from "./accommodation-detail-view/reservation/reservation.component";
import { CommentReportsComponent } from './accommodation-detail-view/comment-reports/comment-reports.component';
import {NotificationsComponent} from "./notifications/notifications/notifications.component";
import {CreateReportComponent} from "./report/create-report/create-report.component";

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component:RegisterComponent},
  {path: 'home', component: AccommodationCardsComponent},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  {path: 'accommodation-details/:id/:startDate/:endDate/:peopleNum', component: AccommodationDetailsComponent},
  {path: 'accommodation-details/:id', component: AccommodationDetailsComponent},
  {path: 'accommodation-verification', component: AccommodationVerificationComponent, canActivate: [AdminGuard]},
  {path: 'admin-accounts-view', component: AdminAccountsViewComponent, canActivate: [AdminGuard]},
  {path: 'accommodation-create', component: CreateAccommodationComponent, canActivate: [HostGuard]},
  {path: 'reservation-verification', component: ReservationVerificationComponent, canActivate: [HostGuard]},
  {path: 'guest-reservation', component: GuestReservationsComponent, canActivate: [GuestGuard]},
  {path: 'accommodation-create/:id', component: CreateAccommodationComponent, canActivate: [HostGuard]},
  {path: 'host-accommodations', component: AccommodationCardsHostComponent, canActivate: [HostGuard]},
  {path: 'favourites', component: FavoritesComponent, canActivate: [GuestGuard]},
  {path: 'comment-reports', component: CommentReportsComponent, canActivate: [AdminGuard]},
  {path: 'notifications', component: NotificationsComponent},
  {path: 'reservation/:id', component: ReservationComponent},
  {path: 'report', component: CreateReportComponent, canActivate: [HostGuard]}

]
@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
