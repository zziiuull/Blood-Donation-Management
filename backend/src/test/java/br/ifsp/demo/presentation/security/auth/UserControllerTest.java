package br.ifsp.demo.presentation.security.auth;

import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.presentation.BaseApiIntegrationTest;
import com.github.javafaker.Faker;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

class UserControllerTest extends BaseApiIntegrationTest {
    private static final Faker faker = Faker.instance();
    private final List<UUID> createdPhysicianIds = new ArrayList<>();

    @AfterEach
    void tearDown() {
        createdPhysicianIds.forEach(id -> repository.deleteById(id));
        createdPhysicianIds.clear();
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class AuthenticateTest {
        private Physician physician;
        private String plainTextPassword;

        @BeforeEach
        void setup() {
            plainTextPassword = "123password";
            physician = registerPhysician(plainTextPassword);
            createdPhysicianIds.add(physician.getId());
        }

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should login with valid credentials")
        void shouldLoginWithValidCredentials() {
            createdPhysicianIds.add(physician.getId());
            AuthRequest authRequest = new AuthRequest(physician.getEmail(), plainTextPassword);

            given().contentType("application/json").port(port).body(authRequest)
            .when().post("/api/v1/authenticate")
            .then()
                    .log().ifValidationFails(LogDetail.BODY)
                    .statusCode(200)
                    .body("token", notNullValue());
        }

        @ParameterizedTest
        @CsvSource({
                "invalid@email.com, registered",         // invalid email
                "registered@example.com, wrongPassword", // invalid password
                "invalid@email.com, wrongPassword"       // invalid both
        })
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should fail login with invalid credentials")
        void shouldFailLoginWithInvalidCredentials(String textEmail, String textPassword) {
            String email =
                    textEmail.equals(faker.internet().emailAddress()) ? physician.getEmail() : textEmail;
            String password =
                    textPassword.equals("registered") ? plainTextPassword : textPassword;

            AuthRequest authRequest = new AuthRequest(email, password);
            given().contentType("application/json").port(port).body(authRequest)
            .when().post("/api/v1/authenticate")
            .then()
                    .log()
                    .ifValidationFails(LogDetail.BODY)
                    .statusCode(401);
        }
    }
}