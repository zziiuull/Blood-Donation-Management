package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.domain.model.*;
import br.ifsp.demo.domain.repository.DonationRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationRegisterServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @InjectMocks
    private DonationRegisterService donationRegisterService;

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {

        @Test
        @DisplayName("Should register donation when donor is eligible")
        void shouldRegisterDonationWhenDonorIsEligible() {

            ContactInfo donorContactInfo = new ContactInfo(
                    "weverton@email.com",
                    "11991239867",
                    "Rua da Ponte Caída, n. 101, Itaquaquecetuba/SP"
            );

            Donor eligibleDonor = new Donor(
                    "Weverton",
                    new Cpf("12345678955"),
                    donorContactInfo,
                    LocalDate.of(1990, 5, 20),
                    85.0,
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

            Appointment appointment = new Appointment(
                    LocalDateTime.now().plusDays(1),
                    AppointmentStatus.SCHEDULED,
                    site,
                    "First donation"
            );
            Donation expectedDonation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.EM_ANDAMENTO
            );

            when(donationRepository.save(any(Donation.class))).thenReturn(expectedDonation);

            Donation result = donationRegisterService.register(eligibleDonor, appointment);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(DonationStatus.EM_ANDAMENTO);
            assertThat(result.getDonor()).isEqualTo(eligibleDonor);
            assertThat(result.getAppointment()).isEqualTo(appointment);

            verify(donationRepository, times(1)).save(any(Donation.class));
        }

    }
    @Nested
    @DisplayName("For invalid tests")
    class InvalidTests {

        @Test
        @DisplayName("Should throw exception when trying to register donation without donor")
        void shouldThrowExceptionWhenTryingToRegisterDonationWithoutDonor() {
            ContactInfo siteContactInfo = new ContactInfo(
                    "doesangue.sorocaba@email.com",
                    "1533761530",
                    "Av. Anhanguera, n. 715, Sorocaba/SP"
            );
            CollectionSite site = new CollectionSite(
                    "Banco de Doação de Sorocaba",
                    siteContactInfo
            );

            Appointment appointment = new Appointment(
                    LocalDateTime.now().plusDays(1),
                    AppointmentStatus.SCHEDULED,
                    site,
                    "First donation"
            );
            assertThatThrownBy(() -> donationRegisterService.register(null, appointment))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Donor must not be null");

            verifyNoInteractions(donationRepository);
        }

        @Test
        @DisplayName("Should throw exception when donor is not eligible to donate")
        void shouldThrowExceptionWhenDonorIsNotEligible() {
            ContactInfo donorContactInfo = new ContactInfo(
                    "enzo@email.com",
                    "11991239867",
                    "Rua da Ponte Caída, n. 101, Itaquaquecetuba/SP"
            );

            Donor ineligibleDonor = new Donor(
                    "Enzo",
                    new Cpf("12345678955"),
                    donorContactInfo,
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

            Appointment appointment = new Appointment(
                    LocalDateTime.now().plusDays(1),
                    AppointmentStatus.SCHEDULED,
                    site,
                    "First donation"
            );


            assertThatThrownBy(() -> donationRegisterService.register(ineligibleDonor, appointment))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Donor is not eligible to donate");

            verifyNoInteractions(donationRepository);
        }

        @Test
        @DisplayName("Should throw exception when trying to register donation for a non-existent donor")
        void shouldThrowExceptionWhenTryingToRegisterDonationForANonExistentDonor() {
            UUID nonExistentDonorId = UUID.randomUUID();

            Appointment appointment = new Appointment();

            when(donorRepository.findById(nonExistentDonorId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> donationRegisterService.registerByDonorId(nonExistentDonorId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Donor does not exist");

            verifyNoInteractions(donationRepository);
        }
    }
}