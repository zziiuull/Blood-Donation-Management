package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.notifier.NotifierService;
import br.ifsp.demo.domain.model.*;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import br.ifsp.demo.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDonationServiceTest {
    @Mock
    private DonationRepository donationRepository;

    @Mock
    private NotifierService notifierService;

    @InjectMocks
    private UpdateDonationService sut;

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

            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(donation));
            when(donationRepository.save(any(Donation.class))).thenReturn(donation);

            Donation result = sut.approve(UUID.randomUUID());

            assertThat(result.getStatus()).isEqualTo(DonationStatus.APPROVED);
            assertThat(result.getUpdatedAt()).isNotNull();
            verify(donationRepository, times(1)).save(any(Donation.class));
            verify(notifierService, times(1)).notify(any(Donor.class), anyString());
        }

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should reject donation")
        void shouldRejectDonation() {
            Donor eligibleDonor = mock(Donor.class);
            Appointment appointment = mock(Appointment.class);
            Donation donation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.UNDER_ANALYSIS
            );

            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(donation));
            when(donationRepository.save(any(Donation.class))).thenReturn(donation);

            Donation result = sut.reject(UUID.randomUUID());

            assertThat(result.getStatus()).isEqualTo(DonationStatus.REJECTED);
            assertThat(result.getUpdatedAt()).isNotNull();
            verify(donationRepository, times(1)).save(any(Donation.class));
            verify(notifierService, times(1)).notify(any(Donor.class), anyString());
        }
    }
}
