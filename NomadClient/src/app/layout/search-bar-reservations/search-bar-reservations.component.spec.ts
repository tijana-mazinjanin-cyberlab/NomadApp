import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SearchBarReservationsComponent} from './search-bar-reservations.component';

describe('SearchBarReservationsComponent', () => {
  let component: SearchBarReservationsComponent;
  let fixture: ComponentFixture<SearchBarReservationsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SearchBarReservationsComponent]
    });
    fixture = TestBed.createComponent(SearchBarReservationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
