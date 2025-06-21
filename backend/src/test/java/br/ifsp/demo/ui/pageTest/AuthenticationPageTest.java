package br.ifsp.demo.ui.pageTest;

import br.ifsp.demo.presentation.security.auth.AuthenticationService;
import br.ifsp.demo.presentation.security.auth.RegisterUserRequest;
import br.ifsp.demo.presentation.security.user.JpaUserRepository;
import br.ifsp.demo.ui.pageObject.AuthenticationPageObject;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationPageTest extends BaseSeleniumTest {

    private static final Faker faker = Faker.instance();
    private String email;
    private String password;
    private UUID userId;

    @Autowired
    JpaUserRepository userRepository;
    @Autowired
    AuthenticationService authenticationService;

    AuthenticationPageObject authPage;

    @Override
    protected void setInitialPage() {
        driver.get("http://localhost:3000/login");
    }

    @BeforeEach
    void setup() {
        authPage = new AuthenticationPageObject(driver);

        email = faker.internet().emailAddress();
        password = faker.internet().password(8, 16);

        var request = new RegisterUserRequest(
                faker.name().firstName(),
                faker.name().lastName(),
                email,
                password
        );

        var registerResponse = authenticationService.register(request);
        userId = registerResponse.id();
    }

    @AfterEach
    void cleanup() {
        userRepository.deleteById(userId);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Should navigate to registration page by clicking the link")
    void shouldNavigateToRegistrationPageByClickingTheLink() {
        var registrationPage = authPage.navigateToRegistrationPage();
        assertThat(registrationPage.currentUrl()).contains("/register");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Should navigate to donation page after authenticate")
    void shouldNavigateToDonationPageAfterAuthenticate() {
        var donationPage = authPage.authenticateWithCredentials(email, password);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("/donation"));
        assertThat(donationPage.currentUrl()).contains("/donation");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Should get wrong user or password error message")
    void shouldGetWrongUserOrPasswordErrorMessage() {
        authPage.authenticateWithRandomCredentials();
        assertThat(authPage.pageErrorMessage()).isEqualTo("Invalid email and/or password");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Should warning the required fields when try authenticate with empty fields")
    void shouldWarningTheRequiredFieldsWhenTryAuthenticateWithEmptyFields(){
        String message = authPage.authenticateWithEmptyCredentials();
        assertThat(message).isNotEmpty();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Should toggle password visibility on and off when clicking twice")
    public void shouldTogglePasswordVisibilityOnAndOffWhenClickingTwice() {
        authPage = new AuthenticationPageObject(driver);
        boolean toggleWorks = authPage.toggleDoublePasswordVisibility();
        assertThat(toggleWorks)
                .withFailMessage("Expected password visibility to toggle ON (type='text') and then OFF (type='password') after clicking the toggle button twice, but it did not.")
                .isTrue();
    }
    
    @Test
    @Tag("UiTest")
    @DisplayName("Should warning when email is invalid")
    void shouldWarningWhenEmailIsInvalid(){
        assertThat(authPage.emailInvalidErrorMessage()).isNotEmpty();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Should warning when email is uncompleted")
    void shouldWarningWhenEmailIsUncompleted(){
        assertThat(authPage.emailUncompletedErrorMessage()).isNotEmpty();
    }
}