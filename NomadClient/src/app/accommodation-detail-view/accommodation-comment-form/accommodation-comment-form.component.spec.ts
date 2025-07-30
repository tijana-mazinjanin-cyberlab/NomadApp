import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccommodationCommentFormComponent} from './accommodation-comment-form.component';

describe('AccommodationCommentFormComponent', () => {
  let component: AccommodationCommentFormComponent;
  let fixture: ComponentFixture<AccommodationCommentFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccommodationCommentFormComponent]
    });
    fixture = TestBed.createComponent(AccommodationCommentFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
