import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateReportComponent } from './create-report/create-report.component';
import {MaterialModule} from "../infrastructure/material/material.module";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {RouterLink} from "@angular/router";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatSliderModule} from "@angular/material/slider";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatNativeDateModule} from "@angular/material/core";
import {MapComponent} from "../shared/map/map.component";
import {SnackBarComponent} from "../shared/snack-bar/snack-bar.component";
import {MatSelect, MatSelectModule} from "@angular/material/select";


@NgModule({
  declarations: [
    CreateReportComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    FormsModule,
    MatSelectModule
  ],
  exports: [
    CreateReportComponent
  ]
})
export class ReportModule { }
