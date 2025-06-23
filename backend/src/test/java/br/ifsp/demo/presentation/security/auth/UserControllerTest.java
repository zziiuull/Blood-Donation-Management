package br.ifsp.demo.presentation.security.auth;

import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.presentation.BaseApiIntegrationTest;
import br.ifsp.demo.presentation.EntityBuilder;
import com.github.javafaker.Faker;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

class UserControllerTest extends BaseApiIntegrationTest {
    private static final Faker faker = Faker.instance();
    private final List<UUID> createdPhysicianIds = new ArrayList<>();

    @AfterEach
    void tearDown() {
        createdPhysicianIds.forEach(id -> userRepository.deleteById(id));
        createdPhysicianIds.clear();
    }

    @Nested
    class RegisterTests {
        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should register physician and return 201 with id as payload")
        void shouldRegisterPhysicianAndReturn201WithIdAsPayload() {
            final String plainTextPassword = "123password";
            final RegisterPhysicianRequest registerPhysicianRequest =
                    EntityBuilder.createRandomRegisterPhysicianRequest(plainTextPassword);

            String id = given().contentType("application/json").port(port).body(registerPhysicianRequest)
            .when().post("/api/v1/register/physician")
            .then()
                .log()
                .ifValidationFails(LogDetail.BODY)
                .statusCode(201)
                .body("id", notNullValue())
                .extract().path("id");

            createdPhysicianIds.add(UUID.fromString(id));
        }

        @ParameterizedTest
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @MethodSource("provideInvalidPhysiciansFieldsScenarios")
        @DisplayName("Should fail on register physician and return 400")
        void shouldFailOnRegisterPhysicianAndReturn400(
                String name,
                String lastName,
                String email,
                String password,
                String cpf,
                String phone,
                String address,
                String crmNumber,
                String crmState
        ) {
            RegisterPhysicianRequest request = new RegisterPhysicianRequest(
                    name, lastName, email, password, cpf, phone, address, crmNumber, crmState
            );

            given().contentType("application/json").port(port).body(request)
            .when().post("/api/v1/register/physician")
            .then()
                .log()
                .ifValidationFails(LogDetail.BODY)
                .statusCode(400);
        }

        public static Stream<Arguments> provideInvalidPhysiciansFieldsScenarios() {
            return Stream.of(
                    // invalid name
                    Arguments.of(
                            "",
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),
                    Arguments.of(
                            null,
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),

                    // invalid lastname
                    Arguments.of(
                            faker.name().firstName(),
                            "",
                            faker.internet().emailAddress(),
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),
                    Arguments.of(
                            faker.name().firstName(),
                            null,
                            faker.internet().emailAddress(),
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),

                    // invalid email
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            "invalid-email",
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            null,
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            "",
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),

                    // invalid password
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            null,
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),

                    // invalid cpf
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            "123",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            null,
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            "",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),

                    // invalid phone
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            "12345678901",
                            "",
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            "12345678901",
                            null,
                            "Rua 1",
                            "1234567",
                            "SP"
                    ),

                    // invalid address
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "",
                            "1234567",
                            "SP"
                    ),
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            null,
                            "1234567",
                            "SP"
                    ),

                    // invalid crmNumber
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "",
                            "SP"
                    ),
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            null,
                            "SP"
                    ),

                    // invalid crmState
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            ""
                    ),
                    Arguments.of(
                            faker.name().firstName(),
                            faker.name().lastName(),
                            faker.internet().emailAddress(),
                            "123password",
                            "12345678901",
                            faker.phoneNumber().cellPhone(),
                            "Rua 1",
                            "1234567",
                            null
                    )
            );
        }

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should fail on register physician when email is already registered and return 409")
        void shouldFailOnRegisterPhysicianWhenEmailAlreadyRegisteredAndReturn409() {
            final String plainTextPassword = "123password";
            final RegisterPhysicianRequest request = EntityBuilder.createRandomRegisterPhysicianRequest(plainTextPassword);

            // First try
            String id = given()
                .contentType("application/json")
                .port(port)
                .body(request)
            .when()
                .post("/api/v1/register/physician")
            .then()
                .statusCode(201)
                .extract()
                .path("id");

            createdPhysicianIds.add(UUID.fromString(id));

            // Second try, need return 409
            given()
                .contentType("application/json")
                .port(port)
                .body(request)
            .when()
                .post("/api/v1/register/physician")
                .then()
                .statusCode(409);
        }
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