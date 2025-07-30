import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccommodationImagesComponent} from './accommodation-images.component';

describe('AccommodationImagesComponent', () => {
  let component: AccommodationImagesComponent;
  let fixture: ComponentFixture<AccommodationImagesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccommodationImagesComponent]
    });
    fixture = TestBed.createComponent(AccommodationImagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
