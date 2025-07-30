import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccommodationCardsHostComponent} from './accommodation-cards-host.component';

describe('AccommodationCardsHostComponent', () => {
  let component: AccommodationCardsHostComponent;
  let fixture: ComponentFixture<AccommodationCardsHostComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccommodationCardsHostComponent]
    });
    fixture = TestBed.createComponent(AccommodationCardsHostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
