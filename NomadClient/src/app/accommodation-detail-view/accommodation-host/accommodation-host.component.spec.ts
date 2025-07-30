import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccommodationHostComponent} from './accommodation-host.component';

describe('AccommodationHostComponent', () => {
  let component: AccommodationHostComponent;
  let fixture: ComponentFixture<AccommodationHostComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccommodationHostComponent]
    });
    fixture = TestBed.createComponent(AccommodationHostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
