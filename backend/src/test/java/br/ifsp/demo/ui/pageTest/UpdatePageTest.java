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
import br.ifsp.demo.ui.pageObject.UpdateImmunoExamPageObject;
import br.ifsp.demo.ui.pageObject.UpdateSeroExamPageObject;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdatePageTest extends BaseSeleniumTest{

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    JpaUserRepository userRepository;
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    DonationRepository donationRepository;


    private static final Faker faker = Faker.instance();
    private String email;
    private String password;
    private UUID userId;
    private AuthenticationPageObject authPage;
    private DonationPageObject donationPage;

    @Override
    protected void setInitialPage() {
        String page = "http://localhost:3000/login";
        driver.get(page);
    }

    @BeforeEach
    public void classSetUp(){
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

    @Nested
    class Immuno {

        @ParameterizedTest
        @Tag("UiTest")
        @DisplayName("Should approve immuno exam to each blood type")
        @CsvSource({
                "'A POS', 'A+'",
                "'A NEG', 'A-'",
                "'B POS', 'B+'",
                "'B NEG', 'B-'",
                "'AB POS', 'AB+'",
                "'AB NEG', 'AB-'",
                "'O POS', 'O+'",
                "'O NEG', 'O-'"
        })
        void shouldApproveImmunoExamToEachBloodType(String bloodType, String expectedBloodType) {
            String irregularAntibodies = "Negative";

            donationPage.registerDonationWithAllExams("Weverton");
            donationPage.clickOnUpdateTabButton();
            donationPage.selectDonationInList("Weverton");

            UpdateImmunoExamPageObject immunoPage = donationPage.clickUpdateForImmunohematologyExam();

            immunoPage.selectBloodType(bloodType);
            immunoPage.selectIrregularAntibodies(irregularAntibodies);
            immunoPage.fillObservations(faker.lorem().sentence());

            DonationPageObject donationPage = immunoPage.clickApproveButtonAndExpectSuccess();

            String selectedBloodType = donationPage.getUpdatedBloodTypeTextFromTable();
            String selectedIrregularAntibodies = donationPage.getUpdatedAntibodiesTextFromTable();

            assertThat(selectedIrregularAntibodies).isEqualTo("NEGATIVE");
            assertThat(selectedBloodType).isEqualTo(expectedBloodType);
        }

        @Test
        @Tag("UiTest")
        @DisplayName("Should reject immuno exam")
        void shouldRejectImmunoExam(){
            String bloodType = "A POS";
            String irregularAntibodies = "Positive";

            donationPage.registerDonationWithAllExams("Weverton");
            donationPage.clickOnUpdateTabButton();
            donationPage.selectDonationInList("Weverton");

            UpdateImmunoExamPageObject immunoPage = donationPage.clickUpdateForImmunohematologyExam();

            immunoPage.selectBloodType(bloodType);
            immunoPage.selectIrregularAntibodies(irregularAntibodies);
            immunoPage.fillObservations(faker.lorem().sentence());

            DonationPageObject donationPage = immunoPage.clickRejectButton();

            String selectedBloodType = donationPage.getUpdatedBloodTypeTextFromTable();
            String selectedIrregularAntibodies = donationPage.getUpdatedAntibodiesTextFromTable();

            assertThat(selectedIrregularAntibodies).isEqualTo("POSITIVE");
            assertThat(selectedBloodType).isEqualTo("A+");
        }
        
        @Test
        @Tag("UiTest")
        @DisplayName("Should display error toast when can not approve the exam")
        void shouldDisplayErrorToastWhenCanNotApproveTheExam(){
            String irregularAntibodies = "Positive";

            donationPage.registerDonationWithAllExams("Weverton");
            donationPage.clickOnUpdateTabButton();
            donationPage.selectDonationInList("Weverton");

            UpdateImmunoExamPageObject immunoPage = donationPage.clickUpdateForImmunohematologyExam();

            immunoPage.selectBloodType("A POS");
            immunoPage.selectIrregularAntibodies(irregularAntibodies);
            immunoPage.fillObservations(faker.lorem().sentence());

            immunoPage.clickApproveButtonAndExpectFailure();

            assertThat(immunoPage.getUpdateExamErrorToastText()).isNotNull();
        }
    }

    @Nested
    class Sero {

        @Test
        @Tag("UiTest")
        @DisplayName("Should approve sero exam")
        void shouldApproveSeroExam(){
            String negative = "Negative";

            donationPage.registerDonationWithAllExams("Weverton");
            donationPage.clickOnUpdateTabButton();
            donationPage.selectDonationInList("Weverton");

            UpdateSeroExamPageObject seroPage = donationPage.clickUpdateForSerologicalExam();

            seroPage.selectDiseaseStatus("hepatitisB", negative);
            seroPage.selectDiseaseStatus("hepatitisC", negative);
            seroPage.selectDiseaseStatus("chagasDisease", negative);
            seroPage.selectDiseaseStatus("syphilis", negative);
            seroPage.selectDiseaseStatus("aids", negative);
            seroPage.selectDiseaseStatus("htlv1_2", negative);
            seroPage.fillObservations(faker.lorem().sentence());

            DonationPageObject donationPage = seroPage.clickApproveButtonAndExpectSuccess();

            String selectedHepatitisB = donationPage.getUpdatedHepatitisBStatus();
            String selectedHepatitisC = donationPage.getUpdatedHepatitisCStatus();
            String selectedChagasDisease = donationPage.getUpdatedChagasDiseaseStatus();
            String selectedSyphilis = donationPage.getUpdatedSyphilisStatus();
            String selectedAids= donationPage.getUpdatedAidsStatus();
            String selectedHtlvStatus = donationPage.getUpdatedHtlvStatus();

            assertThat(selectedHepatitisB).isEqualTo("NEGATIVE");
            assertThat(selectedHepatitisC).isEqualTo("NEGATIVE");
            assertThat(selectedChagasDisease).isEqualTo("NEGATIVE");
            assertThat(selectedSyphilis).isEqualTo("NEGATIVE");
            assertThat(selectedAids).isEqualTo("NEGATIVE");
            assertThat(selectedHtlvStatus).isEqualTo("NEGATIVE");
        }

        @Test
        @Tag("UiTest")
        @DisplayName("Should reject exam")
        void shouldRejectExam(){
            String negative = "Negative";
            String positive = "Positive";

            donationPage.registerDonationWithAllExams("Weverton");
            donationPage.clickOnUpdateTabButton();
            donationPage.selectDonationInList("Weverton");

            UpdateSeroExamPageObject seroPage = donationPage.clickUpdateForSerologicalExam();

            seroPage.selectDiseaseStatus("hepatitisB", negative);
            seroPage.selectDiseaseStatus("hepatitisC", negative);
            seroPage.selectDiseaseStatus("chagasDisease", negative);
            seroPage.selectDiseaseStatus("syphilis", negative);
            seroPage.selectDiseaseStatus("aids", negative);
            seroPage.selectDiseaseStatus("htlv1_2", positive);
            seroPage.fillObservations(faker.lorem().sentence());

            DonationPageObject donationPage = seroPage.clickRejectButton();

            String selectedHepatitisB = donationPage.getUpdatedHepatitisBStatus();
            String selectedHepatitisC = donationPage.getUpdatedHepatitisCStatus();
            String selectedChagasDisease = donationPage.getUpdatedChagasDiseaseStatus();
            String selectedSyphilis = donationPage.getUpdatedSyphilisStatus();
            String selectedAids= donationPage.getUpdatedAidsStatus();
            String selectedHtlvStatus = donationPage.getUpdatedHtlvStatus();

            assertThat(selectedHepatitisB).isEqualTo("NEGATIVE");
            assertThat(selectedHepatitisC).isEqualTo("NEGATIVE");
            assertThat(selectedChagasDisease).isEqualTo("NEGATIVE");
            assertThat(selectedSyphilis).isEqualTo("NEGATIVE");
            assertThat(selectedAids).isEqualTo("NEGATIVE");
            assertThat(selectedHtlvStatus).isEqualTo("POSITIVE");
        }

        @ParameterizedTest
        @Tag("UiTest")
        @DisplayName("Should display error toast when can not approve exam for each disease")
        @MethodSource("provideDiseaseKeys")
        void shouldDisplayErrorToastWhenCanNotApproveExamForEachDisease(String positiveDisease){
            donationPage.registerDonationWithAllExams("Weverton");
            donationPage.clickOnUpdateTabButton();
            donationPage.selectDonationInList("Weverton");

            UpdateSeroExamPageObject seroPage = donationPage.clickUpdateForSerologicalExam();

            List<String> allDiseaseKeys = List.of("hepatitisB", "hepatitisC", "chagasDisease", "syphilis", "aids", "htlv1_2");

            for (String key : allDiseaseKeys) {
                if (key.equals(positiveDisease)) {
                    seroPage.selectDiseaseStatus(key, "Positive");
                } else {
                    seroPage.selectDiseaseStatus(key, "Negative");
                }
            }
            seroPage.fillObservations(faker.lorem().sentence());

            seroPage.clickApproveButtonAndExpectFailure();

            assertThat(seroPage.getUpdateExamErrorToastText()).isNotNull();
        }

        private static Stream<String> provideDiseaseKeys() {
            return Stream.of(
                    "hepatitisB",
                    "hepatitisC",
                    "chagasDisease",
                    "syphilis",
                    "aids",
                    "htlv1_2"
            );
        }
    }

}
