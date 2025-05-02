package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.application.service.exam.dto.ImmunohematologyExamDTO;
import br.ifsp.demo.application.service.exam.dto.SerologicalScreeningExamDTO;
import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.exam.*;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.EntityNotFoundException;
import br.ifsp.demo.exception.ExamAlreadyAnalyzedException;
import br.ifsp.demo.exception.InvalidExamAnalysisException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));
            when(examRepository.save(any(ImmunohematologyExam.class))).thenReturn(immunohematologyExam);

            ImmunohematologyExam result = sut.registerApprovedExam(UUID.randomUUID(), new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.NEGATIVE));

            assertThat(result.getStatus()).isEqualTo(ExamStatus.APPROVED);
            assertThat(result.getUpdatedAt()).isNotNull();
            assertThat(result.getBloodType()).isEqualTo(BloodType.A_POS);
            assertThat(result.getIrregularAntibodies()).isEqualTo(IrregularAntibodies.NEGATIVE);

            verify(examRepository, times(1)).save(any(ImmunohematologyExam.class));
        }

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should approve serological screening exam")
        void shouldApproveSerologicalScreeningExam() {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));
            when(examRepository.save(any(SerologicalScreeningExam.class))).thenReturn(serologicalScreeningExam);

            SerologicalScreeningExam result = sut.registerApprovedExam(UUID.randomUUID(), new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE));

            assertThat(result.getStatus()).isEqualTo(ExamStatus.APPROVED);
            assertThat(result.getUpdatedAt()).isNotNull();
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
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));
            when(examRepository.save(any(ImmunohematologyExam.class))).thenReturn(immunohematologyExam);

            ImmunohematologyExam result = sut.registerRejectedExam(UUID.randomUUID(), new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.POSITIVE));

            assertThat(result.getStatus()).isEqualTo(ExamStatus.REJECTED);
            assertThat(result.getUpdatedAt()).isNotNull();
            assertThat(result.getBloodType()).isEqualTo(BloodType.A_POS);
            assertThat(result.getIrregularAntibodies()).isEqualTo(IrregularAntibodies.POSITIVE);

            verify(examRepository, times(1)).save(any(ImmunohematologyExam.class));
        }

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should reject serological screening exam")
        void shouldRejectSerologicalScreeningExam() {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));
            when(examRepository.save(any(SerologicalScreeningExam.class))).thenReturn(serologicalScreeningExam);

            SerologicalScreeningExam result = sut.registerRejectedExam(UUID.randomUUID(), new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE));

            assertThat(result.getStatus()).isEqualTo(ExamStatus.REJECTED);
            assertThat(result.getUpdatedAt()).isNotNull();
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

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));

            assertThatThrownBy(() -> sut.registerApprovedExam(UUID.randomUUID(), new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.NEGATIVE))).isInstanceOf(ExamAlreadyAnalyzedException.class);
        }

        @Test
        @Tag("UnitTest")
        @DisplayName("Should throw when analysis for approving immunohematology exam in contradictory")
        void shouldThrowWhenAnalysisForApprovingImmunohematologyExamInContradictory() {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));

            assertThatThrownBy(() -> sut.registerApprovedExam(UUID.randomUUID(), new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.POSITIVE))).isInstanceOf(InvalidExamAnalysisException.class);
        }

        @Test
        @Tag("UnitTest")
        @DisplayName("Should throw when analysis for approving serological screening exam in contradictory")
        void shouldThrowWhenAnalysisForApprovingSerologicalScreeningExamInContradictory() {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));

            assertThatThrownBy(() -> sut.registerApprovedExam(UUID.randomUUID(), new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE))).isInstanceOf(InvalidExamAnalysisException.class);
        }

        @Test
        @Tag("UnitTest")
        @DisplayName("Should throw when analysis for rejecting serological screening exam in contradictory")
        void shouldThrowWhenAnalysisForRejectingSerologicalScreeningExamInContradictory() {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));

            assertThatThrownBy(() -> sut.registerRejectedExam(UUID.randomUUID(), new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE))).isInstanceOf(InvalidExamAnalysisException.class);
        }

        @Test
        @Tag("UnitTest")
        @DisplayName("Should throw when exam is not found")
        void shouldThrowWhenExamIsNotFound() {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.registerRejectedExam(UUID.randomUUID(), new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.POSITIVE))).isInstanceOf(EntityNotFoundException.class);
        }
    }
}
