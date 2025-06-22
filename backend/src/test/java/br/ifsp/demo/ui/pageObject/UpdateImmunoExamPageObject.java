package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UpdateImmunoExamPageObject extends BasePageObject{

    private final By bloodTypeSelect = By.id("blood-type-select");
    private final By irregularAntibodiesSelect = By.id("irregular-antibodies-select");
    private final By observationsTextarea = By.id("observations-textarea");
    private final By rejectButton = By.id("reject-button");
    private final By approveButton = By.id("approve-button");

    public UpdateImmunoExamPageObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/exams")) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }

    public void selectBloodType(String bloodType) {
        driver.findElement(bloodTypeSelect).click();

        By optionLocator = By.xpath(String.format("//li[@role='option'][normalize-space()='%s']", bloodType));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(optionLocator)).click();
    }

    public void selectIrregularAntibodies(String antibodyStatus) {
        driver.findElement(irregularAntibodiesSelect).click();

        By optionLocator = By.xpath(String.format("//li[@role='option'][normalize-space()='%s']", antibodyStatus));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(optionLocator)).click();
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

    public UpdateImmunoExamPageObject clickApproveButtonAndExpectFailure() {
        driver.findElement(approveButton).click();

        return this;
    }

    public DonationPageObject clickRejectButton() {
        driver.findElement(rejectButton).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/donation"));

        return new DonationPageObject(driver);
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
