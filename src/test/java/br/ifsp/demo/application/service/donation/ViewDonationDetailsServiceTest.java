package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.donation.dto.DonationDetailsDTO;
import br.ifsp.demo.domain.model.*;
import br.ifsp.demo.domain.model.exam.Exam;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.EntityNotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewDonationDetailsServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @Mock
    private ExamRepository examRepository;

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
            ImmunohematologyExam immunohematologyExam = mock(ImmunohematologyExam.class);
            SerologicalScreeningExam serologicalScreeningExam = mock(SerologicalScreeningExam.class);
            Donation donation = mock(Donation.class);

            when(donation.getId()).thenReturn(donationId);
            when(donation.getStatus()).thenReturn(DonationStatus.EM_ANDAMENTO);

            when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));
            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of(immunohematologyExam, serologicalScreeningExam));

            DonationDetailsDTO result = viewDonationDetailsService.getDonationDetails(donationId);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(donationId);
            assertThat(result.status()).isEqualTo(DonationStatus.EM_ANDAMENTO);
            assertThat(result.exams()).hasSize(2);

            verify(donationRepository, times(1)).findById(donationId);
            verify(examRepository, times(1)).findAllByDonationId(donationId);
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
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Donation does not exist");

            verify(donationRepository, times(1)).findById(donationId);
        }
    }
}
