package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UpdateSeroExamPageObject extends BasePageObject {

    private final By observationsTextarea = By.xpath("//label[text()='Observations']/following::textarea[1]");
    private final By rejectButton = By.id("reject-button");
    private final By approveButton = By.id("approve-button");

    public UpdateSeroExamPageObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/exams/serological-screening/")) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }

    public void selectDiseaseStatus(String diseaseKey, String status) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        By diseaseSelectButton = By.id(diseaseKey + "-select");
        WebElement buttonElement = driver.findElement(diseaseSelectButton);

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", buttonElement);

        wait.until(ExpectedConditions.elementToBeClickable(buttonElement)).click();

        By optionLocator = By.xpath(String.format("//li[@role='option'][normalize-space()='%s']", status));
        WebElement optionToClick = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));

        executor.executeScript("arguments[0].click();", optionToClick);
    }

    public void fillObservations(String text) {
        WebElement textarea = driver.findElement(observationsTextarea);
        textarea.clear();
        textarea.sendKeys(text);
    }

    public DonationPageObject clickApproveButtonAndExpectSuccess() {
        driver.findElement(approveButton).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/donation"));

        return new DonationPageObject(driver);
    }

    public UpdateSeroExamPageObject clickApproveButtonAndExpectFailure() {
        driver.findElement(approveButton).click();

        return this;
    }

    public DonationPageObject clickRejectButtonAndExpectSucceess() {
        driver.findElement(rejectButton).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/donation"));

        return new DonationPageObject(driver);
    }

    public UpdateSeroExamPageObject clickRejectButtonAndExpectFailure() {
        driver.findElement(rejectButton).click();

        return this;
    }

    public String getUpdateExamErrorToastText() {
        By errorToastLocator = By.xpath("//*[text()='Error updating exam']");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorToastLocator));
            return toastElement.getText();
        } catch (TimeoutException e) {
            System.out.println("Não foi encontrada a notificação de erro 'Error updating exam'.");
            return "";
        }
    }
}
