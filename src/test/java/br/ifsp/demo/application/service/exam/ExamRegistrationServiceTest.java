package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.application.service.exam.dto.ImmunohematologyExamDTO;
import br.ifsp.demo.application.service.exam.dto.SerologicalScreeningExamDTO;
import br.ifsp.demo.domain.model.*;
import br.ifsp.demo.domain.model.exam.*;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamAlreadyAnalyzedException;
import br.ifsp.demo.exception.InvalidExamAnalysisException;
import br.ifsp.demo.exception.InvalidUpdatedTimeException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamRegistrationServiceTest {
    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private ExamRegistrationService sut;

    private ImmunohematologyExam immunohematologyExam;
    private SerologicalScreeningExam serologicalScreeningExam;

    @BeforeEach
    void setUp() {
        Donor eligibleDonor = mock(Donor.class);
        Appointment appointment = mock(Appointment.class);
        Donation donation = new Donation(
                eligibleDonor,
                appointment,
                DonationStatus.UNDER_ANALYSIS
        );
        immunohematologyExam = new ImmunohematologyExam(donation);
        serologicalScreeningExam = new SerologicalScreeningExam(donation);
    }

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should approve immunohematology exam")
        void shouldApproveImmunohematologyExam() {
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));
            when(examRepository.save(any(ImmunohematologyExam.class))).thenReturn(immunohematologyExam);

            ImmunohematologyExam result = sut.registerApprovedExam(UUID.randomUUID(), new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.NEGATIVE, updatedAt));

            assertThat(result.getStatus()).isEqualTo(ExamStatus.APPROVED);
            assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
            assertThat(result.getBloodType()).isEqualTo(BloodType.A_POS);
            assertThat(result.getIrregularAntibodies()).isEqualTo(IrregularAntibodies.NEGATIVE);

            verify(examRepository, times(1)).save(any(ImmunohematologyExam.class));
        }

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should approve serological screening exam")
        void shouldApproveSerologicalScreeningExam() {
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));
            when(examRepository.save(any(SerologicalScreeningExam.class))).thenReturn(serologicalScreeningExam);

            SerologicalScreeningExam result = sut.registerApprovedExam(UUID.randomUUID(), new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, updatedAt));

            assertThat(result.getStatus()).isEqualTo(ExamStatus.APPROVED);
            assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
            assertThat(result.getHepatitisB()).isEqualTo(DiseaseDetection.NEGATIVE);
            assertThat(result.getHepatitisC()).isEqualTo(DiseaseDetection.NEGATIVE);
            assertThat(result.getChagasDisease()).isEqualTo(DiseaseDetection.NEGATIVE);
            assertThat(result.getSyphilis()).isEqualTo(DiseaseDetection.NEGATIVE);
            assertThat(result.getAids()).isEqualTo(DiseaseDetection.NEGATIVE);
            assertThat(result.getHtlv1_2()).isEqualTo(DiseaseDetection.NEGATIVE);

            verify(examRepository, times(1)).save(any(SerologicalScreeningExam.class));
        }

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should reject immunohematology exam")
        void shouldRejectImmunohematologyExam() {
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));
            when(examRepository.save(any(ImmunohematologyExam.class))).thenReturn(immunohematologyExam);

            ImmunohematologyExam result = sut.registerRejectedExam(UUID.randomUUID(), new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.POSITIVE, updatedAt));

            assertThat(result.getStatus()).isEqualTo(ExamStatus.REJECTED);
            assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
            assertThat(result.getBloodType()).isEqualTo(BloodType.A_POS);
            assertThat(result.getIrregularAntibodies()).isEqualTo(IrregularAntibodies.POSITIVE);

            verify(examRepository, times(1)).save(any(ImmunohematologyExam.class));
        }

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should reject serological screening exam")
        void shouldRejectSerologicalScreeningExam() {
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));
            when(examRepository.save(any(SerologicalScreeningExam.class))).thenReturn(serologicalScreeningExam);

            SerologicalScreeningExam result = sut.registerRejectedExam(UUID.randomUUID(), new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, updatedAt));

            assertThat(result.getStatus()).isEqualTo(ExamStatus.REJECTED);
            assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
            assertThat(result.getHepatitisB()).isEqualTo(DiseaseDetection.POSITIVE);
            assertThat(result.getHepatitisC()).isEqualTo(DiseaseDetection.POSITIVE);
            assertThat(result.getChagasDisease()).isEqualTo(DiseaseDetection.POSITIVE);
            assertThat(result.getSyphilis()).isEqualTo(DiseaseDetection.POSITIVE);
            assertThat(result.getAids()).isEqualTo(DiseaseDetection.POSITIVE);
            assertThat(result.getHtlv1_2()).isEqualTo(DiseaseDetection.POSITIVE);

            verify(examRepository, times(1)).save(any(SerologicalScreeningExam.class));
        }
    }

    @Nested
    @DisplayName("For invalid tests")
    class InvalidTests {
        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw ExamAlreadyAnalyzedException when exam is no longer under analysis")
        void shouldThrowExamAlreadyAnalyzedExceptionWhenExamIsNoLongerUnderAnalysis() {
            immunohematologyExam.setStatus(ExamStatus.REJECTED);
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));

            assertThatThrownBy(() -> sut.registerApprovedExam(UUID.randomUUID(), new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.NEGATIVE, updatedAt))).isInstanceOf(ExamAlreadyAnalyzedException.class);
        }

        @Test
        @Tag("UnitTest")
        @DisplayName("should throw InvalidUpdatedTimeException when update time is not in the future")
        void shouldThrowInvalidUpdatedTimeExceptionWhenUpdateTimeIsNotInTheFuture() {
            LocalDateTime updatedAt = LocalDateTime.now().minusDays(1);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));

            assertThatThrownBy(()->sut.registerApprovedExam(UUID.randomUUID(), new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.POSITIVE, updatedAt))).isInstanceOf(InvalidUpdatedTimeException.class);
        }

        @Test
        @Tag("UnitTest")
        @DisplayName("Should throw when analysis for approving immunohematology exam in contradictory")
        void shouldThrowWhenAnalysisForApprovingImmunohematologyExamInContradictory() {
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));

            assertThatThrownBy(() -> sut.registerApprovedExam(UUID.randomUUID(), new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.POSITIVE, updatedAt))).isInstanceOf(InvalidExamAnalysisException.class);
        }

        @Test
        @Tag("UnitTest")
        @DisplayName("Should throw when analysis for approving serological screening exam in contradictory")
        void shouldThrowWhenAnalysisForApprovingSerologicalScreeningExamInContradictory() {
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));

            assertThatThrownBy(() -> sut.registerApprovedExam(UUID.randomUUID(), new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, updatedAt))).isInstanceOf(InvalidExamAnalysisException.class);
        }

        @Test
        @Tag("UnitTest")
        @DisplayName("Should throw when analysis for rejecting serological screening exam in contradictory")
        void shouldThrowWhenAnalysisForRejectingSerologicalScreeningExamInContradictory() {
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));

            assertThatThrownBy(() -> sut.registerRejectedExam(UUID.randomUUID(), new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, updatedAt))).isInstanceOf(InvalidExamAnalysisException.class);
        }
    }
}
