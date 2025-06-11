package br.ifsp.demo.infrastructure.repository.donation;

import br.ifsp.demo.application.service.donation.DonationRegisterService;
import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.donation.*;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.donor.Sex;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import br.ifsp.demo.infrastructure.repository.collectionSite.CollectionSiteRepository;
import br.ifsp.demo.infrastructure.repository.donor.DonorRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DonationRepositoryTest {
    @Autowired
    private DonationRepository sut;
    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private CollectionSiteRepository collectionSiteRepository;

    private Donor eligibleDonor;
    private Appointment appointment;
    private Donation donation;

    @BeforeEach
    void setup() {
        ContactInfo contactInfo = new ContactInfo(
                "weverton@email.com",
                "11991239867",
                "Rua da Ponte Caída, n. 101, Itaquaquecetuba/SP"
        );
        eligibleDonor = new Donor(
                "Weverton",
                new Cpf("12345678955"),
                contactInfo,
                LocalDate.of(1990, 5, 20),
                85.0,
                Sex.MALE,
                BloodType.O_POS
        );
        donorRepository.save(eligibleDonor);

        ContactInfo siteContactInfo = new ContactInfo(
                "doesangue.sorocaba@email.com",
                "1533761530",
                "Av. Anhanguera, n. 715, Sorocaba/SP"
        );
        CollectionSite site = new CollectionSite(
                "Banco de Doação de Sorocaba",
                siteContactInfo
        );
        collectionSiteRepository.save(site);

        appointment = new Appointment(
                LocalDateTime.now().plusDays(1),
                AppointmentStatus.SCHEDULED,
                site,
                "First donation"
        );
        appointmentRepository.save(appointment);

        donation = new Donation(
                eligibleDonor,
                appointment,
                DonationStatus.UNDER_ANALYSIS
        );
        sut.save(donation);
    }

    @AfterEach
    void tearDown() {
        donorRepository.delete(eligibleDonor);
        appointmentRepository.delete(appointment);
        sut.delete(donation);
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Should return true when exist by donor and appointment")
    void shouldReturnTrueWhenExistByDonorAndAppointment(){
        boolean result = sut.existsByDonorAndAppointment(eligibleDonor, appointment);
        assertThat(result).isTrue();
    }
}