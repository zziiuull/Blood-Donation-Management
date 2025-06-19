package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class RegisterPageObject extends BasePageObject{
    private static final String PAGE_TITLE = "Register";
    private final WebElement nameInput = driver.findElement(By.id("name-input"));
    private final WebElement lastNameInput = driver.findElement(By.id("lastname-input"));
    private final WebElement cpfInput = driver.findElement(By.id("cpf-input"));
    private final WebElement phoneInput = driver.findElement(By.id("phone-input"));
    private final WebElement addressInput = driver.findElement(By.id("address-input"));
    private final WebElement crmInput = driver.findElement(By.id("crm-input"));
    private final WebElement emailInput = driver.findElement(By.id("email-input"));
    private final WebElement passwordInput = driver.findElement(By.id("password-input"));
    private final Select stateSelect = new Select(driver.findElement(By.id("react-aria2658155816-«r1f»")));
    private final WebElement registerButton = driver.findElement(By.id("register-button"));
    private final WebElement registerTitle = driver.findElement(By.id("register-title"));
    private final WebElement loginButton = driver.findElement(By.id("login-link"));
    private final WebElement eyeButton = driver.findElement(By.cssSelector("button[aria-label='toggle password visibility']"));

    public RegisterPageObject(WebDriver driver) { super(driver); }

    public void register(String name, String lastName, String CPF, String phone,
                         String address, String crm, int state, String email, String password) {
        nameInput.sendKeys(name);
        lastNameInput.sendKeys(lastName);
        cpfInput.sendKeys(CPF);
        phoneInput.sendKeys(phone);
        addressInput.sendKeys(address);
        crmInput.sendKeys(crm);
        stateSelect.selectByIndex(state);
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);

        registerButton.click();
    }

    public String getPageTitle(){
        return registerTitle.getText();
    }

    public void backToLogin(){
        loginButton.click();
    }

    public void togglePasswordVisibility(){
        eyeButton.click();
    }
}
