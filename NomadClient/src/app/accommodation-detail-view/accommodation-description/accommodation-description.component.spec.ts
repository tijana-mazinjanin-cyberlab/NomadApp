import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccommodationDescriptionComponent} from './accommodation-description.component';

describe('AccommodationDescriptionComponent', () => {
  let component: AccommodationDescriptionComponent;
  let fixture: ComponentFixture<AccommodationDescriptionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccommodationDescriptionComponent]
    });
    fixture = TestBed.createComponent(AccommodationDescriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
