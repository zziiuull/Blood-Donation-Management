package br.ifsp.demo.ui.pageTest;

import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.AppointmentStatus;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import br.ifsp.demo.infrastructure.repository.donation.DonationRepository;
import br.ifsp.demo.presentation.security.auth.AuthenticationService;
import br.ifsp.demo.presentation.security.auth.RegisterUserRequest;
import br.ifsp.demo.presentation.security.user.JpaUserRepository;
import br.ifsp.demo.ui.pageObject.AuthenticationPageObject;
import br.ifsp.demo.ui.pageObject.DonationPageObject;
import com.github.javafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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
        void shouldNavigateToDonationPageWhenUserLogin() {
            donationPage = authPage.authenticateWithCredentials(email, password);
            assertThat(donationPage.pageUrl()).contains("/donation");
        }

        @Test
        @Tag("UiTest")
        @DisplayName("Should remain logged in when navigating to the page again")
        void shouldRemainLoggedInOnPageRevisit() {
            DonationPageObject donationPage = authPage.authenticateWithCredentials(email, password);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlContains("/donation"));

            driver.get("http://localhost:3000/donation");

            assertThat(donationPage.isDonationTitleVisible()).isTrue();
        }
    }

    @Nested
    class MenuButtons {
        @Test
        @Tag("UiTest")
        @DisplayName("Should logout when click on logout button")
        void shouldLogoutWhenClickOnLogoutButton() {
            donationPage = authPage.authenticateWithCredentials(email, password);
            AuthenticationPageObject authenticationPageObject = donationPage.logout();
            assertThat(authenticationPageObject.pageUrl()).contains("/login");
        }

        @Test
        @Tag("UiTest")
        @DisplayName("Should stay on donation page when click on BDM button")
        void shouldStayOnDonationPageWhenClickOnBdmButton() {
            donationPage = authPage.authenticateWithCredentials(email, password);
            DonationPageObject donationPageObject = donationPage.clickOnBDMButton();
            assertThat(donationPageObject.pageUrl()).contains("/donation");
        }
    }

    @Nested
    class RegisterDonation {

        @AfterEach
        public void tearDown() {
            donationRepository.findAll().stream()
                    .filter(donation -> donation.getCreatedAt().toLocalDate().isEqual(LocalDate.now()))
                    .reduce((first, second) -> second)
                    .ifPresent(lastDonation -> {
                        Appointment appointment = lastDonation.getAppointment();
                        appointment.setStatus(AppointmentStatus.SCHEDULED);
                        appointmentRepository.save(appointment);
                        donationRepository.deleteById(lastDonation.getId());
                    });
        }

        @Test
        @Tag("UiTest")
        @DisplayName("Should stay on register donation page when click on register donation")
        void shouldStayOnRegisterDonationPageWhenClickOnRegisterDonation() {
            donationPage = authPage.authenticateWithCredentials(email, password);
            donationPage.clickOnRegisterTabButton();
            assertThat(donationPage.isRegisterDonationStepOneTextVisible()).isTrue();
        }

        @Test
        @Tag("UiTest")
        @DisplayName("Should register a donation with immunohematology exam")
        void shouldRegisterADonationWithImmunohematologyExam() {
            donationPage = authPage.authenticateWithCredentials(email, password);
            donationPage.registerDonationWithImmunohematologyExam("Weverton");
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(donationPage.donationRequestMessage()).isEqualTo("Donation requested");
            softly.assertThat(donationPage.immunohematologyExamRequestMessage()).isEqualTo("Immunohematology exam requested");
            softly.assertAll();
        }

        @Test
        @Tag("UiTest")
        @DisplayName("Should register a donation with serological exam")
        void shouldRegisterADonationWithSerologicalExam() {
            donationPage = authPage.authenticateWithCredentials(email, password);
            donationPage.registerDonationWithSerologicalExam("Weverton");
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(donationPage.donationRequestMessage()).isEqualTo("Donation requested");
            softly.assertThat(donationPage.serologicalExamRequestMessage()).isEqualTo("Serological screening exam requested");
            softly.assertAll();
        }

        @Test
        @Tag("UiTest")
        @DisplayName("Should register a donation with all exams")
        void shouldRegisterADonationWithAllExams() {
            donationPage = authPage.authenticateWithCredentials(email, password);
            donationPage.registerDonationWithAllExams("Ana Beatriz");
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(donationPage.donationRequestMessage()).isEqualTo("Donation requested");
            softly.assertThat(donationPage.immunohematologyExamRequestMessage()).isEqualTo("Immunohematology exam requested");
            softly.assertThat(donationPage.serologicalExamRequestMessage()).isEqualTo("Serological screening exam requested");
            softly.assertAll();
        }
    }

    @Nested
    class UpdateDonation {

        @Nested
        class RegisterDonation {

            @AfterEach
            public void tearDown() {
                donationRepository.findAll().stream()
                        .filter(donation -> donation.getCreatedAt().toLocalDate().isEqual(LocalDate.now()))
                        .reduce((first, second) -> second)
                        .ifPresent(lastDonation -> {
                            Appointment appointment = lastDonation.getAppointment();
                            appointment.setStatus(AppointmentStatus.SCHEDULED);
                            appointmentRepository.save(appointment);
                            donationRepository.deleteById(lastDonation.getId());
                        });
            }

            @Test
            @Tag("UiTest")
            @DisplayName("Should update a Immuno exam")
            void shouldUpdateAImmunoExam() {
                donationPage = authPage.authenticateWithCredentials(email, password);

                donationPage.registerDonationWithAllExams("Weverton");
                donationPage.clickOnUpdateTabButton();
                donationPage.selectDonationInList("Weverton");
                donationPage.clickUpdateForImmunohematologyExam();

                new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/exams/immunohematology/"));

                assertThat(driver.getCurrentUrl()).contains("/exams/immunohematology/");
            }

            @Test
            @Tag("UiTest")
            @DisplayName("Should update a Sero exam")
            void shouldUpdateASeroExam() {
                donationPage = authPage.authenticateWithCredentials(email, password);

                donationPage.registerDonationWithAllExams("Weverton");
                donationPage.clickOnUpdateTabButton();
                donationPage.selectDonationInList("Weverton");
                donationPage.clickUpdateForSerologicalExam();

                new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/exams/serological-screening/"));

                assertThat(driver.getCurrentUrl()).contains("/exams/serological-screening/");
            }
        }

        @Test
        @Tag("UiTest")
        @DisplayName("Should go to update donation when click on update donation")
        void shouldGoToUpdateDonationWhenClickOnUpdateDonation() {
            donationPage = authPage.authenticateWithCredentials(email, password);
            donationPage.clickOnUpdateTabButton();
            assertThat(donationPage.isUpdateTabActive()).isTrue();
        }
    }

    @Nested
    class ViewDonation {

        @Nested
        class RegisterDonation {

            @AfterEach
            public void tearDown() {
                donationRepository.findAll().stream()
                        .filter(donation -> donation.getCreatedAt().toLocalDate().isEqual(LocalDate.now()))
                        .reduce((first, second) -> second)
                        .ifPresent(lastDonation -> {
                            Appointment appointment = lastDonation.getAppointment();
                            appointment.setStatus(AppointmentStatus.SCHEDULED);
                            appointmentRepository.save(appointment);
                            donationRepository.deleteById(lastDonation.getId());
                        });
            }

            @Test
            @Tag("UiTest")
            @DisplayName("Should show view of a donation")
            void shouldShowViewOfADonation() {
                String donorName = "Ana Beatriz";
                donationPage = authPage.authenticateWithCredentials(email, password);
                donationPage.registerDonationWithAllExams(donorName);
                donationPage.viewDonationRegistered(donorName);
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(donationPage.getDonorOfView(donorName)).isEqualTo(donorName);
                softly.assertThat(donationPage.getQuantityOfExamUnderAnalysisOfView()).isEqualTo(2);
                softly.assertThat(donationPage.getColumnTextOfView("BLOOD TYPE")).isEqualTo("N/A");
                softly.assertThat(donationPage.getColumnTextOfView("IRREGULAR ANTIBODIES")).isEqualTo("N/A");
                softly.assertThat(donationPage.getColumnTextOfView("HEPATITIS B")).isEqualTo("N/A");
                softly.assertThat(donationPage.getColumnTextOfView("HEPATITIS C")).isEqualTo("N/A");
                softly.assertThat(donationPage.getColumnTextOfView("CHAGAS DISEASE")).isEqualTo("N/A");
                softly.assertThat(donationPage.getColumnTextOfView("SYPHILIS")).isEqualTo("N/A");
                softly.assertThat(donationPage.getColumnTextOfView("AIDS")).isEqualTo("N/A");
                softly.assertThat(donationPage.getColumnTextOfView("HTLV I/II")).isEqualTo("N/A");
                softly.assertAll();
            }

            @Test
            @Tag("UiTest")
            @DisplayName("should show view of a donation updated")
            void shouldShowViewOfADonationUpdated() {
                String donorName = "Ana Beatriz";
                String bloodType = "A+";
                String immunoObs = "immuno obs";
                String seroObs = "sero obs";
                String negative = "NEGATIVE";
                donationPage = authPage.authenticateWithCredentials(email, password);
                donationPage.updateExams(donorName, "A POS", immunoObs, seroObs);
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
                new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(
                        By.id("view-tab")
                ));
                donationPage.viewDonationRegistered(donorName);
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(donationPage.getDonorOfView(donorName)).isEqualTo(donorName);
                softly.assertThat(donationPage.getColumnTextOfView("BLOOD TYPE")).isEqualTo(bloodType);
                softly.assertThat(donationPage.getColumnTextOfView("IRREGULAR ANTIBODIES")).isEqualTo(negative);
                softly.assertThat(donationPage.getColumnTextOfView("HEPATITIS B")).isEqualTo(negative);
                softly.assertThat(donationPage.getColumnTextOfView("HEPATITIS C")).isEqualTo(negative);
                softly.assertThat(donationPage.getColumnTextOfView("CHAGAS DISEASE")).isEqualTo(negative);
                softly.assertThat(donationPage.getColumnTextOfView("SYPHILIS")).isEqualTo(negative);
                softly.assertThat(donationPage.getColumnTextOfView("AIDS")).isEqualTo(negative);
                softly.assertThat(donationPage.getColumnTextOfView("HTLV I/II")).isEqualTo(negative);
                softly.assertAll();
            }
        }

        @Test
        @Tag("UiTest")
        @DisplayName("Should go to view donation when click on view donation")
        void shouldGoToViewDonationWhenClickOnViewDonation() {
            donationPage = authPage.authenticateWithCredentials(email, password);
            donationPage.clickOnViewTabButton();
            assertThat(donationPage.isViewTabActive()).isTrue();
        }
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
        @Tag("UiTest")
        @MethodSource("screenSizes")
        @DisplayName("Should be able to scroll to view button on navigation bar")
        void shouldBeAbleToScrollToViewButtonOnNavigationBar(Dimension screenSize){
            driver.manage().window().setSize(screenSize);
            donationPage = authPage.authenticateWithCredentials(email, password);
            donationPage.scrollAndClickOnViewTabButton();
            assertThat(donationPage.isViewTabActive())
                    .withFailMessage("Expected tab content to be visible after clicking tabList.")
                    .isTrue();
        }
    }
}
