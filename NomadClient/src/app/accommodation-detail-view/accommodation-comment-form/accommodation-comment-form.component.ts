
import { AccommodationDetailsService } from '../accommodation-details.service';
import {Component, EventEmitter, Input, Output, OnInit} from '@angular/core';
import {FloatLabelType} from '@angular/material/form-field';
import {TokenStorage} from 'src/app/infrastructure/auth/jwt/token.service';
import {CommentService} from "../comment.service";
import {NotificationService} from "../../notifications/notification.service";
import {MyNotification} from "../../notifications/notification.model";
import {AccommodationDetails} from "../model/accommodationDetails.model";

@Component({
  selector: 'app-accommodation-comment-form',
  templateUrl: './accommodation-comment-form.component.html',
  styleUrls: ['./accommodation-comment-form.component.css']
})
export class AccommodationCommentFormComponent implements OnInit{
  text: String = "";
  title: String = "";
  rating: number = 5;
  @Input() accommodationId: number = 0;
  @Input() hostId?: number;
  @Output() newCommentEvent = new EventEmitter<string>();
  @Input() ratingType: string = "accommodation";
  @Input() canRate: Boolean = false;
  hasComment: Boolean = false;

  ngOnInit(): void {
    this.service.canRate(this.accommodationId, +this.tokenStorage.getId()!).subscribe({
      next: (data:Boolean) => {
        this.canRate = data;
      }
    })
    if(this.accommodationId != 0) {

      this.loadHostId();
    }
  }
  getFloatLabelValue(): FloatLabelType  {
    return 'auto';
  }
  constructor(private service: CommentService,
              private tokenStorage: TokenStorage,
              private notificationService: NotificationService,
              private accommodationService: AccommodationDetailsService) {}

  setRating(n : number){
    this.rating = n
  }

  post() {
    if(this.ratingType == "accommodation") {this.postAccommodationRating(); }
    else {this.postHostRating(); }
  }

  postAccommodationRating() : void {
    var comment = {
      "text" : this.title + " " + this.text,
      "rating" : this.rating,
      "userName" : "Aa",
      "id" : 0,
      "userId" : +this.tokenStorage!.getId()!,
      "ratedId" : +this.accommodationId!
    }
    this.service.addAccommodationComment(comment).subscribe({
      next: () => {
        this.canRate = false;

        this.newCommentEvent.emit("")
      },
      error: (_) => {console.log("Greska!")}
    })

    const notification: MyNotification = {
      "date": new Date(),
      "notificationType": "NEW_ACCOMMODATION_RATING",
      "targetAppUser": +this.hostId!,
      "text": "Your accommodation is rated ",
      "title": "New accommodation rating"
    }
    this.sendNotification(notification);
  }

  postHostRating(): void {
    var comment = {
      "text" : this.title + " " + this.text,
      "rating" : this.rating,
      "userName" : "Aa",
      "id" : 0,
      "userId" : +this.tokenStorage!.getId()!,
      "ratedId" : +this.hostId!
    }

    this.service.addHostComment(comment).subscribe({
      next: () => {
        this.newCommentEvent.emit("")
      },
      error: (_) => {console.log("Greska!")}
    })
    const notification: MyNotification = {
      "date": new Date(),
      "notificationType": "NEW_RATING",
      "targetAppUser": +this.hostId!,
      "text": "You have been rated",
      "title": "New rating"
    }
    this.sendNotification(notification);
  }

  sendNotification(notification: MyNotification) {
    // this.notificationService.addNotification(notification).subscribe({
    //   next: () => {console.log("New notification successfully send");},
    //   error: () => {console.log("Error while posting new notification! ", notification);}
    // })
      this.notificationService.addNotification(notification)
  }

  loadHostId() {
    this.accommodationService.getAccommodation(this.accommodationId).subscribe({
      next: (data: AccommodationDetails) => {
        this.hostId = data.hostId;
      }
    })
  }
}
