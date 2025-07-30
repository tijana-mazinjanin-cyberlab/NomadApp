import {Component, Input} from '@angular/core';
import {Reservation} from '../model/reservation.model';
import {AccommodationDetailsService} from '../accommodation-details.service';
import {AccommodationDetails} from '../model/accommodationDetails.model';
import {TokenStorage} from 'src/app/infrastructure/auth/jwt/token.service';
import {SearchFilterService} from "../../layout/search-filter.service";
import {User} from "../../account/model/user.model";
import {NotificationService} from "../../notifications/notification.service";
import {MyNotification} from "../../notifications/notification.model";

@Component({
  selector: 'app-reservation-verification',
  templateUrl: './reservation-verification.component.html',
  styleUrls: ['./reservation-verification.component.css']
})
export class ReservationVerificationComponent {
  @Input() reservations: Reservation[]

  constructor(private searchFilterService:SearchFilterService ,private service: AccommodationDetailsService, private tokenStorage: TokenStorage, private notificationService: NotificationService,){
    this.reservations = []
    this.loadReservations()
  }
  ngOnInit(){
    this.searchFilterService.reservations$.subscribe(data => {
      this.reservations = data;
      this.loadAccommodationsAndUsers()
    });
  }
  loadReservations() : void {
    this.service.getReservationsForUser(+this.tokenStorage.getId()!).subscribe({
      next: (data: Reservation[]) => {
        this.reservations = data
        this.loadAccommodationsAndUsers()
     },
      error: (_) => {console.log("Greska!")}
    })
  }
  loadAccommodationsAndUsers() : void {
    for (let res of this.reservations){
      this.service.getAccommodation(res.accommodation).subscribe({
        next: (data: AccommodationDetails) => {
          res.accommodationDetails = data;
       },
        error: (_) => {console.log("Greska!")}
      })
      this.service.getUser(res.user).subscribe({
        next: (data: User) => {
          res.userDetails = data;
        },
        error: (_) => {console.log("Greska!")}
      })
    }
  }

  accept(request: Reservation) : void{
    this.service.confirmReservation(request.id!).subscribe({
      next: (_) => {this.loadReservations()}
    });

    const notification: MyNotification = {
      "date": new Date(),
      "notificationType": "REQUEST_RESPONSE",
      "targetAppUser": request.user,
      "text": "Host responded to your request",
      "title": "Request response"
    }
    this.sendNotification(notification);
  }
  deny(request: Reservation) : void{
    this.service.rejectReservation(request.id!).subscribe({
      next: (_) => {this.loadReservations()}
    });

    const notification: MyNotification = {
      "date": new Date(),
      "notificationType": "REQUEST_RESPONSE",
      "targetAppUser": request.user,
      "text": "Host responded to your request",
      "title": "Request response"
    }
    this.sendNotification(notification);
  }

  sendNotification(notification: MyNotification) {
    // this.notificationService.addNotification(notification).subscribe({
    //   next: () => {console.log("New notification successfully send");},
    //   error: () => {console.log("Error while posting new notification! ", notification);}
    // })
      this.notificationService.addNotification(notification);
  }
}
