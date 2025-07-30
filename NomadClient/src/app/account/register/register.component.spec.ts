import {ComponentFixture, TestBed} from '@angular/core/testing';

import { RegisterComponent } from './register.component';
import { By } from '@angular/platform-browser';
import { NgModule, PlatformRef } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModule } from 'src/app/infrastructure/material/material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from '@angular/cdk/layout';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { CommonModule } from '@angular/common';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'
import { UserRegistration } from '../model/user-registration.model';



describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let httpClient: HttpTestingController;
  const url = 'http://localhost:8080/auth'
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports : [MatSnackBarModule, HttpClientModule, MaterialModule, HttpClientTestingModule,
        FormsModule,
        ReactiveFormsModule,
        MaterialModule,
        LayoutModule,
        MatSelectModule,
        MatCardModule,
        MatTableModule],
      declarations: [RegisterComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    // TestBed.initTestEnvironment(RegisterComponent, platform)
    TestBed.inject(MatSnackBar)
    httpClient = TestBed.inject(HttpTestingController) 
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should be invalid when empty', () => {
    
    expect(component.registerForm.valid).toBeFalsy();


  });
  it('should be valid when filled out', () => {
    
    expect(component).toBeTruthy();
    component.registerForm.controls['name'].setValue("TestName")
    component.registerForm.controls['surname'].setValue("TestSurName")
    component.registerForm.controls['email'].setValue("email@gmail.com")
    component.registerForm.controls['adress'].setValue("AAAAAAA")
    component.registerForm.controls['password'].setValue("Sifra123")
    component.registerForm.controls['passwordConfirm'].setValue("Sifra123")
    expect(component.registerForm.valid).toBeTruthy();

  });
  it('should be invalid with invalid email', () => {
    
    expect(component).toBeTruthy();
    component.registerForm.controls['name'].setValue("TestName")
    component.registerForm.controls['surname'].setValue("TestSurName")
    component.registerForm.controls['email'].setValue("email")
    component.registerForm.controls['adress'].setValue("AAAAAAA")
    component.registerForm.controls['password'].setValue("Sifra123")
    component.registerForm.controls['passwordConfirm'].setValue("Sifra123")
    expect(component.registerForm.valid).toBeFalsy();

  });
  it('should be invalid with non-matching passwords', () => {
    
    expect(component).toBeTruthy();
    component.registerForm.controls['name'].setValue("TestName")
    component.registerForm.controls['surname'].setValue("TestSurName")
    component.registerForm.controls['email'].setValue("email")
    component.registerForm.controls['adress'].setValue("AAAAAAA")
    component.registerForm.controls['password'].setValue("Sifra12")
    component.registerForm.controls['passwordConfirm'].setValue("Sifra123")
    expect(component.registerForm.valid).toBeFalsy();

  });
  it('should be invalid with matching invalid password', () => {
    
    expect(component).toBeTruthy();
    component.registerForm.controls['name'].setValue("TestName")
    component.registerForm.controls['surname'].setValue("TestSurName")
    component.registerForm.controls['email'].setValue("email")
    component.registerForm.controls['adress'].setValue("AAAAAAA")
    component.registerForm.controls['password'].setValue("1")
    component.registerForm.controls['passwordConfirm'].setValue("1")
    expect(component.registerForm.valid).toBeFalsy();

  });
  it('should be invalid with invalid first name', () => {
    
    expect(component).toBeTruthy();
    component.registerForm.controls['name'].setValue("")
    component.registerForm.controls['surname'].setValue("TestSurName")
    component.registerForm.controls['email'].setValue("email")
    component.registerForm.controls['adress'].setValue("AAAAAAA")
    component.registerForm.controls['password'].setValue("Sifra12")
    component.registerForm.controls['passwordConfirm'].setValue("Sifra123")
    expect(component.registerForm.valid).toBeFalsy();

  });
  it('should be invalid with invalid last name', () => {
    
    expect(component).toBeTruthy();
    component.registerForm.controls['name'].setValue("TestName")
    component.registerForm.controls['surname'].setValue("")
    component.registerForm.controls['email'].setValue("email")
    component.registerForm.controls['adress'].setValue("AAAAAAA")
    component.registerForm.controls['password'].setValue("Sifra12")
    component.registerForm.controls['passwordConfirm'].setValue("Sifra123")
    expect(component.registerForm.valid).toBeFalsy();

  });
  
  it('should be invalid with empty adress', () => {
    
    expect(component).toBeTruthy();
    component.registerForm.controls['name'].setValue("TestName")
    component.registerForm.controls['surname'].setValue("TestSurName")
    component.registerForm.controls['email'].setValue("email")
    component.registerForm.controls['adress'].setValue("")
    component.registerForm.controls['password'].setValue("Sifra12")
    component.registerForm.controls['passwordConfirm'].setValue("Sifra123")
    expect(component.registerForm.valid).toBeFalsy();

  });
  it('should send post request when clicked and valid', () => {
    
    expect(component).toBeTruthy();

    var testRegistration : UserRegistration = {
      "firstName" : "TestName",
      "lastName" : "TestSurName",
      "username" : "email@gmail.com",
      "address" : "AAAAAA",
      "password" : "Sifra123",
      "passwordConfirmation" : "Sifra123",
      "roles" : ["GUEST"],
      "phoneNumber" : "0694251300"
    }

    component.registerForm.controls['name'].setValue(testRegistration.firstName)
    component.registerForm.controls['surname'].setValue(testRegistration.lastName)
    component.registerForm.controls['email'].setValue(testRegistration.username)
    component.registerForm.controls['adress'].setValue(testRegistration.address)
    component.registerForm.controls['password'].setValue(testRegistration.password)
    component.registerForm.controls['passwordConfirm'].setValue(testRegistration.passwordConfirmation)
    component.registerForm.controls['phone'].setValue(testRegistration.phoneNumber)
    expect(component.registerForm.valid).toBeTruthy();
    component._register()!.subscribe((res) => {
      expect(res).toEqual(testRegistration);
    }
    )
    const req = httpClient.expectOne({
      method : 'POST',
      url : `${url}/signup`
    })
  });
  
});

//name
//surname
//address
//email
//password