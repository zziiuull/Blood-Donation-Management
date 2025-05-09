package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.donation.dto.DonationDetailsDTO;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.DonationNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private ViewDonationDetailsService sut;

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should return donation details when donation exists")
        void shouldReturnDonationDetailsWhenDonationExists() {
            UUID donationId = UUID.randomUUID();

            ImmunohematologyExam immunohematologyExam = mock(ImmunohematologyExam.class);
            SerologicalScreeningExam serologicalScreeningExam = mock(SerologicalScreeningExam.class);
            Donation donation = mock(Donation.class);

            when(donation.getId()).thenReturn(donationId);
            when(donation.getStatus()).thenReturn(DonationStatus.UNDER_ANALYSIS);

            when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));
            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of(immunohematologyExam, serologicalScreeningExam));

            DonationDetailsDTO result = sut.getDonationDetails(donationId);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(donationId);
            assertThat(result.status()).isEqualTo(DonationStatus.UNDER_ANALYSIS);
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

            assertThatThrownBy(() -> sut.getDonationDetails(donationId))
                    .isInstanceOf(DonationNotFoundException.class)
                    .hasMessage("Donation does not exist");

            verify(donationRepository, times(1)).findById(donationId);
        }
    }
}
