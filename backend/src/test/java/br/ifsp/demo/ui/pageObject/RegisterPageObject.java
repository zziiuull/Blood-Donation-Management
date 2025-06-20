package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPageObject extends BasePageObject {

    private final By nameInput = By.id("name-input");
    private final By lastnameInput = By.id("lastname-input");
    private final By cpfInput = By.id("cpf-input");
    private final By phoneInput = By.id("phone-input");
    private final By addressInput = By.id("address-input");
    private final By crmInput = By.id("crm-input");
    private final By stateSelectButton = By.id("state-select");
    private final By emailInput = By.id("email-input");
    private final By passwordInput = By.id("password-input");
    private final By registerButton = By.id("register-button");
    private final By loginLink = By.id("login-link");
    private final By eyeButton = By.cssSelector("button[aria-label='toggle password visibility']");


    public RegisterPageObject(WebDriver driver) {
        super(driver);
    }

    public void register(String name, String lastName, String cpf, String phone,
                         String address, String crm, String state, String email, String password) {

        driver.findElement(nameInput).sendKeys(name);
        driver.findElement(lastnameInput).sendKeys(lastName);
        driver.findElement(cpfInput).sendKeys(cpf);
        driver.findElement(phoneInput).sendKeys(phone);
        driver.findElement(addressInput).sendKeys(address);
        driver.findElement(crmInput).sendKeys(crm);
        selectState(state);
        driver.findElement(emailInput).sendKeys(email);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(registerButton).click();
    }

    private void selectState(String stateText) {
        driver.findElement(stateSelectButton).click();

        By stateOption = By.xpath(String.format("//li[@role='option'][normalize-space()='%s']", stateText));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement optionToClick = wait.until(ExpectedConditions.visibilityOfElementLocated(stateOption));

        Actions actions = new Actions(driver);
        actions.moveToElement(optionToClick).click().perform();
    }

    public void backToLogin() {
        driver.findElement(loginLink).click();
    }

    public void togglePasswordVisibility() {
        driver.findElement(eyeButton).click();
    }
}
