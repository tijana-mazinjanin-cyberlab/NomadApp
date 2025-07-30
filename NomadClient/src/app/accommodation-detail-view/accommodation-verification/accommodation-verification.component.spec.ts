import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccommodationVerificationComponent} from './accommodation-verification.component';

describe('AccommodationVerificationComponent', () => {
  let component: AccommodationVerificationComponent;
  let fixture: ComponentFixture<AccommodationVerificationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccommodationVerificationComponent]
    });
    fixture = TestBed.createComponent(AccommodationVerificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
