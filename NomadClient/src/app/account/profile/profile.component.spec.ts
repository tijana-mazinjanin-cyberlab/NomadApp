import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ProfileComponent} from './profile.component';

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import {AccountService} from "../account.service";
import {AuthService} from "../../infrastructure/auth/auth.service";
import {of, throwError} from "rxjs";
import {SnackBarService} from "../../shared/snack-bar.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {User} from "../model/user.model";
import {By} from "@angular/platform-browser";


describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let authService: AuthService;
  let accountService: AccountService;

  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProfileComponent],
      imports: [HttpClientTestingModule],
      providers: [AccountService, AuthService, SnackBarService, MatSnackBar]
    });
    authService = TestBed.inject(AuthService);
    accountService = TestBed.inject(AccountService);
    httpTestingController = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    //fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set default values', async () => {
      component.user = {
          id: 0, password: "", roles: [], suspended: false, userType: "",
          firstName: 'John',
          lastName: 'Doe',
          address: '123MainSt',
          phoneNumber: '0614030802',
          username: 'johndoe'
      }
      spyOn(accountService, 'getLoggedUser').and.returnValue(of(component.user));
      component.ngOnInit();
      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.profileForm.getRawValue().firstName).toEqual(component.user.firstName);
      expect(component.profileForm.getRawValue().lastName).toEqual(component.user.lastName);
      expect(component.profileForm.getRawValue().address).toEqual(component.user.address);
      expect(component.profileForm.getRawValue().phoneNumber).toEqual(component.user.phoneNumber);
      expect(component.profileForm.getRawValue().username).toEqual(component.user.username);
  })

  it('should validate the form correctly', async () => {
    component.user = {
      id: 0, password: "", roles: [], suspended: false, userType: "",
      firstName: 'John',
      lastName: 'Doe',
      address: '123MainSt',
      phoneNumber: '0614030802',
      username: 'johndoe'
    }

      spyOn(accountService, 'getLoggedUser').and.returnValue(of(component.user));
      component.ngOnInit();
      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.profileForm.valid).toEqual(true);
  });

  it('should validate the form as false due to empty fields', async () => {
    component.user = {
      id: 0, password: "", roles: [], suspended: false, userType: "",
      firstName: '',
      lastName: '',
      address: '',
      phoneNumber: '',
      username: ''
    }

    spyOn(accountService, 'getLoggedUser').and.returnValue(of(component.user));
    component.ngOnInit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(component.profileForm.get('firstName')?.errors?.['required']).toEqual(true);
    expect(component.profileForm.get('lastName')?.errors?.['required']).toEqual(true);
    expect(component.profileForm.get('address')?.errors?.['required']).toEqual(true);
    expect(component.profileForm.get('phoneNumber')?.errors?.['required']).toEqual(true);
    expect(component.profileForm.get('username')?.errors?.['required']).toEqual(true);

    expect(component.profileForm.valid).toEqual(false);
  });

    it('should validate the form as false due to too short names', async () => {
        component.user = {
            id: 0, password: "", roles: [], suspended: false, userType: "",
            firstName: 'A',
            lastName: 'Aa',
            address: 'a',
            phoneNumber: '1234567890',
            username: 'a'
        }

        spyOn(accountService, 'getLoggedUser').and.returnValue(of(component.user));
        component.ngOnInit();
        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.profileForm.hasError('pattern', 'firstName')).toBeTrue();
        expect(component.profileForm.hasError('pattern', 'lastName')).toBeTrue();
        expect(component.profileForm.hasError('pattern', 'address')).toBeTrue();
        expect(component.profileForm.hasError('pattern', 'username')).toBeTrue();

        expect(component.profileForm.valid).toEqual(false);
    });

    it('should validate phone numbers with and without international prefixes', ()=> {
        component.setProfileForm();
        component.profileForm.patchValue({phoneNumber: "123-456-7890"})
        expect(component.profileForm.hasError('pattern', 'phoneNumber')).toBeFalse();

        component.profileForm.patchValue({phoneNumber: "123.456.7890"})
        expect(component.profileForm.hasError('pattern', 'phoneNumber')).toBeFalse();

        component.profileForm.patchValue({phoneNumber: "1234567890"})
        expect(component.profileForm.hasError('pattern', 'phoneNumber')).toBeFalse();

        component.profileForm.patchValue({phoneNumber: "(123)456-7890"})
        expect(component.profileForm.hasError('pattern', 'phoneNumber')).toBeFalse();

        component.profileForm.patchValue({phoneNumber: "+38123456789"})
        expect(component.profileForm.hasError('pattern', 'phoneNumber')).toBeFalse();

    })

    it('should call the onSubmit method', async () => {
        const openSnackBarSpy = spyOn(component, 'openSnackBar');
        component.setProfileForm();
        component.profileForm.setValue({
            username: 'newUsername',
            firstName: 'John',
            lastName: 'Doee',
            address: '123 Main St',
            phoneNumber: '0614030802'
        })

        spyOn(accountService, 'editUser').and.returnValue(of(component.user));
        component.editUser();
        fixture.detectChanges();
        await fixture.whenStable();

        expect(openSnackBarSpy).toHaveBeenCalledWith('SUCCESS: User is successfully updated!');

    });

    it('should handle incorrect old password', () => {
        component.setResetPasswordForm();
        spyOn(authService, 'reauthenticate').and.returnValue(
            throwError({ message: 'Incorrect old password' })
        );
        spyOn(accountService, 'editUser');

        component.resetPasswordForm.setValue({
            oldPassword: 'incorrectOldPassword',
            newPassword: 'newPassword123',
            confirmPassword: 'newPassword123',
        });
        component.resetPassword();

        expect(authService.reauthenticate).toHaveBeenCalledWith({
            username: component.user.username,
            password: 'incorrectOldPassword',
        });

        expect(component.passwordErrors).toContain('Old Password Incorrect');
        expect(accountService.editUser).toHaveBeenCalledTimes(0);
    });

    it('should reset password if old password is correct and form is valid', () => {
        component.setResetPasswordForm();
        component.resetPasswordForm.setValue({
            oldPassword: 'correctOldPassword',
            newPassword: 'newPassword123',
            confirmPassword: 'newPassword123',
        });

        spyOn(authService, 'reauthenticate').and.returnValue(of({
            username: component.user.username,
            password: 'correctOldPassword',
        }));


        component.resetPassword();

        expect(authService.reauthenticate).toHaveBeenCalledWith({
            username: component.user.username,
            password: 'correctOldPassword',
        });
        expect(component.resetPasswordForm.valid).toBeTruthy();
        const buttonElement: HTMLButtonElement = fixture.debugElement.query(By.css('button')).nativeElement;
        expect(buttonElement.disabled).toBeFalse();

    });

    it('should validate reset password form as incorrect due to wrong password format',  () => {
        spyOn(accountService, 'editUser');
        component.setResetPasswordForm();
        component.resetPasswordForm.setValue({
            oldPassword: 'correctOldPassword',
            newPassword: 'short',
            confirmPassword: 'short',
        });
        expect(component.resetPasswordForm.valid).toBeFalsy()
        expect(accountService.editUser).toHaveBeenCalledTimes(0);
        const buttonElement: HTMLButtonElement = fixture.debugElement.query(By.css('button')).nativeElement;
        expect(buttonElement.disabled).toBeTrue;
    });

    it('should validate reset password form as incorrect because the passwords do not mach', async () => {
        spyOn(accountService, 'editUser');
        component.setResetPasswordForm();
        component.resetPasswordForm.setValue({
            oldPassword: 'correctOldPassword',
            newPassword: 'password123',
            confirmPassword: 'password',
        });
        expect(component.resetPasswordForm.valid).toBeFalsy()
        expect(accountService.editUser).toHaveBeenCalledTimes(0);

        const buttonElement: HTMLButtonElement = fixture.debugElement.query(By.css('button')).nativeElement;
        expect(buttonElement.disabled).toBeTrue;
    });

});
