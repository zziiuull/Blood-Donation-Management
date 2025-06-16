package br.ifsp.demo.presentation.controller.donor;

import br.ifsp.demo.application.service.dto.donor.DonorDTO;
import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.donor.Sex;
import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.infrastructure.repository.donor.DonorRepository;
import br.ifsp.demo.presentation.BaseApiIntegrationTest;
import br.ifsp.demo.presentation.security.user.JpaUserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;


class DonorControllerTest extends BaseApiIntegrationTest {

    private Faker faker = Faker.instance();
    private Physician user;
    private String token;

    @Autowired
    private DonorRepository donorRepository;

    @BeforeEach
    void setUp() {
        this.user = registerPhysician("Teste123");
        this.token = authenticate(user.getEmail(), "Teste123");
    }
    
    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Should return 200 and a list of donors when they exist")
    void shouldReturn200AndAListOfDonorsWhenTheyExist(){
        Donor donor = new Donor(faker.name().toString(),
                new Cpf("12345678910"),
                new ContactInfo(faker.internet().emailAddress().toString(), faker.phoneNumber().toString(), faker.address().toString()),
                LocalDate.now().minusDays(35),
                70.0,
                Sex.FEMALE,
                BloodType.O_POS
                );

        donorRepository.save(donor);

        List<DonorDTO> donors = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/donor")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList("", DonorDTO.class);

        assertThat(donors).isNotNull();

        Optional<DonorDTO> foundDonorOpt = donors.stream()
                .filter(dto -> dto.id().equals(donor.getId()))
                .findFirst();

        assertThat(foundDonorOpt).isPresent();

        DonorDTO foundDonor = foundDonorOpt.get();
        assertThat(foundDonor.id()).isEqualTo(donor.getId());


        donorRepository.delete(donor);
        repository.delete(user);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Should return 404 when donors do not exist")
    void shouldReturn404WhenDonorsDoNotExist(){
        List<Donor> donors = donorRepository.findAll();
        donorRepository.deleteAll();

        List<DonorDTO> responseList = given().header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/donor")
                .then()
                .statusCode(404)
                .body("description", is("Donors do not exist."))
                .extract().jsonPath().getList("", DonorDTO.class);


        assertThat(responseList.size()).isEqualTo(0);

        donors.forEach(donor -> {donorRepository.save(donor);});
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Should return 401 when authentication fails")
    void shouldReturn401WhenAuthenticationFails(){
        given()
                .when().get("/api/v1/donor")
                .then().statusCode(401);
    }
}