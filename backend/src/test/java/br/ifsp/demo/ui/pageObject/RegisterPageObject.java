package br.ifsp.demo.ui.pageObject;

import com.github.javafaker.Faker;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPageObject extends BasePageObject {
    private static final Faker faker = Faker.instance();

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

    public void selectState(String stateText) {
        driver.findElement(stateSelectButton).click();

        By stateOption = By.xpath(String.format("//li[@role='option'][normalize-space()='%s']", stateText));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement optionToClick = wait.until(ExpectedConditions.visibilityOfElementLocated(stateOption));

        Actions actions = new Actions(driver);
        actions.moveToElement(optionToClick).click().perform();
    }

    public void fillName(String name) {
        driver.findElement(nameInput).sendKeys(name);
    }

    public void fillLastName(String lastName) {
        driver.findElement(lastnameInput).sendKeys(lastName);
    }

    public void fillCpf(String cpf) {
        driver.findElement(cpfInput).sendKeys(cpf);
    }

    public void fillPhone(String phone) {
        driver.findElement(phoneInput).sendKeys(phone);
    }

    public void fillAddress(String address) {
        driver.findElement(addressInput).sendKeys(address);
    }

    public void fillCrm(String crm) {
        driver.findElement(crmInput).sendKeys(crm);
    }

    public void fillEmail(String email) {
        driver.findElement(emailInput).sendKeys(email);
    }

    public void fillPassword(String password) {
        driver.findElement(passwordInput).sendKeys(password);
    }

    public void clickRegisterButton(){
        driver.findElement(registerButton).click();
    }

    public String getErrorMessageFor(String inputId) {
        String errorMsgXpath = String.format(
                "//input[@id='%s']/ancestor::div[contains(@class, 'relative')]/following-sibling::div//div[contains(@class, 'text-danger')]",
                inputId
        );

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(errorMsgXpath))).getText();
        } catch (TimeoutException e) {
            return "";
        }
    }

    public String getStateSelectionErrorMessage() {
        By stateErrorLocator = By.xpath("//*[contains(text(), 'Selecione um item da lista.')]");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement errorMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(stateErrorLocator));

        return errorMessageElement.getText();
    }

    public void backToLogin() {
        driver.findElement(loginLink).click();
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

    public String emailErrorMessage(String email) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement registerLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Register account")));
        registerLink.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name-input")));
        try {
            register(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    "12345678910",
                    faker.phoneNumber().cellPhone(),
                    faker.address().fullAddress(),
                    "12345",
                    "São Paulo",
                    email,
                    faker.internet().password()
            );
            wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            String xpath = String.format("//*[text()='Email already registered: %s']", email);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).getText();
        } catch (TimeoutException e) {
            return "";
        }
    }

    public boolean isPlaceholderLikelyClipped(String id) {
        WebElement input = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));

        String placeholder = input.getAttribute("placeholder");
        if (placeholder == null || placeholder.isEmpty()) {
            return false;
        }

        String fontSizeStr = input.getCssValue("font-size");
        int fontSizePx = Integer.parseInt(fontSizeStr.replace("px", "").trim());

        int estimatedTextWidth = placeholder.length() * fontSizePx / 2;
        int inputWidth = input.getSize().width;

        return estimatedTextWidth > inputWidth;
    }
}
