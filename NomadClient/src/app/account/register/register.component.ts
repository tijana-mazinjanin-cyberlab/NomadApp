import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UserRegistration} from "../model/user-registration.model";
import {AuthService} from "../../infrastructure/auth/auth.service";
import {Router} from "@angular/router";
import {SnackBarComponent} from "../../shared/snack-bar/snack-bar.component";
import {SnackBarService} from "../../shared/snack-bar.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import { Observable } from 'rxjs';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  passwordCheck:boolean=true;

  windowHeight: number = window.innerHeight;
  windowWidth: number = window.innerWidth;
  constructor(private snackService: SnackBarService, private _snackBar: MatSnackBar,private authService: AuthService,
              private router: Router) {
  }
  registerForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(2)]),
    surname: new FormControl('', [Validators.required, Validators.minLength(2)]),
    adress: new FormControl('', ),
    phone: new FormControl('', ),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required,Validators.minLength(8)]),
    passwordConfirm: new FormControl('',
      [Validators.required, Validators.minLength(8)]),
    options: new FormControl('GUEST', [Validators.required]),
  });
  openSnackBar() {
    this._snackBar.openFromComponent(SnackBarComponent, {
      duration: 5000,
    });
    this.snackService.errorMessage$.next("Activation link is sent to the email adress")
  }
  register(): void{
    if(this.registerForm.value.password != this.registerForm.value.passwordConfirm){
      this.passwordCheck = false;
      return 
    }this.passwordCheck = true;

    const user: UserRegistration = {
      firstName:this.registerForm.value.name || "",
      lastName:this.registerForm.value.surname || "",
      address:this.registerForm.value.adress || "",
      username:this.registerForm.value.email || "",
      password:this.registerForm.value.password || "",
      passwordConfirmation:this.registerForm.value.passwordConfirm || "",
      phoneNumber:this.registerForm.value.phone || "",
      roles:[this.registerForm.value.options || ""],
    };
    this._register().subscribe({
      next: () => {
        this.router.navigate(['/login']);
        this.openSnackBar();
      },
    });
  }
  _register(): Observable<UserRegistration> {
    const user: UserRegistration = {
      firstName:this.registerForm.value.name || "",
      lastName:this.registerForm.value.surname || "",
      address:this.registerForm.value.adress || "",
      username:this.registerForm.value.email || "",
      password:this.registerForm.value.password || "",
      passwordConfirmation:this.registerForm.value.passwordConfirm || "",
      phoneNumber:this.registerForm.value.phone || "",
      roles:[this.registerForm.value.options || ""],
    };
    var observable = this.authService.register(user);
    return observable;
  }
}
