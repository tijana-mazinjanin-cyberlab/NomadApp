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
public class LoginPage {
    private WebDriver driver;

    private static String PAGE_URL="http://localhost:8081/login";

    @FindBy(xpath = "//input[@formcontrolname='username']")
    private WebElement userNameElement;

    @FindBy(xpath = "//input[@formcontrolname='password']")
    private WebElement passwordElement;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    public LoginPage(WebDriver driver){
        this.driver=driver;
        driver.get(PAGE_URL);

        PageFactory.initElements(driver, this);
    }
    public void login(String username, String password){
        userNameElement.sendKeys(username);
        passwordElement.sendKeys(password);
        (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }


}
