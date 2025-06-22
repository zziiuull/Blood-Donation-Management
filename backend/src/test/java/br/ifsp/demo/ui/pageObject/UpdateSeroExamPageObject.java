package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UpdateSeroExamPageObject extends BasePageObject {

    private final By hepatitisBSelect = By.id("hepatitisb-select");
    private final By hepatitisCSelect = By.id("hepatitisc-select");
    private final By chagasDiseaseSelect = By.id("chagasDisease-select");
    private final By syphilisSelect = By.id("syphilis-select");
    private final By aidsSelect = By.id("aids-select");
    private final By htlvSelect = By.id("htlv1_2-select");
    private final By observationsTextarea = By.id("observations-textarea");
    private final By rejectButton = By.id("reject-button");
    private final By approveButton = By.id("approve-button");

    public UpdateSeroExamPageObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/exams")) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }

    public void selectHepatitisBOption(String optionText) {
        WebElement selectElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(hepatitisBSelect));
        Select select = new Select(selectElement);
        select.selectByVisibleText(optionText);
    }

    public void selectHepatitisCOption(String optionText) {
        WebElement selectElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(hepatitisCSelect));
        Select select = new Select(selectElement);
        select.selectByVisibleText(optionText);
    }

    public void selectChagasDiseaseOption(String optionText) {
        WebElement selectElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(chagasDiseaseSelect));
        Select select = new Select(selectElement);
        select.selectByVisibleText(optionText);
    }

    public void selectSyphilisOption(String optionText) {
        WebElement selectElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(syphilisSelect));
        Select select = new Select(selectElement);
        select.selectByVisibleText(optionText);
    }

    public void selectAidsOption(String optionText) {
        WebElement selectElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(aidsSelect));
        Select select = new Select(selectElement);
        select.selectByVisibleText(optionText);
    }
}
