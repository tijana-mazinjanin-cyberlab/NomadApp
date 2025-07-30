import {AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {DateRange, MatCalendar, MatCalendarCellCssClasses} from "@angular/material/datepicker";
import {AccommodationDetailsService} from "../../accommodation-detail-view/accommodation-details.service";
import {SnackBarService} from "../../shared/snack-bar.service";
import {SnackBarComponent} from "../../shared/snack-bar/snack-bar.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AccommodationDetails} from "../../accommodation-detail-view/model/accommodationDetails.model";
import { PriceDateRange} from "../../accommodation-detail-view/model/priceDateRange.model";
import {DateRangeModel} from "../../accommodation-detail-view/model/dateRange.model";

@Component({
  selector: 'app-accommodation-availability',
  templateUrl: './accommodation-availability.component.html',
  styleUrls: ['./accommodation-availability.component.css']
})
export class AccommodationAvailabilityComponent implements AfterViewInit {

  datesToHighlight : string[] = [];
  dateRange:DateRange<Date>|null =null;

  defaultPrice: number = 0;
  intervalPrice:number = 0.0;
  @Output() defaultPriceEmitter = new EventEmitter<number> ();
  @Output() priceType = new EventEmitter<string> ();
  @Output() conformationType = new EventEmitter<string> ();
  @Output() pricesForIntervals = new EventEmitter<PriceDateRange>();
  @Output() unavailableIntervalsEmmiter = new EventEmitter<DateRange<Date>>();
  @Output() availableIntervalsEmmiter = new EventEmitter<DateRange<Date>>();

  @Input() accommodation: AccommodationDetails | undefined;

  @ViewChild(MatCalendar) calendar!: MatCalendar<Date>;

  constructor(private accommodationDetailsService: AccommodationDetailsService,
              private snackService: SnackBarService, private _snackBar: MatSnackBar,
              private cdr: ChangeDetectorRef) {}

  ngAfterViewInit(): void {
        if(this.accommodation) {
          this.loadDates();
          this.calendar.dateClass = this.dateClass(this.datesToHighlight);
        }
    }

  ngAfterContentInit(): void {
        this.initializeFields();
    }

  initializeFields(): void {
    if(this.accommodation) {
      this.defaultPrice = this.accommodation.defaultPrice;
      this.defaultPriceEmitter.emit(this.defaultPrice);
      const priceTypeRadio = document.getElementById(this.accommodation.priceType) as HTMLInputElement;
      priceTypeRadio.checked= true;
      if(this.accommodation.confirmationType == 'AUTOMATIC'){
        const conformationTypeBox = document.getElementById("conformationType") as HTMLInputElement;
        conformationTypeBox.checked = true;
      }

    }
  }


  onPriceTypeChange(event:any):void {
    const selected = event.target.value;
    this,this.priceType.emit(selected);
    // if(selected == "FOR_GUEST"){
    //   this.priceType.emit("FOR_GUEST");
    // }
    // if (selected == "FOR_ACCOMMODATION") {
    //   this.priceType.emit("FOR_ACCOMMODATION");
    // }
  }

  onConformationTypeChange(event: any):void {
    if (event.target.checked) { this.conformationType.emit("AUTOMATIC"); }
    else { this.conformationType.emit("MANUAL"); }
  }

  onDefaultPriceChange(): void {
    this.defaultPriceEmitter.emit(this.defaultPrice);
  }

  setPriceForInterval() {
    if(this.dateRange == null) {
      this.openSnackBar("WARNING! Select date range first!");
      return;
    }
    if(this.intervalPrice < 0 || !this.intervalPrice) {
      this.openSnackBar("WARNING! Enter price first!");
      return;
    }
    const priceDR: PriceDateRange = {
      price:this.intervalPrice.toFixed(2),
      dateRange: {startDate:this.dateRange.start!, finishDate:this.dateRange.end!}
    }
    // this.pricesForIntervals.emit({
    //   "price": this.intervalPrice.toFixed(2),
    //   "startDate": this.dateRange.start,
    //   "finishDate": this.dateRange.end
    // });
    this.pricesForIntervals.emit(priceDR);
    this.openSnackBar("SUCCESS: Price will be updated!");
  }

  setUnavailable() {
    if(this.dateRange == null) {
      this.openSnackBar("WARNING! Select date range first!");
      return;
    }
    this.unavailableIntervalsEmmiter.emit(this.dateRange);
    this.openSnackBar("SUCCESS: Accommodation availability will be updated!");
    this.datesToHighlight = [];
    // @ts-ignore
    this.datesToHighlight.push(this.dateRange.start.toDateString());
    // @ts-ignore
    this.datesToHighlight.push(this.dateRange.end.toDateString());
    this.calendar.dateClass = this.dateClass(this.datesToHighlight);
    this.cdr.detectChanges();
  }

  setAvailable() {
    if(this.dateRange == null) {
      this.openSnackBar("WARNING! Select date range first!");
      return;
    }
    this.availableIntervalsEmmiter.emit(this.dateRange);
    this.openSnackBar("SUCCESS: Accommodation availability will be updated!");
    this.datesToHighlight = [];
    // @ts-ignore
    this.datesToHighlight.push(this.dateRange.start.toDateString());
    // @ts-ignore
    this.datesToHighlight.push(this.dateRange.end.toDateString());
    this.calendar.dateClass = this.dateClass(this.datesToHighlight);
    this.cdr.detectChanges();
  }


  setPriceForDate(date:Date): void {
    if(this.accommodation) {
      this.accommodationDetailsService.getPrice(this.accommodation.id, date.toDateString()).subscribe({
        next: (data: number) => {
          this.intervalPrice = data;
        },
        error: (_) => {console.log("Greska!")}
      });
    }
  }

  dateClass(dates: string[]) {
    return (date: Date): MatCalendarCellCssClasses => {
      const highlightDate = dates
        .map(strDate => new Date(strDate))
        .some(d => d.getDate() === date.getDate() && d.getMonth() === date.getMonth() && d.getFullYear() === date.getFullYear());

      return highlightDate ? 'special-date' : '';
    };
  }

  loadDates():void{
    this.accommodationDetailsService.getTakenDates(this.accommodation!.id).subscribe({
      next: (data: string[]) => {
        this.datesToHighlight = data.map(date => {return new Date(date).toDateString()});
      },
      error: (_) => {console.log("Greska!")}
    })
  }

  onChange(ev: any) {
    let v = new Date(ev);
    if(this.dateRange == null){
      this.dateRange =  new DateRange((() => {
        let v = new Date(ev);
        this.setPriceForDate(v);
        return v;
      })(), v);
    }else if(v > this.dateRange!.start!){
      if (this.datesOverlap(this.dateRange.start!, v)){
        this.dateRange = null;
      }
      this.dateRange =  new DateRange(this.dateRange!.start!, (() => {
        let v = new Date(ev);

        return v;
      })());
    }

  }

  datesOverlap(start: Date, end: Date):boolean{
    if(end < new Date() || start < new Date()){
      return true;
    }
    return false;
  }

  reset():void{
    this.dateRange = null;
  }

  openSnackBar(message: string) {
    this._snackBar.openFromComponent(SnackBarComponent, {
      duration: 5000,
    });
    this.snackService.errorMessage$.next(message)
  }

}
