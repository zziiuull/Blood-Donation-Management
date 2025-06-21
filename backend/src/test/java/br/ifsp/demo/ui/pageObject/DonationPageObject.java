package br.ifsp.demo.ui.pageObject;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Slf4j
public class DonationPageObject extends BasePageObject {

    private final By registerTabButton = By.id("register-tab");
    private final By registerDonationStepOneText = By.xpath("//h3[contains(text(), 'Select a donor')]");
    private final By updateTabButton = By.id("update-tab");
    private final By donationAutocompleteSelectButton = By.id("donation-autocomplete");
    private final By viewTabButton = By.id("view-tab");
    private final By activeUpdateTabLocator = By.xpath("//button[@id='update-tab' and @aria-selected='true']");
    private final By activeViewTabLocator = By.xpath("//button[@id='view-tab' and @aria-selected='true']");
    private final By viewDonationSelectButton = By.id("donation-autocomplete");

    public DonationPageObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/donation")) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }

    public AuthenticationPageObject logout() {
        WebElement logoutButton = driver.findElement(By.id("logout-desktop-button"));
        logoutButton.click();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.urlContains("/login"));
        return new AuthenticationPageObject(driver);
    }

    public DonationPageObject clickOnBDMButton() {
        WebElement button = driver.findElement(By.xpath("//*[@id=\"navbar-link-home\"]/p"));
        button.click();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.urlContains("/donation"));
        return new DonationPageObject(driver);
    }

    public void clickOnRegisterTabButton() {
        driver.findElement(registerTabButton).click();
    }

    public boolean isRegisterDonationStepOneTextVisible() {
        List<WebElement> elements = driver.findElements(registerDonationStepOneText);

        return !elements.isEmpty();
    }

    public void clickOnUpdateTabButton() {
        driver.findElement(updateTabButton).click();
    }

    public boolean isViewDonationSelectButtonVisible() {
        List<WebElement> elements = driver.findElements(viewDonationSelectButton);
        return !elements.isEmpty();
    }

    public void clickOnViewTabButton() {
        driver.findElement(viewTabButton).click();
    }

    public boolean isUpdateTabActive() {
        return !driver.findElements(activeUpdateTabLocator).isEmpty();
    }

    public boolean isViewTabActive() {
        return !driver.findElements(activeViewTabLocator).isEmpty();
    }
}
