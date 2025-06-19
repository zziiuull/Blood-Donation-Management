package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class RegisterPageObject extends BasePageObject{
    private static final String PAGE_TITLE = "Register";

    public RegisterPageObject(WebDriver driver) { super(driver); }

    public void register(String name, String lastName, String CPF, String phone,
                         String address, String crm, String email, String password) {
        driver.findElement(By.id("name-input")).sendKeys(name);
        driver.findElement(By.id("lastname-input")).sendKeys(lastName);
        driver.findElement(By.id("cpf-input")).sendKeys(CPF);
        driver.findElement(By.id("phone-input")).sendKeys(phone);
        driver.findElement(By.id("address-input")).sendKeys(address);
        driver.findElement(By.id("crm-input")).sendKeys(crm);
        driver.findElement(By.id("email-input")).sendKeys(email);
        driver.findElement(By.id("password-input")).sendKeys(password);

        final Select state = new Select(driver.findElement(By.id("react-aria2658155816-«r1f»")));
        state.selectByVisibleText("Santa Catarina");

        driver.findElement(By.id("register-button")).click();
    }

    
}
