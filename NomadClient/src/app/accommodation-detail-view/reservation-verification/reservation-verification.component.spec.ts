import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ReservationVerificationComponent} from './reservation-verification.component';

describe('ReservationVerificationComponent', () => {
  let component: ReservationVerificationComponent;
  let fixture: ComponentFixture<ReservationVerificationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReservationVerificationComponent]
    });
    fixture = TestBed.createComponent(ReservationVerificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
