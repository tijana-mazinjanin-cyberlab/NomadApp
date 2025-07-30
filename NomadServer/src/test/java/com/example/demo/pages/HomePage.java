package com.example.demo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
public class HomePage {
    private WebDriver driver;


    @FindBy(xpath = "//div/p[contains(text(), 'Listing')]/..")
    private WebElement listingButton;
    @FindBy(xpath = "//app-accommodation-card-host/div/button[contains(text(), 'Edit')]")
    private WebElement editButton;

    public HomePage(WebDriver driver){
        this.driver=driver;

        PageFactory.initElements(driver, this);
    }

    public void clickListing(){
        (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.elementToBeClickable(listingButton)).click();
    }
    public void clickEdit(){
        (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.elementToBeClickable((WebElement) editButton)).click();
    }
}
