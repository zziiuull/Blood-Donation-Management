package br.ifsp.demo.ui.pageTest;

import br.ifsp.demo.presentation.security.auth.AuthenticationService;
import br.ifsp.demo.presentation.security.auth.RegisterUserRequest;
import br.ifsp.demo.presentation.security.user.JpaUserRepository;
import br.ifsp.demo.ui.pageObject.AuthenticationPageObject;
import br.ifsp.demo.ui.pageObject.DonationPageObject;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

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
        donationPage.clickOnUpdateTabButton();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteById(userId);
        super.tearDown();
    }

}
