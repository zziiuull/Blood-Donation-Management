package br.ifsp.demo.application.service.donation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
            Donor eligibleDonor = new Donor();
            Appointment appointment = new Appointment();
            Donation expectedDonation = new Donation();
            Mockito.when(donationRepository.save()).thenReturn(expectedDonation);

            Donation result = donationRegisterService.register(eligibleDonor, appointment);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(DonationStatus.EM_ANDAMENTO);
            assertThat(result.getDonor()).isEqualTo(eligibleDonor);

            verify(donationRepository, times(1)).save();
    }
    }
    @Nested
    @DisplayName("For invalid tests")
    class InvalidTests {

        @Test
        @DisplayName("Should throw exception when trying to register donation without donor")
        void shouldThrowExceptionWhenTryingToRegisterDonationWithoutDonor() {
            Appointment appointment = new Appointment();

            assertThatThrownBy(() -> donationRegisterService.register(null, appointment))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Donor must not be null");

            verifyNoInteractions(donationRepository);
        }

        @Test
        @DisplayName("Should throw exception when donor is not eligible to donate")
        void shouldThrowExceptionWhenDonorIsNotEligible() {
            Donor ineligibleDonor = new Donor();
            Appointment appointment = new Appointment();

            assertThatThrownBy(() -> donationRegisterService.register(ineligibleDonor))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Donor is not eligible to donate");

            verifyNoInteractions(donationRepository);
        }
    }
}