package br.ifsp.demo.presentation.controller.exam;

import br.ifsp.demo.application.service.exam.ExamRequestService;
import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.CollectionSite;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.exam.DiseaseDetection;
import br.ifsp.demo.domain.model.exam.Exam;
import br.ifsp.demo.domain.model.exam.IrregularAntibodies;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import br.ifsp.demo.infrastructure.repository.collectionSite.CollectionSiteRepository;
import br.ifsp.demo.infrastructure.repository.donation.DonationRepository;
import br.ifsp.demo.infrastructure.repository.donor.DonorRepository;
import br.ifsp.demo.infrastructure.repository.exam.ExamRepository;
import br.ifsp.demo.presentation.BaseApiIntegrationTest;
import br.ifsp.demo.presentation.EntityBuilder;
import br.ifsp.demo.presentation.controller.exam.request.ImmunohematologyExamRequest;
import br.ifsp.demo.presentation.controller.exam.request.SerologicalScreeningExamRequest;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

class ExamControllerTest extends BaseApiIntegrationTest {
    private final List<UUID> createdDonationIds = new ArrayList<>();
    private final List<UUID> createdDonorIds = new ArrayList<>();
    private final List<UUID> createdAppointmentIds = new ArrayList<>();
    private final List<UUID> createdCollectionSiteIds = new ArrayList<>();
    private final List<UUID> createdExamIds = new ArrayList<>();

    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private CollectionSiteRepository collectionSiteRepository;
    @Autowired
    private ExamRepository examRepository;

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
        createdExamIds.forEach(id -> examRepository.deleteById(id));
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
                .body(donation)
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

    @Nested
    class ApproveImmunohematologyExamTests {
        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("should approve immunohematology exam and return 200")
        void shouldApproveImmunohematologyExamAndReturn200(){
            ExamRequestService examRequestService = new ExamRequestService(examRepository);
            Donation donation = donationRepository.save(EntityBuilder.createRandomDonation(donor, appointment));
            createdDonationIds.add(donation.getId());

            Exam exam = examRepository.save(examRequestService.requestImmunohematologyExam(donation));
            createdExamIds.add(exam.getId());

            ImmunohematologyExamRequest examRequest = new ImmunohematologyExamRequest(BloodType.O_NEG, IrregularAntibodies.NEGATIVE);

            given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .port(port)
                .body(examRequest)
            .when()
                .post("/api/v1/exam/register/donation/" + donation.getId() + "/immunohematology/approve/" + exam.getId())
            .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("bloodType", equalTo("O_NEG"))
                .body("donationId", equalTo(donation.getId().toString()))
                .body("examStatus", equalTo("APPROVED"))
                .body("irregularAntibodies", equalTo("NEGATIVE"))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue())
                .body("observations", notNullValue());
        }

        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 400 if donation should not be approved")
        void shouldReturn400IfDonationShouldNotBeApproved(){
            ExamRequestService examRequestService = new ExamRequestService(examRepository);
            Donation donation = donationRepository.save(EntityBuilder.createRandomDonation(donor, appointment));
            createdDonationIds.add(donation.getId());

            Exam exam = examRepository.save(examRequestService.requestImmunohematologyExam(donation));
            createdExamIds.add(exam.getId());

            ImmunohematologyExamRequest examRequest = new ImmunohematologyExamRequest(BloodType.O_NEG, IrregularAntibodies.POSITIVE);

            given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .port(port)
                .body(examRequest)
            .when()
                .post("/api/v1/exam/register/donation/" + donation.getId() + "/immunohematology/approve/" + exam.getId())
            .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class ApproveSerologicalExamTests {
        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("should approve serological exam and return 200")
        void shouldApproveSerologicalExamAndReturn200(){
            ExamRequestService examRequestService = new ExamRequestService(examRepository);
            Donation donation = donationRepository.save(EntityBuilder.createRandomDonation(donor, appointment));
            createdDonationIds.add(donation.getId());

            Exam exam = examRepository.save(examRequestService.requestSerologicalScreeningExam(donation));
            createdExamIds.add(exam.getId());

            SerologicalScreeningExamRequest examRequest = new SerologicalScreeningExamRequest(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE);

            given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .port(port)
                .body(examRequest)
            .when()
                .post("/api/v1/exam/register/donation/" + donation.getId() + "/serologicalscreening/approve/" + exam.getId())
            .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("donationId", equalTo(donation.getId().toString()))
                .body("examStatus", equalTo("APPROVED"))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue())
                .body("hepatitisB", equalTo("NEGATIVE"))
                .body("hepatitisC", equalTo("NEGATIVE"))
                .body("chagasDisease", equalTo("NEGATIVE"))
                .body("syphilis", equalTo("NEGATIVE"))
                .body("aids", equalTo("NEGATIVE"))
                .body("htlv1_2", equalTo("NEGATIVE"))
                .body("observations", notNullValue());
        }

        @ParameterizedTest
        @MethodSource("provideDataToInvalidExamRequests")
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("Should return 400 if donation should not be approved")
        void shouldReturn400IfDonationShouldNotBeApproved(SerologicalScreeningExamRequest examRequest) {
            ExamRequestService examRequestService = new ExamRequestService(examRepository);
            Donation donation = donationRepository.save(EntityBuilder.createRandomDonation(donor, appointment));
            createdDonationIds.add(donation.getId());

            Exam exam = examRepository.save(examRequestService.requestSerologicalScreeningExam(donation));
            createdExamIds.add(exam.getId());

            given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .port(port)
                .body(examRequest)
            .when()
                .post("/api/v1/exam/register/donation/" + donation.getId() + "/serologicalscreening/approve/" + exam.getId())
            .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        public static Stream<Arguments> provideDataToInvalidExamRequests() {
            return Stream.of(
                    Arguments.of(new SerologicalScreeningExamRequest(DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamRequest(DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamRequest(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamRequest(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamRequest(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamRequest(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE))
            );
        }
    }

    @Nested
    class RejectImmunohematologyExamTests {
        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("should reject ImmunohematologyExam and return 200")
        void shouldRejectImmunohematologyExamAndReturn200(){
            ExamRequestService examRequestService = new ExamRequestService(examRepository);
            Donation donation = donationRepository.save(EntityBuilder.createRandomDonation(donor, appointment));
            createdDonationIds.add(donation.getId());

            Exam exam = examRepository.save(examRequestService.requestImmunohematologyExam(donation));
            createdExamIds.add(exam.getId());

            ImmunohematologyExamRequest examRequest = new ImmunohematologyExamRequest(BloodType.O_NEG, IrregularAntibodies.POSITIVE);

            given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .port(port)
                .body(examRequest)
            .when()
                .post("/api/v1/exam/register/donation/" + donation.getId() + "/immunohematology/reject/" + exam.getId())
            .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("bloodType", equalTo("O_NEG"))
                .body("donationId", equalTo(donation.getId().toString()))
                .body("examStatus", equalTo("REJECTED"))
                .body("irregularAntibodies", equalTo("POSITIVE"))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue())
                .body("observations", notNullValue());
        }
    }

    @Nested
    class RejectSerologicalScreeningExam {
        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("should reject serological exam and return 200")
        void shouldRejectSerologicalExamAndReturn200(){
            ExamRequestService examRequestService = new ExamRequestService(examRepository);
            Donation donation = donationRepository.save(EntityBuilder.createRandomDonation(donor, appointment));
            createdDonationIds.add(donation.getId());

            Exam exam = examRepository.save(examRequestService.requestSerologicalScreeningExam(donation));
            createdExamIds.add(exam.getId());

            SerologicalScreeningExamRequest examRequest = new SerologicalScreeningExamRequest(DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE);

            given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .port(port)
                .body(examRequest)
            .when()
                .post("/api/v1/exam/register/donation/" + donation.getId() + "/serologicalscreening/reject/" + exam.getId())
            .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("donationId", equalTo(donation.getId().toString()))
                .body("examStatus", equalTo("REJECTED"))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue())
                .body("hepatitisB", equalTo("POSITIVE"))
                .body("hepatitisC", equalTo("NEGATIVE"))
                .body("chagasDisease", equalTo("NEGATIVE"))
                .body("syphilis", equalTo("NEGATIVE"))
                .body("aids", equalTo("NEGATIVE"))
                .body("htlv1_2", equalTo("NEGATIVE"))
                .body("observations", notNullValue());
        }
    }

    @Nested
    class ViewImmunohematologyExam {
        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("should return exam and status 200")
        void shouldReturnExamAndStatus200(){
            ExamRequestService examRequestService = new ExamRequestService(examRepository);
            Donation donation = donationRepository.save(EntityBuilder.createRandomDonation(donor, appointment));
            createdDonationIds.add(donation.getId());

            Exam exam = examRepository.save(examRequestService.requestImmunohematologyExam(donation));
            createdExamIds.add(exam.getId());

            given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .port(port)
            .when()
                .get("/api/v1/exam/view/immunohematology/" + donation.getId())
            .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("donationId", equalTo(donation.getId().toString()))
                .body("examStatus", equalTo("UNDER_ANALYSIS"))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue())
                .body("irregularAntibodies", nullValue())
                .body("bloodType", nullValue())
                .body("observations", nullValue());
        }
    }

    @Nested
    class ViewSerologicalScreeningExam {
        @Test
        @Tag("ApiTest")
        @Tag("IntegrationTest")
        @DisplayName("should return exam and status 200")
        void shouldReturnExamAndStatus200(){
            ExamRequestService examRequestService = new ExamRequestService(examRepository);
            Donation donation = donationRepository.save(EntityBuilder.createRandomDonation(donor, appointment));
            createdDonationIds.add(donation.getId());

            Exam exam = examRepository.save(examRequestService.requestSerologicalScreeningExam(donation));
            createdExamIds.add(exam.getId());

            given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .port(port)
            .when()
                .get("/api/v1/exam/view/serologicalscreening/" + donation.getId())
            .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("donationId", equalTo(donation.getId().toString()))
                .body("examStatus", equalTo("UNDER_ANALYSIS"))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue())
                .body("hepatitisB", nullValue())
                .body("hepatitisC", nullValue())
                .body("chagasDisease", nullValue())
                .body("syphilis", nullValue())
                .body("aids", nullValue())
                .body("htlv1_2", nullValue())
                .body("observations", nullValue());
        }
    }
}