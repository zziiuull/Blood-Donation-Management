package br.ifsp.demo.presentation.controller.donation;

import br.ifsp.demo.application.service.donation.DonationRegisterService;
import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.donation.*;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.donor.Sex;
import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import br.ifsp.demo.infrastructure.repository.collectionSite.CollectionSiteRepository;
import br.ifsp.demo.infrastructure.repository.donation.DonationRepository;
import br.ifsp.demo.infrastructure.repository.donor.DonorRepository;
import br.ifsp.demo.presentation.BaseApiIntegrationTest;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class DonationControllerTest extends BaseApiIntegrationTest {

    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private CollectionSiteRepository collectionSiteRepository;
    @Autowired
    private DonationRegisterService donationRegisterService;
    @Autowired
    private DonationRepository donationRepository;

    private Faker faker = Faker.instance();
    private Physician user;
    private String token;

    private Donor elegibleDonor;
    private Donor ineligibleDonor;
    private Donor anotherElegibleDonor;
    private CollectionSite site;
    private Appointment appointment;
    private Appointment secondAppointment;


    @BeforeEach
    void setUp() {
        this.user = registerPhysician("Teste123");
        this.token = authenticate(user.getEmail(), "Teste123");

        this.elegibleDonor = new Donor(
                faker.name().toString(),
                new Cpf("11122233344"),
                new ContactInfo(faker.internet().emailAddress(), faker.phoneNumber().toString(), faker.address().toString()),
                LocalDate.now().minusYears(30),
                80.0,
                Sex.MALE,
                BloodType.A_POS
        );

        this.ineligibleDonor = new Donor(
                faker.name().toString(),
                new Cpf("11222233344"),
                new ContactInfo(faker.internet().emailAddress(), faker.phoneNumber().toString(), faker.address().toString()),
                LocalDate.now().minusYears(17),
                40.0,
                Sex.MALE,
                BloodType.O_NEG
        );

        this.anotherElegibleDonor = new Donor(
                faker.name().toString(),
                new Cpf("11122333344"),
                new ContactInfo(faker.internet().emailAddress(), faker.phoneNumber().toString(), faker.address().toString()),
                LocalDate.now().minusYears(40),
                75.0,
                Sex.MALE,
                BloodType.AB_POS
        );

        site = new CollectionSite(faker.name().toString(),
                new ContactInfo(faker.internet().emailAddress(), faker.phoneNumber().toString(), faker.address().toString()));

        appointment = new Appointment(LocalDateTime.now().plusDays(5), AppointmentStatus.SCHEDULED, site, "Notes test");
        secondAppointment = new Appointment(LocalDateTime.now().plusDays(7), AppointmentStatus.SCHEDULED, site, "Notes test");

        donorRepository.save(ineligibleDonor);
        donorRepository.save(elegibleDonor);
        donorRepository.save(anotherElegibleDonor);
        collectionSiteRepository.save(site);
        appointmentRepository.save(appointment);
        appointmentRepository.save(secondAppointment);
    }

    @AfterEach
    void tearDown() {
        donorRepository.delete(ineligibleDonor);
        donorRepository.delete(elegibleDonor);
        donorRepository.delete(anotherElegibleDonor);
        collectionSiteRepository.delete(site);
        appointmentRepository.delete(appointment);
    }

    @Nested
    @DisplayName("Register method")
    class register {

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 200 when registering a donation successfully")
        void shouldReturn200WhenRegisteringADonationSuccessfully() {
            RegisterRequest request = new RegisterRequest(elegibleDonor.getId(), appointment.getId());

            given()
                    .header("Authorization", "Bearer " + token)
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .post("/api/v1/donation")
                    .then()
                    .statusCode(200)
                    .body("id", notNullValue());
        }

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 400 when donor is not elegible to donate")
        void shouldReturn400WhenDonorIsNotElegibleToDonate() {
            RegisterRequest request = new RegisterRequest(ineligibleDonor.getId(), appointment.getId());

            given()
                    .header("Authorization", "Bearer " + token)
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .post("/api/v1/donation")
                    .then()
                    .statusCode(400);
        }

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 400 when donation already exists for this appointment")
        void shouldReturn400WhenDonationAlreadyExistsForThisAppointment(){
            donationRegisterService.registerByDonorId(elegibleDonor.getId(), appointment.getId());

            RegisterRequest secondRequest = new RegisterRequest(anotherElegibleDonor.getId(), appointment.getId());

            given()
                    .header("Authorization", "Bearer " + token)
                    .contentType("application/json")
                    .body(secondRequest)
                    .when()
                    .post("/api/v1/donation")
                    .then()
                    .statusCode(400);
        }

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 401 when authentication fails")
        void shouldReturn401WhenAuthenticationFails(){
            given()
                    .when().post("/api/v1/donation")
                    .then().statusCode(401);
        }

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 404 when donor does not exist")
        void shouldReturn404WhenDonorDoesNotExist(){
            collectionSiteRepository.save(site);
            appointmentRepository.save(appointment);

            RegisterRequest request = new RegisterRequest(UUID.randomUUID(), appointment.getId());

            given()
                    .header("Authorization", "Bearer " + token)
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .post("/api/v1/donation")
                    .then()
                    .statusCode(404);

            appointmentRepository.deleteById(appointment.getId());
        }

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 404 when appointment does not exist")
        void shouldReturn404WhenAppointmentDoesNotExist(){
            donorRepository.save(elegibleDonor);

            RegisterRequest request = new RegisterRequest(elegibleDonor.getId(), UUID.randomUUID());

            given()
                    .header("Authorization", "Bearer " + token)
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .post("/api/v1/donation")
                    .then()
                    .statusCode(404);

            donorRepository.deleteById(elegibleDonor.getId());
        }
    }

    @Nested
    @DisplayName("View method")
    class view {

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 200 when view successfully")
        void shouldReturn200WhenViewSuccessfully(){
            Donation donation = new Donation(elegibleDonor, appointment, DonationStatus.UNDER_ANALYSIS);
            donationRepository.save(donation);
            UUID donationId = donation.getId();

            given()
                    .header("Authorization", "Bearer " + token)
                    .pathParam("id", donationId)
                    .when()
                    .get("/api/v1/donation/{id}")
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(donationId.toString()));
        }

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 401 when authentication fails")
        void shouldReturn401WhenAuthenticationFails(){
            Donation donation = new Donation(elegibleDonor, appointment, DonationStatus.UNDER_ANALYSIS);
            donationRepository.save(donation);
            UUID donationId = donation.getId();

            given()
                    .pathParam("id", donationId)
                    .when()
                    .get("/api/v1/donation/{id}")
                    .then()
                    .statusCode(401);
        }

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 404 when donation does not exist")
        void shouldReturn404WhenDonationDoesNotExist(){
            given()
                    .header("Authorization", "Bearer " + token)
                    .pathParam("id", UUID.randomUUID())
                    .when()
                    .get("/api/v1/donation/{id}")
                    .then()
                    .statusCode(404);
        }
    }

    @Nested
    @DisplayName("View all method")
    class viewAll {

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 200 when view all successfully")
        void shouldReturn200WhenViewAllSuccessfully(){
            Donation donation = new Donation(elegibleDonor, appointment, DonationStatus.UNDER_ANALYSIS);
            Donation anotherDonation = new Donation(anotherElegibleDonor, secondAppointment, DonationStatus.UNDER_ANALYSIS);
            donationRepository.save(donation);
            donationRepository.save(anotherDonation);

            given()
                    .header("Authorization", "Bearer " + token)
                    .contentType("application/json")
            .when()
                    .get("/api/v1/donation/")
            .then()
                    .statusCode(200)
                    .body("size()", is(2))
                    .body("id", hasItems(donation.getId().toString(), anotherDonation.getId().toString()));

            donationRepository.deleteById(donation.getId());
            donationRepository.deleteById(anotherDonation.getId());
        }

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 401 when authentication fails")
        void shouldReturn401WhenAuthenticationFails(){
            given()
                    .when().get("/api/v1/donation")
                    .then().statusCode(401);
        }
    }
}
