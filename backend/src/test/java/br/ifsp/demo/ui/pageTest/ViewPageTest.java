package br.ifsp.demo.ui.pageTest;

import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import br.ifsp.demo.infrastructure.repository.donation.DonationRepository;
import br.ifsp.demo.presentation.security.auth.AuthenticationService;
import br.ifsp.demo.presentation.security.auth.RegisterUserRequest;
import br.ifsp.demo.presentation.security.user.JpaUserRepository;
import br.ifsp.demo.ui.pageObject.AuthenticationPageObject;
import br.ifsp.demo.ui.pageObject.DonationPageObject;
import br.ifsp.demo.ui.pageObject.ViewImmunohematologyObject;
import br.ifsp.demo.ui.pageObject.ViewSerologicalObject;
import com.github.javafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ViewPageTest extends BaseSeleniumTest{
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
    @Autowired
    DonationRepository donationRepository;
    @Autowired
    AppointmentRepository appointmentRepository;

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

        donationPage = authPage.authenticateWithCredentials(email, password);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteById(userId);
        super.tearDown();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Click on view immunohematology and confirm informations")
    void clickOnViewImmunohematologyAndConfirmInformations(){
        String donorName = "Ana Beatriz";
        String immunoObs = "immuno obs";
        String seroObs = "sero obs";

        donationPage.updateExams(donorName, "A POS", immunoObs, seroObs);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(
                By.id("view-tab")
        ));
        donationPage.viewDonationRegistered(donorName);
        ViewImmunohematologyObject viewPage = donationPage.cickOnViewImmunohematologyButton();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(viewPage.status()).isEqualTo("Status: APPROVED");
        softly.assertThat(viewPage.bloodType()).isEqualTo("Blood type: A +");
        softly.assertThat(viewPage.irregularAntibodies()).isEqualTo("Irregular Antibodies: Negative");
        softly.assertThat(viewPage.observations()).isEqualTo("Observations: " + immunoObs);
        softly.assertAll();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Click on view serological and confirm informations")
    void clickOnViewSerologicalAndConfirmInformations(){
        String donorName = "Ana Beatriz";
        String immunoObs = "immuno obs";
        String seroObs = "sero obs";

        donationPage.updateExams(donorName, "A POS", immunoObs, seroObs);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(
                By.id("view-tab")
        ));
        donationPage.viewDonationRegistered(donorName);
        ViewSerologicalObject viewPage = donationPage.cickOnViewSerologicalButton();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(viewPage.status()).isEqualTo("Status: APPROVED");
        softly.assertThat(viewPage.hepatitisB()).isEqualTo("Hepatitis B: NEGATIVE");
        softly.assertThat(viewPage.hepatitisC()).isEqualTo("Hepatitis C: NEGATIVE");
        softly.assertThat(viewPage.chagasDisease()).isEqualTo("Chagas Disease: NEGATIVE");
        softly.assertThat(viewPage.syphilis()).isEqualTo("Syphilis: NEGATIVE");
        softly.assertThat(viewPage.aids()).isEqualTo("AIDS: NEGATIVE");
        softly.assertThat(viewPage.htlv()).isEqualTo("HTLV 1/2: NEGATIVE");
        softly.assertThat(viewPage.observations()).isEqualTo("Observations: " + seroObs);
        softly.assertAll();
    }
}
