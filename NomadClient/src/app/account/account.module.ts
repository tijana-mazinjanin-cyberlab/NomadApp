import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {RouterLink} from "@angular/router";
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ProfileComponent} from "./profile/profile.component";
import {MaterialModule} from "../infrastructure/material/material.module";
import {LayoutModule} from "../layout/layout.module";
import {MatSelectModule} from "@angular/material/select";
import {AdminAccountsViewComponent} from './admin-accounts-view/admin-accounts-view.component';
import {MatCardModule} from '@angular/material/card';
import {MatTableModule} from '@angular/material/table';

@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    ProfileComponent,
    AdminAccountsViewComponent
  ],
  imports: [
    CommonModule,
    RouterLink,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    LayoutModule,
    MatSelectModule,
    MatCardModule,
    MatTableModule
  ],
  exports: [
    LoginComponent,
    RegisterComponent,
    ProfileComponent
  ]
})
export class AccountModule { }
