package br.ifsp.demo.presentation.controller.donation;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.AppointmentStatus;
import br.ifsp.demo.domain.model.donation.CollectionSite;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.donor.Sex;
import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import br.ifsp.demo.infrastructure.repository.collectionSite.CollectionSiteRepository;
import br.ifsp.demo.infrastructure.repository.donor.DonorRepository;
import br.ifsp.demo.presentation.BaseApiIntegrationTest;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

class DonationControllerTest extends BaseApiIntegrationTest {

    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private CollectionSiteRepository collectionSiteRepository;

    private Faker faker = Faker.instance();
    private Physician user;
    private String token;

    private Donor elegibleDonor;
    private CollectionSite site;
    private Appointment appointment;


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

        this.site = new CollectionSite(faker.name().toString(),
                new ContactInfo(faker.internet().emailAddress(), faker.phoneNumber().toString(), faker.address().toString()));

        this.appointment = new Appointment(LocalDateTime.now().plusDays(5), AppointmentStatus.SCHEDULED, site, "Notes test");
    }


    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Should return 200 when registering a donation successfully")
    void shouldReturn200WhenRegisteringADonationSuccessfully(){
        donorRepository.save(elegibleDonor);
        collectionSiteRepository.save(site);
        appointmentRepository.save(appointment);

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


        donorRepository.deleteById(elegibleDonor.getId());
        appointmentRepository.deleteById(appointment.getId());
    }

}
