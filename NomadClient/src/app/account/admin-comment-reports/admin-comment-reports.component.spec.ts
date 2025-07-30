import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCommentReportsComponent } from './admin-comment-reports.component';

describe('AdminCommentReportsComponent', () => {
  let component: AdminCommentReportsComponent;
  let fixture: ComponentFixture<AdminCommentReportsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminCommentReportsComponent]
    });
    fixture = TestBed.createComponent(AdminCommentReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
