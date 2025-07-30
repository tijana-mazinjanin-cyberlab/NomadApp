import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccommodationCardHostComponent} from './accommodation-card-host.component';

describe('AccommodationCardHostComponent', () => {
  let component: AccommodationCardHostComponent;
  let fixture: ComponentFixture<AccommodationCardHostComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccommodationCardHostComponent]
    });
    fixture = TestBed.createComponent(AccommodationCardHostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
