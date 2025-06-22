package br.ifsp.demo.ui.pageTest;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.presentation.security.auth.AuthenticationService;
import br.ifsp.demo.presentation.security.auth.RegisterUserRequest;
import br.ifsp.demo.presentation.security.user.JpaUserRepository;
import br.ifsp.demo.ui.pageObject.AuthenticationPageObject;
import br.ifsp.demo.ui.pageObject.DonationPageObject;
import br.ifsp.demo.ui.pageObject.UpdateImmunoExamPageObject;
import br.ifsp.demo.ui.pageObject.UpdateSeroExamPageObject;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdatePageTest extends BaseSeleniumTest{

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    JpaUserRepository userRepository;


    private static final Faker faker = Faker.instance();
    private String email;
    private String password;
    private UUID userId;
    private AuthenticationPageObject authPage;
    private DonationPageObject donationPage;
    private BloodType bloodType;

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
    }
    
    @Test
    @Tag("UiTest")
    @DisplayName("Should approve immuno exam")
    void shouldApproveImmunoExam(){
        String bloodType = "A POS";
        String irregularAntibodies = "Negative";

        donationPage.registerDonationWithAllExams("Weverton");
        donationPage.clickOnUpdateTabButton();
        donationPage.selectDonationInList("Weverton");

        UpdateImmunoExamPageObject immunoPage = donationPage.clickUpdateForImmunohematologyExam();

        immunoPage.selectBloodType(bloodType);
        immunoPage.selectIrregularAntibodies(irregularAntibodies);
        immunoPage.fillObservations(faker.lorem().sentence());

        DonationPageObject donationPage = immunoPage.clickApproveButton();

        String selectedBloodType = donationPage.getUpdatedBloodTypeTextFromTable();
        String selectedIrregularAntibodies = donationPage.getUpdatedAntibodiesTextFromTable();

        assertThat(selectedIrregularAntibodies).isEqualTo("NEGATIVE");
        assertThat(selectedBloodType).isEqualTo("A+");
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
    @DisplayName("Should approve sero exam")
    void shouldApproveSeroExam(){
        String positive = "Positive";
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

        DonationPageObject donationPage = seroPage.clickApproveButton();

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
}
