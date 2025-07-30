import {Component, Input} from '@angular/core';
import {User} from "../model/user.model";
import {AccountService} from "../account.service";
import {AuthService} from "../../infrastructure/auth/auth.service";
import {Login} from 'src/app/infrastructure/auth/model/login.model';
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {SnackBarService} from "../../shared/snack-bar.service";
import {SnackBarComponent} from "../../shared/snack-bar/snack-bar.component";
import {MatSnackBar} from "@angular/material/snack-bar";
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  isResetPasswordMode: boolean = false;
  isModalVisible: boolean = false;
  deleteConformation: boolean = false;
  oldPassword: String = "";
  newPassword: String = "";
  confirmedPassword: String = "";
  errors: String[] = [];
  passwordErrors: String[] = [];

  profileForm :FormGroup= new FormGroup({});
  resetPasswordForm: FormGroup = new FormGroup({});
  get f() { return this.profileForm.controls; }

  validationPatterns = {
    "phoneNumber" : new RegExp("^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$"),
    "userName" : new RegExp("[A-Za-z0-9]{3,20}"),
    "name" : new RegExp("[A-Za-z]{3,20}"),
    "address" : new RegExp("[A-Za-z0-9]{3,20}"),
  }
  @Input() user: User = {} as User;
  isEditMode:boolean=false;
  constructor(private authService: AuthService,
              private service: AccountService,
              private _formBuilder: FormBuilder,
              private snackService: SnackBarService, private _snackBar: MatSnackBar) {}
  turnEditMode(){
    this.isEditMode = true;
  }
  exitEditMode(){
    this.isEditMode = false;
  }

  getDeleteConformation(conformation: boolean): void {
    this.isModalVisible = false;
    this.deleteConformation = conformation;
    if (this.deleteConformation) {
      this.service.deleteAccount(this.user.id).subscribe({
        next: () => {
          console.log("User with id ", this.user.id, " is successfully deleted!");
          this.logout();
        },
        error: () => {
          console.log("Error while deleting user with id: ", this.user.id,  "!"); }
      })
    }
  }
  editUser(): void {
    if(!this.profileForm.valid){ return; }

    this.user.username = this.profileForm.getRawValue().username;
    this.user.firstName = this.profileForm.getRawValue().firstName;
    this.user.lastName = this.profileForm.getRawValue().lastName;
    this.user.address = this.profileForm.getRawValue().address;
    this.user.phoneNumber = this.profileForm.getRawValue().phoneNumber;

    this.service.editUser(this.user).subscribe({
      next: (res: User) => {
        this.isEditMode = false;
        this.openSnackBar("SUCCESS: User is successfully updated!");
      },
      error: () => { console.log("Error while editing user with id: ", this.user.id,  "!"); }
    });
  }
  validate(): boolean{
    this.errors = []
    if(!this.validationPatterns.phoneNumber.test(this.user.phoneNumber)){
      this.errors.push("Phone number invalid");
    }
    if(!this.validationPatterns.userName.test(this.user.username)){
      this.errors.push("Username invalid");
    }
    if(!this.validationPatterns.name.test(this.user.firstName)){
      this.errors.push("First Name invalid");
    }
    if(!this.validationPatterns.name.test(this.user.lastName)){
      this.errors.push("Last Name invalid");
    }
    if(!this.validationPatterns.address.test(this.user.address)){
      this.errors.push("Address invalid");
    }
    return this.errors.length == 0;
  }
  showDialog(): void {
    this.isModalVisible = true;
  }

  logout(): void {
    this.authService.logout();
  }
  ngOnInit(): void {
    this.setProfileForm();
    this.setResetPasswordForm();

    this.service.getLoggedUser().subscribe({
      next: (data: User) => {
        this.user = data;
        this.profileForm.setValue({
          firstName:data.firstName,
          lastName:data.lastName,
          address:data.address,
          phoneNumber:data.phoneNumber,
          username:data.username
        });
        },
      error: () => { console.log("Error while reading logged user!"); }
    })
  }

  setProfileForm() {
    this.profileForm = this._formBuilder.group({
      firstName:['', [Validators.required, Validators.pattern("[A-Za-z]{3,20}")]],
      lastName:['', [Validators.required, Validators.pattern("[A-Za-z]{3,20}")]],
      address:['', [Validators.required, Validators.pattern("[A-Za-z0-9 ]{3,20}")]],
      phoneNumber:['', [Validators.required, Validators.pattern("^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$")]],
      username:['', [Validators.required, Validators.pattern("[A-Za-z0-9]{3,20}")]]
    })
  }

  setResetPasswordForm() {
    this.resetPasswordForm = this._formBuilder.group({
      oldPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(8)]]
    }, { validator: this.passwordMatchValidator })
  }

  enterResetPasswordMode() : void {
    this.isResetPasswordMode = true;
    this.isEditMode = false;
  }
  exitResetPasswordMode(): void{
    this.isResetPasswordMode = false;
    this.isEditMode = false;
  }
  resetPassword(): void{
    this.passwordErrors = []
    console.log(this.resetPasswordForm.getRawValue().oldPassword);
    var login: Login = {
      "username" : this.user.username,
      "password" : this.resetPasswordForm.getRawValue().oldPassword
    }
    this.authService.reauthenticate(login).subscribe({
      next : (_) => {
        if(!this.resetPasswordForm.valid) {
          return;
        }

        this.user.password = this.resetPasswordForm.getRawValue().newPassword;
        this.service.editUser(this.user).subscribe({
          next: (_) => {},
          error: (_) => {console.log("Error changing password")}
        })
      },
      error : (e) => {
        console.log(e)
        this.passwordErrors.push("Old Password Incorrect")
      }

  });
  }

  openSnackBar(message: string) {
    this._snackBar.openFromComponent(SnackBarComponent, {
      duration: 5000,
    });
    this.snackService.errorMessage$.next(message)
  }

   passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const newPassword = control.get('newPassword');
    const confirmPassword = control.get('confirmPassword');

    if (newPassword && confirmPassword && newPassword.value !== confirmPassword.value) {
      return { passwordMismatch: true };
    }

    return null;
  }
}
