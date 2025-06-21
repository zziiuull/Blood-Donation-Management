package br.ifsp.demo.ui.pageTest;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.presentation.security.auth.AuthenticationService;
import br.ifsp.demo.presentation.security.auth.RegisterUserRequest;
import br.ifsp.demo.presentation.security.user.JpaUserRepository;
import br.ifsp.demo.ui.pageObject.AuthenticationPageObject;
import br.ifsp.demo.ui.pageObject.DonationPageObject;
import br.ifsp.demo.ui.pageObject.UpdateImmunoExamPageObject;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
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
}
