import {ChangeDetectorRef, Component, Input, OnInit, ViewChild} from '@angular/core';
import {DateRange, MatCalendar, MatCalendarCellCssClasses} from '@angular/material/datepicker';
import {AccommodationDetailsService} from '../accommodation-details.service';
import {Reservation} from '../model/reservation.model';
import {AccommodationDetails} from '../model/accommodationDetails.model';
import {TokenStorage} from 'src/app/infrastructure/auth/jwt/token.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {SnackBarService} from 'src/app/shared/snack-bar.service';
import {SnackBarComponent} from 'src/app/shared/snack-bar/snack-bar.component';
import {NotificationService} from "../../notifications/notification.service";
import {MyNotification} from "../../notifications/notification.model";

@Component({
  selector: 'app-accommodation-reservation',
  templateUrl: './accommodation-reservation.component.html',
  styleUrls: ['./accommodation-reservation.component.css']
})
export class AccommodationReservationComponent implements OnInit{
  @Input() id?: number;
  @Input() accommodation!: AccommodationDetails;
  @Input() startDate: string = "";
  @Input() endDate: string = "";
  @Input() peopleNum: number = 0;

  fromDate:Date|null;
  toDate:Date|null;
  datesToHighlight : string[]|null;
  dates : Date[];
  dateRange:DateRange<Date>|null;
  price: number = 0;
  guests: number = 0;
  @ViewChild(MatCalendar, {static: false}) calendar!: MatCalendar<Date>;
  constructor(private service: AccommodationDetailsService,
              public tokenStorage:TokenStorage,
              private cdr: ChangeDetectorRef,
              private snackService: SnackBarService,
              private _snackBar: MatSnackBar,
              private notificationService: NotificationService){
    this.id = 1;
    this.datesToHighlight = null
    this.dates = []

    this.fromDate = null;
    this.toDate = null;
    this.dateRange = null

  }
  parseDate(dateString: string): Date | null {
    const components = dateString.split('/');

    if (components.length === 3) {
      const month = parseInt(components[0], 10) - 1;
      const day = parseInt(components[1], 10);
      const year = parseInt(components[2], 10);

      const date = new Date(year, month, day);

      if (!isNaN(date.getTime())) {
        return date;
      }
    }

    return null;
  }
  openSnackBar(message: string) {
    this._snackBar.openFromComponent(SnackBarComponent, {
      duration: 2000,
    });
    this.snackService.errorMessage$.next(message)
  }
  ngOnInit(): void {
    this.loadDates();
    // this.cdr.detectChanges();
    // this.calendar.updateTodaysDate()
    if(this.peopleNum!= -1){
      this.guests = this.peopleNum;
    }
    if(this.startDate != ""){
      this.fromDate = this.parseDate(this.startDate);
      this.toDate = this.parseDate(this.endDate);
      this.dateRange = new DateRange(this.fromDate, this.toDate);
      this.datesOverlap(this.fromDate, this.toDate);
    }else{
      this.fromDate = null;
      this.toDate = null;
      this.dateRange = null
    }
    this.calendar.updateTodaysDate();
    this.calendar.activeDate = new Date();

  }
  dateClass(dates: string[]) {
    return (date: Date): MatCalendarCellCssClasses => {
      const highlightDate = dates
        .map(strDate => new Date(strDate))
        .some(d => d.getDate() === date.getDate() && d.getMonth() === date.getMonth() && d.getFullYear() === date.getFullYear());

      return highlightDate ? 'special-date' : '';
    };
  }
  onSelect(){}
  reset():void{
    this.fromDate = null;
    this.toDate = null;
    this.dateRange = null;
    this.price = 0
  }
  reserve():void{
    this.service.reserve({
      "user" : +this.tokenStorage.getId()!,
    "accommodation" : this.id!,
    "startDate" : this.dateRange!.start!,
    "finishDate" : this.dateRange!.end!,
    "numGuests" : this.guests,
    "status" : "PENDING",
    }).subscribe({
      next: (data:Reservation) => {
        console.log(data);
        this.openSnackBar("Reservation made successfully");
        this.sendNotification();
      },
      error: (a) => {
        console.log(a);
        this.openSnackBar("Error making reservation");
      }
    })
    this.loadDates();
    this.calendar.dateClass = this.dateClass(this.datesToHighlight!);
    this.cdr.detectChanges();

  }
  loadDates():void{
    this.service.getTakenDates(this.id).subscribe({
      next: (data: string[]) => {
        this.datesToHighlight = data.map(date => {return new Date(date).toDateString()});
     },
      error: (_) => {console.log("Greska!")}
    })
  }
  onChange(ev: Date|null) {
    let v = new Date(ev!);
    if(this.dateRange == null){
      this.dateRange =  new DateRange((() => {
        let v = new Date(ev!);

        return v;
      })(), v);
    }else if(v > this.dateRange!.start!){
      if (this.datesOverlap(this.dateRange.start!, v)){
        this.dateRange = null;
      }
      this.dateRange =  new DateRange(this.dateRange!.start!, (() => {
        let v = new Date(ev!);

        return v;
      })());
    }

  }
 datesOverlap(start: Date|null, end: Date|null):boolean{
    if(start == null || end == null){
      return false;
    }
  this.price = 0
  for(var date = new Date(start); date < end; date.setDate(date.getDate() + 1)){
    this.service.getPrice(this.id!,date.toDateString()).subscribe({
      next: (data: number) => {
        this.price += data
     },
      error: (_) => {console.log("Greska!")}
    })
    if (this.datesToHighlight!.indexOf(date.toDateString()) > -1){
      this.openSnackBar("Accommodation is reserved for some of those dates!")
      return true;
    }


  }
  if(end < new Date() || start < new Date()){
    this.openSnackBar("May not make reservations in the past")
    return true;
  }
  return false;
 }

 sendNotification() {
   const notification: MyNotification = {
     "date": new Date(),
     "notificationType": "NEW_RESERVATION",
     "targetAppUser": this.accommodation.hostId,
     "text": "New reservation has been made.",
     "title": "New reservation"
   }
   // this.notificationService.addNotification(notification).subscribe({
   //   next: () => {console.log("New notification successfully send");},
   //   error: () => {console.log("Error while posting new notification! ", notification);}
   // })
   this.notificationService.addNotification(notification);
 }
}
