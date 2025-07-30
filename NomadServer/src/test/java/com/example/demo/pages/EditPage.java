package com.example.demo.pages;

import org.openqa.selenium.By;
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
public class EditPage {
    private WebDriver driver;
    @FindBy(xpath = "//mat-snack-bar-container//span")
    WebElement snackBar;
    @FindBy(xpath = "//mat-calendar")
    private WebElement calendar;

    @FindBy(xpath = "//button[@aria-label='Next month']")
    private WebElement nextMonthButton;
    @FindBy(xpath = "//button[@aria-label='Previous month']")
    private WebElement previousMonthButton;

    @FindBy(xpath = "//button[contains(text(),'Set Available')]")
    private WebElement makeAvailable;
    @FindBy(xpath = "//button[contains(text(),'Save')]")
    private WebElement save;
    @FindBy(xpath = "//button[contains(text(),'Confirm')]")
    private WebElement confirm;
    @FindBy(xpath = "//button[contains(text(),'Set Unavailable')]")
    private WebElement makeUnavailable;
    @FindBy(xpath = "//button[contains(text(),'Set Price for selected date range')]")
    private WebElement setPrice;

    @FindBy(css = "#selectedDateprice")
    private WebElement priceBox;

    @FindBy(css = "#deadline")
    private WebElement deadlineBox;

    public EditPage(WebDriver driver){
        this.driver=driver;

        PageFactory.initElements(driver, this);
    }
    public void selectDateRange(String start, String end){
        clickDate(start);
        clickDate(end);
    }
    private void clickDate(String date){
        (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.elementToBeClickable(calendar.findElement(By.xpath("//button[@aria-label='" + date + "']")))).click();
    }
    public void clickMakeAvailable(){
        (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.elementToBeClickable(makeAvailable)).click();
    }
    public void clickMakeUnavailable(){
        (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.elementToBeClickable(makeUnavailable)).click();
    }
    public void clickSetPrice(){
        (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.elementToBeClickable(setPrice)).click();
    }
    public void clickNextMonth(){
        (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.elementToBeClickable(nextMonthButton)).click();
    }
    public void clickPreviousMonth(){
        (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.elementToBeClickable(previousMonthButton)).click();
    }
    public boolean operationSuccess(){
        return (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.textToBePresentInElement(snackBar, "SUCCESS"));
    }
    public boolean operationFail(){
        return (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.textToBePresentInElement(snackBar, "WARNING"));
    }
    public void setDatesAvailable(String start, String end){
        selectDateRange(start, end);
        clickMakeAvailable();
    }
    public void setDatesPrice(String start, String end, String price){
        selectDateRange(start, end);
        priceBox.clear();
        priceBox.sendKeys(price);
        clickSetPrice();
    }
    public void setDeadline(String deadline){
        deadlineBox.clear();
        deadlineBox.sendKeys(deadline);
    }
    public void setDatesUnavailable(String start, String end){
        selectDateRange(start, end);
        clickMakeUnavailable();
    }
    public boolean isDateUnavailable(String date){
        return (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.attributeContains(calendar.findElement(By.xpath("//button[@aria-label='" + date + "']")),
                        "ng-reflect-ng-class", "special-date"));
    }
    public boolean isDateAvailable(String date){
        return (new WebDriverWait(driver, Duration.of(1, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.not(ExpectedConditions.attributeContains(calendar.findElement(By.xpath("//button[@aria-label='" + date + "']")),
                        "ng-reflect-ng-class", "special-date")));
    }
    public boolean checkNotSelectedDate(String date){
        return (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.attributeContains(calendar.findElement(By.xpath("//button[@aria-label='" + date + "']")),
                        "aria-pressed", "false"));
    }
    public void saveChanges(){
        (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.elementToBeClickable(save)).click();
        (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.elementToBeClickable(confirm)).click();
    }
    public void clickSave() {
        (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.elementToBeClickable(save)).click();
    }


}
