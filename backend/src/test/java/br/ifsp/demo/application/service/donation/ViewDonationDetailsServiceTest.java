package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.dto.donation.DonationDetailsDTO;
import br.ifsp.demo.application.service.dto.donation.DonationWithRelations;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.infrastructure.repository.donation.DonationRepository;
import br.ifsp.demo.infrastructure.repository.exam.ExamRepository;
import br.ifsp.demo.presentation.exception.DonationNotFoundException;
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
    private ViewDonationDetailsService sut;

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should return donation details when donation exists")
        void shouldReturnDonationDetailsWhenDonationExists() {
            UUID donationId = UUID.randomUUID();

            UUID immunoId = UUID.randomUUID();
            UUID seroId = UUID.randomUUID();
            LocalDateTime immunoDate = LocalDateTime.now().minusHours(2);
            LocalDateTime seroDate = LocalDateTime.now().minusHours(1);

            ImmunohematologyExam immunohematologyExam = mock(ImmunohematologyExam.class);
            when(immunohematologyExam.getId()).thenReturn(immunoId);
            when(immunohematologyExam.getStatus()).thenReturn(ExamStatus.APPROVED);
            when(immunohematologyExam.getCreatedAt()).thenReturn(immunoDate);

            SerologicalScreeningExam serologicalScreeningExam = mock(SerologicalScreeningExam.class);
            when(serologicalScreeningExam.getId()).thenReturn(seroId);
            when(serologicalScreeningExam.getStatus()).thenReturn(ExamStatus.APPROVED);
            when(serologicalScreeningExam.getCreatedAt()).thenReturn(seroDate);

            Donation donation = mock(Donation.class);

            when(donation.getId()).thenReturn(donationId);
            when(donation.getStatus()).thenReturn(DonationStatus.UNDER_ANALYSIS);

            when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));
            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of(immunohematologyExam, serologicalScreeningExam));

            DonationDetailsDTO result = sut.getDonationDetails(donationId);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(donationId);
            assertThat(result.status()).isEqualTo(DonationStatus.UNDER_ANALYSIS);
            assertThat(result.exams()).isNotNull().hasSize(2);
            assertThat(result.exams()).allSatisfy(exam -> {
                assertThat(exam.id()).isNotNull();
                assertThat(exam.status()).isNotNull();
                assertThat(exam.performedAt()).isNotNull();
            });

            verify(donationRepository, times(1)).findById(donationId);
            verify(examRepository, times(1)).findAllByDonationId(donationId);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should return donation with exams (relations) for all donations")
        void shouldReturnDonationWithRelationsForAllDonations() {
            UUID donationId = UUID.randomUUID();

            Donation donation = mock(Donation.class);
            when(donation.getId()).thenReturn(donationId);
            when(donation.getStatus()).thenReturn(DonationStatus.UNDER_ANALYSIS);

            ImmunohematologyExam immunohematologyExam = mock(ImmunohematologyExam.class);

            SerologicalScreeningExam serologicalScreeningExam = mock(SerologicalScreeningExam.class);

            when(donationRepository.findAll()).thenReturn(List.of(donation));
            when(examRepository.findAllByDonationId(donationId))
                    .thenReturn(List.of(immunohematologyExam, serologicalScreeningExam));

            List<DonationWithRelations> result = sut.getAllDonationsWithRelations();

            assertThat(result).isNotNull().hasSize(1);

            DonationWithRelations dto = result.getFirst();
            assertThat(dto.id()).isEqualTo(donationId);
            assertThat(dto.status()).isEqualTo(DonationStatus.UNDER_ANALYSIS);
            assertThat(dto.immunohematologyExam()).isEqualTo(immunohematologyExam);
            assertThat(dto.serologicalScreeningExam()).isEqualTo(serologicalScreeningExam);

            verify(donationRepository, times(1)).findAll();
            verify(examRepository, times(1)).findAllByDonationId(donationId);
        }
    }

    @Nested
    class InvalidTests {
        @Test
        @Tag("Functional")
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
