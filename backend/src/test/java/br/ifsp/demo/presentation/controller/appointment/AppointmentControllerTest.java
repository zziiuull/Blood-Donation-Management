package br.ifsp.demo.presentation.controller.appointment;

import br.ifsp.demo.application.service.dto.appointment.AppointmentDTO;
import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.AppointmentStatus;
import br.ifsp.demo.domain.model.donation.CollectionSite;
import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import br.ifsp.demo.infrastructure.repository.collectionSite.CollectionSiteRepository;
import br.ifsp.demo.presentation.BaseApiIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;


class AppointmentControllerTest extends BaseApiIntegrationTest {

    @Autowired
    private CollectionSiteRepository collectionSiteRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Physician user;
    private String token;

    @BeforeEach
    void setUp() {
        this.user = registerPhysician("Teste123");
        this.token = authenticate(user.getEmail(), "Teste123");
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Should return 200 and a list of appointments when they exist")
    void shouldReturn200AndAListOfAppointmentsWhenTheyExist(){
        CollectionSite site = collectionSiteRepository.save(new CollectionSite("Hemocentro",
                new ContactInfo("email@email.com", "123", "address")));

        Appointment appointment = appointmentRepository.save(new Appointment(
                LocalDateTime.now().plusDays(2),
                AppointmentStatus.SCHEDULED,
                site,
                "Test notes"
        ));


        List<AppointmentDTO> responseList = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/appointment")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList("", AppointmentDTO.class);

        assertThat(responseList).isNotNull();
        assertThat(responseList.size()).isEqualTo(3);

        Optional<AppointmentDTO> foundAppointmentOpt = responseList.stream()
                .filter(dto -> dto.id().equals(appointment.getId()))
                .findFirst();

        assertThat(foundAppointmentOpt).isPresent();

        AppointmentDTO returnedAppointment = foundAppointmentOpt.get();
        assertThat(returnedAppointment.id()).isEqualTo(appointment.getId());

        collectionSiteRepository.delete(site);
        appointmentRepository.delete(appointment);
        repository.delete(user);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Should return 404 when appointments do not exist")
    void shouldReturn404WhenAppointmentsDoNotExist(){
        List<Appointment> appointments = appointmentRepository.findAll();
        appointmentRepository.deleteAll();

        List<AppointmentDTO> responseList = given().header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/appointment")
                .then()
                .statusCode(404)
                .body("description", is("Appointments do not exist."))
                .extract().jsonPath().getList("", AppointmentDTO.class);


        assertThat(responseList.size()).isEqualTo(0);

        appointments.forEach(appointment -> {appointmentRepository.save(appointment);});
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Should return 401 when authentication fails")
    void shouldReturn401WhenAuthenticationFails(){
        given()
        .when().get("/api/v1/appointment")
        .then().statusCode(401);
    }
}
