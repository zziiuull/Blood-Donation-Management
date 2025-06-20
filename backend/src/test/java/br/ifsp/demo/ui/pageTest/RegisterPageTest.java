package br.ifsp.demo.ui.pageTest;

import br.ifsp.demo.ui.pageObject.RegisterPageObject;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RegisterPageTest extends BaseSeleniumTest{
    private RegisterPageObject registerPageObject;
    private Faker faker = Faker.instance();

    @BeforeEach
    void setUpRegisterPage(){
        this.registerPageObject = new RegisterPageObject(driver);
    }

    @Override
    protected void setInitialPage(){
        String page = "http://localhost:3000/register";
        driver.get(page);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Should register and back to login page")
    void shouldRegisterAndBackToLoginPage(){
        registerPageObject.register(
                faker.name().firstName(),
                faker.name().lastName(),
                "12345678910",
                faker.phoneNumber().cellPhone(),
                faker.address().fullAddress(),
                "12345",
                "São Paulo",
                faker.internet().emailAddress(),
                faker.internet().password()
        );

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.urlContains("/login"));

        assertThat(driver.getCurrentUrl()).contains("/login");
    }
}
