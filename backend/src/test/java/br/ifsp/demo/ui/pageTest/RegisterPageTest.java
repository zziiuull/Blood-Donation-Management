package br.ifsp.demo.ui.pageTest;

import br.ifsp.demo.ui.pageObject.RegisterPageObject;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.stream.Stream;

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

    private static Stream<Arguments> provideRequiredFields() {
        return Stream.of(
                Arguments.of("name-input"),
                Arguments.of("lastname-input"),
                Arguments.of("phone-input"),
                Arguments.of("address-input"),
                Arguments.of("crm-input"),
                Arguments.of("email-input"),
                Arguments.of("password-input")
        );
    }

    @Tag("UiTest")
    @DisplayName("Should display error when a required field is left blank on submit")
    @ParameterizedTest(name = "Test validation for empty field: {0}")
    @MethodSource("provideRequiredFields")
    void shouldDisplayErrorWhenFieldIsLeftBlankOnSubmit(String blankFieldId) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String cpf = "12345678910";
        String phone = faker.phoneNumber().cellPhone();
        String address = faker.address().fullAddress();
        String crm = "123456";
        String state = "São Paulo";
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();

        if (!blankFieldId.equals("name-input")) registerPageObject.fillName(firstName);
        if (!blankFieldId.equals("lastname-input")) registerPageObject.fillLastName(lastName);
        if (!blankFieldId.equals("cpf-input")) registerPageObject.fillCpf(cpf);
        if (!blankFieldId.equals("phone-input")) registerPageObject.fillPhone(phone);
        if (!blankFieldId.equals("address-input")) registerPageObject.fillAddress(address);
        if (!blankFieldId.equals("crm-input")) registerPageObject.fillCrm(crm);
        if (!blankFieldId.equals("email-input")) registerPageObject.fillEmail(email);
        if (!blankFieldId.equals("password-input")) registerPageObject.fillPassword(password);

        registerPageObject.selectState(state);

        registerPageObject.clickRegisterButton();

        String errorMessage = registerPageObject.getErrorMessageFor(blankFieldId);
        assertThat(errorMessage).isEqualTo("Preencha este campo.");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Should display error message when state is not selected on submit")
    void shouldDisplayErrorWhenStateIsNotSelectedOnSubmit() {
        registerPageObject.fillName(faker.name().firstName());
        registerPageObject.fillLastName(faker.name().lastName());
        registerPageObject.fillCpf("12345678910");
        registerPageObject.fillPhone(faker.phoneNumber().cellPhone());
        registerPageObject.fillAddress(faker.address().fullAddress());
        registerPageObject.fillCrm("123456");
        registerPageObject.fillEmail(faker.internet().emailAddress());
        registerPageObject.fillPassword(faker.internet().password());

        registerPageObject.clickRegisterButton();

        assertThat(registerPageObject.getStateSelectionErrorMessage()).isEqualTo("Selecione um item da lista.");
    }
}
