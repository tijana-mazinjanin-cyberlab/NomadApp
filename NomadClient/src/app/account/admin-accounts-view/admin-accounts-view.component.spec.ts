import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AdminAccountsViewComponent} from './admin-accounts-view.component';

describe('AdminAccountsViewComponent', () => {
  let component: AdminAccountsViewComponent;
  let fixture: ComponentFixture<AdminAccountsViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminAccountsViewComponent]
    });
    fixture = TestBed.createComponent(AdminAccountsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
