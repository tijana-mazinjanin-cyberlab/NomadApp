import {Component, OnInit} from '@angular/core';
import {Reservation} from "../model/reservation.model";
import {AccommodationDetails, Review} from "../model/accommodationDetails.model";
import {AccommodationDetailsService} from "../accommodation-details.service";
import {ActivatedRoute} from "@angular/router";
import {CommentService} from "../comment.service";

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css']
})
export class ReservationComponent implements OnInit{

  id?:number;
  reservation?: Reservation;
  accommodation?: AccommodationDetails;
  reviewsAccommodation?: Review[];


  addComment:boolean = false;


  constructor(private service: AccommodationDetailsService, private commentService: CommentService, private route: ActivatedRoute) {}

  ngOnInit(): void {

    this.route.params.subscribe(params => {
      this.id = +params['id']; // (+) converts string 'id' to a number
      this.service.getReservation(this.id).subscribe({
        next: (data: Reservation) => {
          this.reservation = data;
          this.service.getAccommodation(data.accommodation).subscribe({
            next: (ac: AccommodationDetails) => {
              this.accommodation = ac;
              this.getCommentsAccommodation();
            },
            error: (_) => {console.log("Greska!")}
          })
        },
        error: (_) => {console.log("Greska!")}
      })
    });

  }


  getCommentsAccommodation(): void {
    this.commentService.getAccommodationComments(this.accommodation!.id!).subscribe({
      next: (data:Review[]) => {
        this.reviewsAccommodation = data;
      }
    })
  }

  onAddCommentClick() {
    this.addComment = !this.addComment;
  }

}
