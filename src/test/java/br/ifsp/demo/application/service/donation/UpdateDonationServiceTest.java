package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.domain.model.*;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UpdateDonationServiceTest {
    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should approve donation")
        void shouldApproveDonation() {
            Donor eligibleDonor = mock(Donor.class);
            Appointment appointment = mock(Appointment.class);
            Donation donation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.UNDER_ANALYSIS
            );
            UpdateDonationService sut = new UpdateDonationService();

            sut.approve(donationId);

            assertThat(donation.getStatus()).isEqualTo(DonationStatus.APPROVED);
            assertThat(donation.getUpdatedAt()).isNotNull();
        }
    }

}
