package br.ifsp.demo.presentation.controller.exam;

import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.CollectionSite;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import br.ifsp.demo.infrastructure.repository.collectionSite.CollectionSiteRepository;
import br.ifsp.demo.infrastructure.repository.donation.DonationRepository;
import br.ifsp.demo.infrastructure.repository.donor.DonorRepository;
import br.ifsp.demo.presentation.BaseApiIntegrationTest;
import br.ifsp.demo.presentation.EntityBuilder;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

class ExamControllerTest extends BaseApiIntegrationTest {
    private final List<UUID> createdDonationIds = new ArrayList<>();
    private final List<UUID> createdDonorIds = new ArrayList<>();
    private final List<UUID> createdAppointmentIds = new ArrayList<>();
    private final List<UUID> createdCollectionSiteIds = new ArrayList<>();

    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private CollectionSiteRepository collectionSiteRepository;

    private Donor donor;
    private Appointment appointment;
    private CollectionSite collectionSite;
    private Physician user;
    private String token;

    @BeforeEach
    void setup() {
        donor = EntityBuilder.createRandomDonor();
        donorRepository.save(donor);
        createdDonorIds.add(donor.getId());
        collectionSite = EntityBuilder.createRandomCollectionSite();
        collectionSiteRepository.save(collectionSite);
        createdCollectionSiteIds.add(collectionSite.getId());
        appointment = EntityBuilder.createRandomAppointment(collectionSite);
        appointmentRepository.save(appointment);
        createdAppointmentIds.add(appointment.getId());
        this.user = registerPhysician("password123");
        this.token = authenticate(user.getEmail(), "password123");
    }

    @AfterEach
    void tearDown() {
        createdDonationIds.forEach(id -> donationRepository.deleteById(id));
        createdDonationIds.clear();
        createdDonorIds.forEach(id -> donorRepository.deleteById(id));
        createdDonorIds.clear();
        createdAppointmentIds.forEach(id -> appointmentRepository.deleteById(id));
        createdAppointmentIds.clear();
        createdCollectionSiteIds.forEach(id -> collectionSiteRepository.deleteById(id));
        createdCollectionSiteIds.clear();
    }

    @Nested
    class RequestImmunohematologyExamTests {

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should request immunohematology exam and return 200 with exam id")
        void shouldRequestImmunohematologyExamAndReturn200WithExamId() {
            Donation donation = donationRepository.save(EntityBuilder.createRandomDonation(donor, appointment));
            createdDonationIds.add(donation.getId());

            given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .port(port)
            .when()
                .post("/api/v1/exam/request/immunohematology/" + donation.getId())
            .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("donationId", equalTo(donation.getId().toString()))
                .body("examStatus", equalTo("UNDER_ANALYSIS"))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue())
                .body("observations", nullValue())
                .body("bloodType", nullValue())
                .body("irregularAntibodies", nullValue());
        }
    }

    @Nested
    class RequestSerologicalExamTests {
        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should request serological exam and return 200 with exam id")
        void shouldRequestSerologicalExamAndReturn200WithExamId(){
            Donation donation = donationRepository.save(EntityBuilder.createRandomDonation(donor, appointment));
            createdDonationIds.add(donation.getId());

            given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .port(port)
            .when()
                .post("/api/v1/exam/request/serologicalscreening/" + donation.getId())
            .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("bloodType", nullValue())
                .body("donationId", equalTo(donation.getId().toString()))
                .body("examStatus", equalTo("UNDER_ANALYSIS"))
                .body("irregularAntibodies", nullValue())
                .body("observations", nullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
        }
    }
}