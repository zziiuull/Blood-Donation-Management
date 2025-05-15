package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.donation.*;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.donor.Sex;
import br.ifsp.demo.domain.repository.appointment.AppointmentRepository;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import br.ifsp.demo.domain.repository.donor.DonorRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationRegisterServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @Mock
    private DonorRepository donorRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private DonationRegisterService sut;

    private UUID eligibleDonorId;
    private UUID ineligibleDonorId;
    private UUID appointmentId;
    private Donor eligibleDonor;
    private Donor ineligibleDonor;
    private UUID nonExistentAppointmentId;
    private UUID nonExistentDonorId;
    private Appointment appointment;

    @BeforeEach
    void setUp(){
        eligibleDonorId = UUID.randomUUID();
        ineligibleDonorId = UUID.randomUUID();
        appointmentId = UUID.randomUUID();
        nonExistentAppointmentId = UUID.randomUUID();
        nonExistentDonorId = UUID.randomUUID();

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

        ContactInfo siteContactInfo = new ContactInfo(
                "doesangue.sorocaba@email.com",
                "1533761530",
                "Av. Anhanguera, n. 715, Sorocaba/SP"
        );
        CollectionSite site = new CollectionSite(
                "Banco de Doação de Sorocaba",
                siteContactInfo
        );
        appointment = new Appointment(
                LocalDateTime.now().plusDays(1),
                AppointmentStatus.SCHEDULED,
                site,
                "First donation"
        );
    }

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should register donation when donor is eligible")
        void shouldRegisterDonationWhenDonorIsEligible() {
            Donation expectedDonation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.UNDER_ANALYSIS
            );

            when(donorRepository.findById(eligibleDonorId)).thenReturn(Optional.of(eligibleDonor));
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
            when(donationRepository.save(any(Donation.class))).thenReturn(expectedDonation);

            Donation result = sut.registerByDonorId(eligibleDonorId, appointmentId);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(DonationStatus.UNDER_ANALYSIS);
            assertThat(result.getDonor()).isEqualTo(eligibleDonor);
            assertThat(result.getAppointment()).isEqualTo(appointment);

            verify(donationRepository, times(1)).save(any(Donation.class));
        }
    }

    @Nested
    @DisplayName("For invalid tests")
    class InvalidTests {
        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when trying to register donation without donor")
        void shouldThrowExceptionWhenTryingToRegisterDonationWithoutDonor() {
            when(donorRepository.findById(any())).thenReturn(Optional.empty());
            assertThatThrownBy(() -> sut.registerByDonorId(null, appointmentId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Donor does not exist");

            verifyNoInteractions(donationRepository);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when donor is not eligible to donate")
        void shouldThrowExceptionWhenDonorIsNotEligible() {
            when(donorRepository.findById(ineligibleDonorId)).thenReturn(Optional.of(ineligibleDonor));
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
            assertThatThrownBy(() -> sut.registerByDonorId(ineligibleDonorId, appointmentId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Donor is not eligible to donate");

            verifyNoInteractions(donationRepository);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when trying to register donation for a non-existent donor")
        void shouldThrowExceptionWhenTryingToRegisterDonationForANonExistentDonor() {
            when(donorRepository.findById(nonExistentDonorId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.registerByDonorId(nonExistentDonorId, appointmentId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Donor does not exist");

            verifyNoInteractions(donationRepository);
        }

        @Test
        @Tag("Functional")
        @Tag("UnitTest")
        @Tag("FunctionalTest")
        @DisplayName("Should throw exception when appointment is null")
        void shouldThrowExceptionWhenAppointmentIsNull() {
            when(donorRepository.findById(eligibleDonorId)).thenReturn(Optional.of(eligibleDonor));
            when(appointmentRepository.findById(any())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.registerByDonorId(eligibleDonorId, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Appointment does not exist");

            verifyNoInteractions(donationRepository);
        }

        @Test
        @Tag("Functional")
        @Tag("UnitTest")
        @Tag("FunctionalTest")
        @DisplayName("Should throw exception when appointment does not exist")
        void shouldThrowExceptionWhenAppointmentDoesNotExist() {
            when(donorRepository.findById(eligibleDonorId)).thenReturn(Optional.of(eligibleDonor));
            when(appointmentRepository.findById(nonExistentAppointmentId)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> sut.registerByDonorId(eligibleDonorId, nonExistentAppointmentId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Appointment does not exist");

            verifyNoInteractions(donationRepository);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when donation already exists for this appointment")
        void shouldThrowExceptionWhenDonationAlreadyExistsForThisAppointment() {
            when(donorRepository.findById(eligibleDonorId)).thenReturn(Optional.of(eligibleDonor));
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
            when(donationRepository.existsByDonorAndAppointment(eligibleDonor, appointment)).thenReturn(true);
            assertThatThrownBy(() -> sut.registerByDonorId(eligibleDonorId, appointmentId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Donation already exists for this appointment");

            verify(donationRepository, never()).save(any());
        }
    }
}
