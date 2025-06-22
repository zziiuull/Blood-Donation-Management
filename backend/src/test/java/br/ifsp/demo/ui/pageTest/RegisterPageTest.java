package br.ifsp.demo.ui.pageTest;

import br.ifsp.demo.presentation.security.user.JpaUserRepository;
import br.ifsp.demo.presentation.security.user.User;
import br.ifsp.demo.ui.pageObject.RegisterPageObject;
import com.github.javafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegisterPageTest extends BaseSeleniumTest{
    private RegisterPageObject registerPageObject;
    private Faker faker = Faker.instance();

    @Autowired
    JpaUserRepository userRepository;

    @BeforeEach
    void setUpRegisterPage(){
        this.registerPageObject = new RegisterPageObject(driver);
    }

    @AfterEach
    public void tearDown(){
        super.tearDown();
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
        String email = faker.internet().emailAddress();
        registerPageObject.register(
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

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/login"));

        assertThat(driver.getCurrentUrl()).contains("/login");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        optionalUser.ifPresent(user -> userRepository.delete(user));
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
    
    @Test
    @Tag("UiTest")
    @DisplayName("Should back to login page when click on login button")
    void shouldBackToLoginPageWhenClickOnLoginButton(){
        registerPageObject.backToLogin();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/login"));

        assertThat(driver.getCurrentUrl()).contains("/login");
    }

    @Tag("UiTest")
    @ParameterizedTest(name = "Field: {0}")
    @CsvSource({
            "'name-input',   '12345', 'Nome inválido'",
            "'lastname-input',   '12345', 'Último nome inválido'",
            "'cpf-input',   'test', 'CPF inválido'",
            "'cpf-input',   '1', 'CPF inválido'",
            "'phone-input',   'test', 'Phone inválido'",
            "'address-input',   '1', 'Address inválido'",
            "'crm-input',   'test', 'CRM inválido'",
            "'crm-input',   '1', 'CRM inválido'",
            "'email-input',   'email', 'Inclua um \"@\" no endereço de e-mail.'",
            "'email-input',   'email@', 'Insira uma parte depois de \"@\".'",
            "'password-input',     'a',          'Senha inválida'"
    })
    @DisplayName("Should display error message when a field is filled with invalid data")
    void shouldDisplayErrorMessageWhenAFieldIsFilledWithInvalidData(String inputId, String invalidData, String expectedMessage){
        String validFirstName = faker.name().firstName();
        String validLastName = faker.name().lastName();
        String validCpf = "12345678910";
        String validPhone = faker.phoneNumber().cellPhone();
        String validAddress = faker.address().fullAddress();
        String validCrm = "123456";
        String validState = "São Paulo";
        String validEmail = faker.internet().emailAddress();
        String validPassword = faker.internet().password();

        if (!inputId.equals("name-input")) registerPageObject.fillName(validFirstName);
        if (!inputId.equals("lastname-input")) registerPageObject.fillLastName(validLastName);
        if (!inputId.equals("cpf-input")) registerPageObject.fillCpf(validCpf);
        if (!inputId.equals("phone-input")) registerPageObject.fillPhone(validPhone);
        if (!inputId.equals("address-input")) registerPageObject.fillAddress(validAddress);
        if (!inputId.equals("crm-input")) registerPageObject.fillCrm(validCrm);
        if (!inputId.equals("email-input")) registerPageObject.fillEmail(validEmail);
        if (!inputId.equals("password-input")) registerPageObject.fillPassword(validPassword);

        registerPageObject.selectState(validState);

        By input = By.id(inputId);

        driver.findElement(input).sendKeys(invalidData);

        registerPageObject.clickRegisterButton();

        assertThat(driver.getCurrentUrl())
                .withFailMessage("Test failed: Current URL does not contain '/register'. This likely means the form was submitted and accepted even with invalid input, causing an unexpected redirect (e.g., to login page).")
                .contains("/register");

        String errorMessage = registerPageObject.getErrorMessageFor(inputId);

        assertThat(errorMessage)
                .withFailMessage("Test failed: No error message was found for input field '%s' after submitting invalid value '%s'. Expected a validation error on the register page.", inputId, invalidData)
                .isNotBlank();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Should warning email already registered")
    void shouldWarningEmailAlreadyRegistered(){
        String email = faker.internet().emailAddress();
        registerPageObject.register(
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
        String errorMessage = registerPageObject.emailErrorMessage(email);
        assertThat(errorMessage).isEqualTo("Email already registered: " + email);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        optionalUser.ifPresent(user -> userRepository.delete(user));
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Should toggle password visibility on and off when clicking twice")
    public void shouldTogglePasswordVisibilityOnAndOffWhenClickingTwice() {
        boolean toggleWorks = registerPageObject.toggleDoublePasswordVisibility();
        assertThat(toggleWorks)
                .withFailMessage("Expected password visibility to toggle ON (type='text') and then OFF (type='password') after clicking the toggle button twice, but it did not.")
                .isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Should not be vulnerable to SQL Injection in Name field during registration")
    void shouldNotBeVulnerableToSqlInjectionInNameField() {
        String maliciousName = "Test' OR '1'='1";
        String email = faker.internet().emailAddress();

        registerPageObject.register(
                maliciousName,
                faker.name().lastName(),
                "12345678910",
                faker.phoneNumber().cellPhone(),
                faker.address().fullAddress(),
                "123456",
                "São Paulo",
                email,
                faker.internet().password()
        );

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/login"));
        assertThat(driver.getCurrentUrl()).contains("/login");
    }

    @Nested
    class Responsivity {

        static Stream<Dimension> screenSizes() {
            return Stream.of(
                    new Dimension(320, 800),
                    new Dimension(375, 800),
                    new Dimension(400, 800),
                    new Dimension(480, 800),
                    new Dimension(580, 800),
                    new Dimension(720, 800)
            );
        }

        @ParameterizedTest(name = "Test placeholders at screen size {0}")
        @MethodSource("screenSizes")
        @DisplayName("Should be able to see placeholder fields on register form")
        void shouldBeAbleToSeePlaceholderFieldsOnRegisterForm(Dimension screenSize) {
            driver.manage().window().setSize(screenSize);

            String[] inputIds = {
                    "name-input", "lastname-input", "cpf-input", "phone-input",
                    "address-input", "crm-input", "email-input", "password-input"
            };


            SoftAssertions softly = new SoftAssertions();
            for (String inputId : inputIds) {
                boolean isClipped = registerPageObject.isPlaceholderLikelyClipped(inputId);
                softly.assertThat(isClipped)
                        .withFailMessage("Placeholder is visually clipped in field with id: %s (at size %dx%d)",
                                inputId, screenSize.getWidth(), screenSize.getHeight())
                        .isFalse();
            }
            softly.assertAll();
        }
    }
}
