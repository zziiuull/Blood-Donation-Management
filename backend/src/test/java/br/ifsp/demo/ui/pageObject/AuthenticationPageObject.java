package br.ifsp.demo.ui.pageObject;

import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AuthenticationPageObject extends BasePageObject {
    private static final Faker faker = new Faker();
    private static final String PAGE_URL = "login";

    public AuthenticationPageObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/login")) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }

    public DonationPageObject authenticateWithCredentials(String username, String password) {
        driver.findElement(By.id("email-input")).sendKeys(username);
        driver.findElement(By.id("password-input")).sendKeys(password);
        driver.findElement(By.xpath("//button[contains(text(), 'Login')]")).click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.urlContains("/donation"));
        return new DonationPageObject(driver);
    }

    public void authenticateWithRandomCredentials() {
        driver.findElement(By.id("email-input")).sendKeys(faker.internet().emailAddress());
        driver.findElement(By.id("password-input")).sendKeys(faker.internet().password(8, 16));
        driver.findElement(By.xpath("//button[contains(text(), 'Login')]")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector(".text-sm.me-4.font-medium.text-danger-600")));
    }

    public String authenticateWithEmptyCredentials() {
        driver.findElement(By.xpath("//button[contains(text(), 'Login')]")).click();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement errorContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("[data-slot='error-message']")));
            return errorContainer.getText();
        } catch (TimeoutException e) {
            return "";
        }
    }

    public boolean toggleDoublePasswordVisibility() {
        WebElement passwordInput = driver.findElement(By.id("password-input"));
        passwordInput.clear();

        String password = faker.internet().password(8, 16);
        passwordInput.sendKeys(password);

        WebElement toggleButton = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='toggle password visibility']")));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        toggleButton.click();
        boolean firstToggle = wait.until(d -> passwordInput.getAttribute("type").equals("text"));

        toggleButton = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='toggle password visibility']")));
        toggleButton.click();
        boolean secondToggle = wait.until(d -> passwordInput.getAttribute("type").equals("password"));

        return firstToggle && secondToggle;
    }

    public RegisterPageObject navigateToRegistrationPage(){
        driver.findElement(By.linkText("Register account")).click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.urlContains("/register"));
        return new RegisterPageObject(driver);
    }

    public String pageErrorMessage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement errorContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".text-sm.me-4.font-medium.text-danger-600")));
            return errorContainer.getText();
        } catch (TimeoutException e) {
            return "";
        }
    }
}
