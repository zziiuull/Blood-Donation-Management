package br.ifsp.demo.ui.pageTest;

import br.ifsp.demo.presentation.security.auth.AuthenticationService;
import br.ifsp.demo.presentation.security.auth.RegisterUserRequest;
import br.ifsp.demo.presentation.security.user.JpaUserRepository;
import br.ifsp.demo.ui.pageObject.AuthenticationPageObject;
import br.ifsp.demo.ui.pageObject.DonationPageObject;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DonationPageTest extends BaseSeleniumTest {
    private static final Faker faker = new Faker();
    private String email;
    private String password;
    private UUID userId;
    private AuthenticationPageObject authPage;
    private DonationPageObject donationPage;

    @Autowired
    JpaUserRepository userRepository;
    @Autowired
    AuthenticationService authenticationService;

    @Override
    protected void setInitialPage() {
        String page = "http://localhost:3000/login";
        driver.get(page);
    }

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
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
    public void tearDown() {
        userRepository.deleteById(userId);
        super.tearDown();
    }

    @Nested
    class Navigate {
        @Test
        @Tag("UiTest")
        @DisplayName("Should navigate to login if user is not logged in")
        void shouldNavigateToLoginIfUserIsNotLoggedIn() {
            ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");

            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.urlContains("/login"));
            assertThat(driver.getCurrentUrl()).contains("/login");
        }

        @Test
        @Tag("UiTest")
        @DisplayName("Should navigate to donation page when user login")
        void shouldNavigateToDonationPageWhenUserLogin(){
            donationPage = authPage.authenticateWithCredentials(email, password);
            assertThat(donationPage.pageUrl()).contains("/donation");
        }
    }

    @Nested
    class MenuButtons {
        @Test
        @Tag("UiTest")
        @DisplayName("Should logout when click on logout button")
        void shouldLogoutWhenClickOnLogoutButton(){
            donationPage = authPage.authenticateWithCredentials(email, password);
            AuthenticationPageObject authenticationPageObject = donationPage.logout();
            assertThat(authenticationPageObject.pageUrl()).contains("/login");
        }
        
        @Test
        @Tag("UiTest")
        @DisplayName("Should stay on donation page when click on BDM button")
        void shouldStayOnDonationPageWhenClickOnBdmButton(){
            donationPage = authPage.authenticateWithCredentials(email, password);
            DonationPageObject donationPageObject = donationPage.clickOnBDMButton();
            assertThat(donationPageObject.pageUrl()).contains("/donation");
        }
    }
    
    @Nested
    class RegisterDonation {
        
        @Test
        @Tag("UiTest")
        @DisplayName("Should stay on register donation page when click on register donation")
        void shouldStayOnRegisterDonationPageWhenClickOnRegisterDonation(){
            donationPage = authPage.authenticateWithCredentials(email, password);
            donationPage.clickOnRegisterTabButton();
            assertThat(donationPage.isRegisterDonationStepOneTextVisible()).isTrue();
        }
    }

    @Nested
    class UpdateDonation {

        @Test
        @Tag("UiTest")
        @DisplayName("Should go to update donation when click on update donation")
        void shouldGoToUpdateDonationWhenClickOnUpdateDonation(){
            donationPage = authPage.authenticateWithCredentials(email, password);
            donationPage.clickOnUpdateTabButton();
            assertThat(donationPage.isUpdateDonationSelectButtonVisible()).isTrue();
        }
    }

    @Nested
    class ViewDonation {
        @Test
        @Tag("UiTest")
        @DisplayName("Should go to view donation when click on view donation")
        void shouldGoToViewDonationWhenClickOnViewDonation(){
            donationPage = authPage.authenticateWithCredentials(email, password);
            donationPage.clickOnViewTabButton();
            assertThat(donationPage.isViewDonationSelectButtonVisible()).isTrue();
        }
    }
}
