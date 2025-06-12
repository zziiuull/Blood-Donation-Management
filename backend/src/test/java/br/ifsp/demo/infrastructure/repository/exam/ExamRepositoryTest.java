package br.ifsp.demo.infrastructure.repository.exam;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.donation.*;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.donor.Sex;
import br.ifsp.demo.domain.model.exam.Exam;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import br.ifsp.demo.infrastructure.repository.collectionSite.CollectionSiteRepository;
import br.ifsp.demo.infrastructure.repository.donation.DonationRepository;
import br.ifsp.demo.infrastructure.repository.donor.DonorRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExamRepositoryTest {

    @Autowired
    private ExamRepository sut;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private CollectionSiteRepository collectionSiteRepository;

    private Donor eligibleDonor;
    private Donor ineligibleDonor;
    private Appointment appointment;
    private Appointment appointment2;
    private Donation donation;
    private CollectionSite site;
    private ImmunohematologyExam immunohematologyExam;
    private SerologicalScreeningExam serologicalScreeningExam;

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
        ineligibleDonor = new Donor(
                "Enzo",
                new Cpf("12345678955"),
                contactInfo,
                LocalDate.of(2008, 5, 20),
                50.0,
                Sex.MALE,
                BloodType.O_POS
        );
        donorRepository.save(eligibleDonor);
        donorRepository.save(ineligibleDonor);

        ContactInfo siteContactInfo = new ContactInfo(
                "doesangue.sorocaba@email.com",
                "1533761530",
                "Av. Anhanguera, n. 715, Sorocaba/SP"
        );
        site = new CollectionSite(
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
        appointment2 = new Appointment(
                LocalDateTime.now().plusDays(1),
                AppointmentStatus.SCHEDULED,
                site,
                "Second donation"
        );

        appointmentRepository.save(appointment);
        appointmentRepository.save(appointment2);

        donation = new Donation(
                eligibleDonor,
                appointment,
                DonationStatus.UNDER_ANALYSIS
        );
        donationRepository.save(donation);

        immunohematologyExam = new ImmunohematologyExam(donation);
        serologicalScreeningExam = new SerologicalScreeningExam(donation);

        sut.save(immunohematologyExam);
        sut.save(serologicalScreeningExam);
    }

    @AfterEach
    void tearDown() {
        sut.delete(immunohematologyExam);
        sut.delete(serologicalScreeningExam);
        donationRepository.delete(donation);
        appointmentRepository.delete(appointment);
        appointmentRepository.delete(appointment2);
        collectionSiteRepository.delete(site);
        donorRepository.delete(eligibleDonor);
        donorRepository.delete(ineligibleDonor);
    }

    @Test
    @DisplayName("Should find no exams when the specific donation does not have exams")
    void shouldFindNoExamsOfASpecificDonationId(){
        List<Exam> result = sut.findAllByDonationId(UUID.randomUUID());
        assertThat(result).isEmpty();
    }
}