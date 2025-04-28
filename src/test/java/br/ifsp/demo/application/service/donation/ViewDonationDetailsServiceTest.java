package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.donation.dto.DonationDetailsDTO;
import br.ifsp.demo.domain.model.*;
import br.ifsp.demo.domain.repository.DonationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewDonationDetailsServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @InjectMocks
    private ViewDonationDetailsService viewDonationDetailsService;

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should return donation details when donation exists")
        void shouldReturnDonationDetailsWhenDonationExists() {

            UUID donationId = UUID.randomUUID();

            Donor donor = mock(Donor.class);
            Appointment appointment = mock(Appointment.class);
            Exam exam = mock(Exam.class);

            when(exam.getId()).thenReturn(UUID.randomUUID());
            when(exam.getStatus()).thenReturn(ExamStatus.UNDER_ANALYSIS);
            when(exam.getPerformedAt()).thenReturn(LocalDateTime.now());

            Donation donation = mock(Donation.class);
            when(donation.getId()).thenReturn(donationId);
            when(donation.getStatus()).thenReturn(DonationStatus.EM_ANDAMENTO);
            when(donation.getExams()).thenReturn(List.of(exam));

            when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));

            DonationDetailsDTO result = viewDonationDetailsService.getDonationDetails(donationId);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(donationId);
            assertThat(result.status()).isEqualTo(DonationStatus.EM_ANDAMENTO);
            assertThat(result.exams()).hasSize(1);

            verify(donationRepository, times(1)).findById(donationId);
        }
    }

    @Nested
    class InvalidTests {
        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when donation does not exist")
        void shouldThrowExceptionWhenDonationDoesNotExist() {
            UUID donationId = UUID.randomUUID();
            when(donationRepository.findById(donationId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> viewDonationDetailsService.getDonationDetails(donationId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Donation does not exist");

            verify(donationRepository, times(1)).findById(donationId);
        }
    }
}