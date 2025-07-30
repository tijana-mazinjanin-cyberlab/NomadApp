import {Component} from '@angular/core';
import {Login} from "../../infrastructure/auth/model/login.model";
import {Router} from "@angular/router";
import {AuthService} from "../../infrastructure/auth/auth.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {SnackBarComponent} from "../../shared/snack-bar/snack-bar.component";
import {SnackBarService} from "../../shared/snack-bar.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  // windowHeight: number = window.innerHeight;
  // windowWidth: number = window.innerWidth;
  constructor(
    private authService: AuthService,
    private router: Router, private snackService: SnackBarService, private _snackBar: MatSnackBar
  ) {}

  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });
  openSnackBar() {
    this._snackBar.openFromComponent(SnackBarComponent, {
      duration: 2000,
    });
    this.snackService.errorMessage$.next("Username or password are invalid")
  }
  login(): void {
    const login: Login = {
      username: this.loginForm.value.username || "",
      password: this.loginForm.value.password || "",
    };

      this.authService.login(login).subscribe({
        next: () => {
          this.router.navigate(['/home']);
        },
        error: () => {
          this.openSnackBar();
        }
      });
  }


}
