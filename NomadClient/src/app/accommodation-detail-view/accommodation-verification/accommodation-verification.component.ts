import {Component, Input, OnInit} from '@angular/core';
import {AccommodationVerificationRequest} from '../model/accommodationVerificationRequest.model';
import {AccommodationDetailsService} from '../accommodation-details.service';

@Component({
  selector: 'app-accommodation-verification',
  templateUrl: './accommodation-verification.component.html',
  styleUrls: ['./accommodation-verification.component.css']
})
export class AccommodationVerificationComponent implements OnInit{
  @Input() requests: AccommodationVerificationRequest[] = []
  constructor(private service: AccommodationDetailsService){

  }
  ngOnInit(): void {
    this.loadRequests()
  }
  loadRequests():void{
    this.service.getUnverifiedRequests().subscribe({
      next: (data: AccommodationVerificationRequest[]) => {
        this.requests = data;
     },
      error: (_) => {console.log("Greska!")}
    })
  }
  accept(id:number):void{
    this.service.accept(id).subscribe({
      next: (data: AccommodationVerificationRequest) => {
        this.loadRequests();
     },
      error: (_) => {console.log("Greska!")}
    })
  }
  deny(id:number):void{
    this.service.decline(id).subscribe({
      next: (data: AccommodationVerificationRequest) => {
        this.loadRequests();
      },
      error: (_) => {console.log("Greska!")}
    })
  }
  //   this.service.delete(id).subscribe({
  //     next: (data: AccommodationVerificationRequest) => {
  //       this.loadRequests()
  //    },
  //     error: (_) => {console.log("Greska!")}
  //   })
  // }
}
