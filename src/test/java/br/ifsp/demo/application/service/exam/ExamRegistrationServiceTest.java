package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.application.service.dto.exam.ImmunohematologyExamDTO;
import br.ifsp.demo.application.service.dto.exam.SerologicalScreeningExamDTO;
import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.exam.*;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamNotFoundException;
import br.ifsp.demo.exception.ExamAlreadyAnalyzedException;
import br.ifsp.demo.exception.InvalidExamAnalysisException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
    private ImmunohematologyExamDTO approvedImmunohematologyExamDTO;
    private ImmunohematologyExamDTO rejectedImmunohematologyExamDTO;
    private SerologicalScreeningExamDTO approvedSerologicalScreeningExamDTO;
    private SerologicalScreeningExamDTO rejectedSerologicalScreeningExamDTO;
    private LocalDateTime updatedAt;

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
        approvedImmunohematologyExamDTO = new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.NEGATIVE);
        rejectedImmunohematologyExamDTO = new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.POSITIVE);
        approvedSerologicalScreeningExamDTO = new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE);
        rejectedSerologicalScreeningExamDTO = new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE);
        updatedAt = LocalDateTime.of(2020, 5, 10, 10, 10, 10);
    }

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should approve immunohematology exam")
        void shouldApproveImmunohematologyExam() {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));
            when(examRepository.save(any(ImmunohematologyExam.class))).thenReturn(immunohematologyExam);

            ImmunohematologyExam result = sut.registerApprovedExam(UUID.randomUUID(), approvedImmunohematologyExamDTO, updatedAt);

            assertThat(result.getStatus()).isEqualTo(ExamStatus.APPROVED);
            assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
            assertThat(result.getBloodType()).isEqualTo(BloodType.A_POS);
            assertThat(result.getIrregularAntibodies()).isEqualTo(IrregularAntibodies.NEGATIVE);

            verify(examRepository, times(1)).save(any(ImmunohematologyExam.class));
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should approve serological screening exam")
        void shouldApproveSerologicalScreeningExam() {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));
            when(examRepository.save(any(SerologicalScreeningExam.class))).thenReturn(serologicalScreeningExam);

            SerologicalScreeningExam result = sut.registerApprovedExam(UUID.randomUUID(), approvedSerologicalScreeningExamDTO, updatedAt);

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
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should reject immunohematology exam")
        void shouldRejectImmunohematologyExam() {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));
            when(examRepository.save(any(ImmunohematologyExam.class))).thenReturn(immunohematologyExam);

            ImmunohematologyExam result = sut.registerRejectedExam(UUID.randomUUID(), rejectedImmunohematologyExamDTO, updatedAt);

            assertThat(result.getStatus()).isEqualTo(ExamStatus.REJECTED);
            assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
            assertThat(result.getBloodType()).isEqualTo(BloodType.A_POS);
            assertThat(result.getIrregularAntibodies()).isEqualTo(IrregularAntibodies.POSITIVE);

            verify(examRepository, times(1)).save(any(ImmunohematologyExam.class));
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should reject serological screening exam")
        void shouldRejectSerologicalScreeningExam() {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));
            when(examRepository.save(any(SerologicalScreeningExam.class))).thenReturn(serologicalScreeningExam);

            SerologicalScreeningExam result = sut.registerRejectedExam(UUID.randomUUID(), rejectedSerologicalScreeningExamDTO, updatedAt);

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
        @Tag("Functional")
        @Tag("UnitTest")
        @DisplayName("Should throw when immunohematology exam is not found")
        void shouldThrowWhenImmunohematologyExamIsNotFound() {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.registerRejectedExam(UUID.randomUUID(), rejectedImmunohematologyExamDTO, updatedAt)).isInstanceOf(ExamNotFoundException.class);
        }

        @Test
        @Tag("UnitTest")
        @Tag("Structural")
        @DisplayName("Should throw when serological screening exam is not found")
        void shouldThrowWhenSerologicalScreeningExamIsNotFound() {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.registerApprovedExam(UUID.randomUUID(), approvedSerologicalScreeningExamDTO, updatedAt)).isInstanceOf(ExamNotFoundException.class);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw when immunohematology exam is no longer under analysis for approval")
        void shouldThrowWhenImmunohematologyExamIsNoLongerUnderAnalysisForApproval() {
            immunohematologyExam.setStatus(ExamStatus.REJECTED);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));

            assertThatThrownBy(() -> sut.registerApprovedExam(UUID.randomUUID(), approvedImmunohematologyExamDTO, updatedAt)).isInstanceOf(ExamAlreadyAnalyzedException.class);
        }

        @Test
        @Tag("Mutation")
        @Tag("UnitTest")
        @DisplayName("Should throw when immunohematology exam is no longer under analysis for rejection")
        void shouldThrowWhenImmunohematologyExamIsNoLongerUnderAnalysisForRejection() {
            immunohematologyExam.setStatus(ExamStatus.REJECTED);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));

            assertThatThrownBy(() -> sut.registerRejectedExam(UUID.randomUUID(), rejectedImmunohematologyExamDTO, updatedAt)).isInstanceOf(ExamAlreadyAnalyzedException.class);
        }

        @Test
        @Tag("Mutation")
        @Tag("UnitTest")
        @DisplayName("Should throw when serological screening exam is no longer under analysis for approval")
        void shouldThrowWhenSerologicalScreeningExamIsNoLongerUnderAnalysisForApproval() {
            serologicalScreeningExam.setStatus(ExamStatus.REJECTED);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));

            assertThatThrownBy(() -> sut.registerApprovedExam(UUID.randomUUID(), approvedSerologicalScreeningExamDTO, updatedAt)).isInstanceOf(ExamAlreadyAnalyzedException.class);
        }

        @Test
        @Tag("Mutation")
        @Tag("UnitTest")
        @DisplayName("Should throw when serological screening exam is no longer under analysis for rejection")
        void shouldThrowWhenSerologicalScreeningExamIsNoLongerUnderAnalysisForRejection() {
            serologicalScreeningExam.setStatus(ExamStatus.REJECTED);

            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));

            assertThatThrownBy(() -> sut.registerRejectedExam(UUID.randomUUID(), rejectedSerologicalScreeningExamDTO, updatedAt)).isInstanceOf(ExamAlreadyAnalyzedException.class);
        }

        @ParameterizedTest
        @MethodSource("invalidImmunohematologyExamForApproval")
        @Tag("UnitTest")
        @Tag("Functional")
        @DisplayName("Should throw when analysis for approving immunohematology exam is invalid")
        void shouldThrowWhenAnalysisForApprovingImmunohematologyExamIsInvalid(ImmunohematologyExamDTO exam) {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));

            assertThatThrownBy(() -> sut.registerApprovedExam(UUID.randomUUID(), exam, updatedAt)).isInstanceOf(InvalidExamAnalysisException.class);
        }

        static Stream<Arguments> invalidImmunohematologyExamForApproval() {
            return Stream.of(
                    Arguments.of(new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.POSITIVE)),
                    Arguments.of(new ImmunohematologyExamDTO(null, IrregularAntibodies.NEGATIVE))
            );
        }

        @ParameterizedTest
        @MethodSource("invalidImmunohematologyExamForRejection")
        @Tag("UnitTest")
        @Tag("Structural")
        @DisplayName("Should throw when analysis for rejecting immunohematology exam is invalid")
        void shouldThrowWhenAnalysisForRejectingImmunohematologyExamIsInvalid(ImmunohematologyExamDTO exam) {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(immunohematologyExam));

            assertThatThrownBy(() -> sut.registerRejectedExam(UUID.randomUUID(), exam, updatedAt)).isInstanceOf(InvalidExamAnalysisException.class);
        }

        static Stream<Arguments> invalidImmunohematologyExamForRejection() {
            return Stream.of(
                    Arguments.of(new ImmunohematologyExamDTO(BloodType.A_POS, IrregularAntibodies.NEGATIVE)),
                    Arguments.of(new ImmunohematologyExamDTO(null, IrregularAntibodies.POSITIVE))
            );
        }

        @ParameterizedTest
        @MethodSource("invalidSerologicalScreeningExamForApproval")
        @Tag("UnitTest")
        @Tag("Structural")
        @DisplayName("Should throw when analysis for approving serological screening exam is invalid")
        void shouldThrowWhenAnalysisForApprovingSerologicalScreeningExamIsInvalid(SerologicalScreeningExamDTO exam) {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));

            assertThatThrownBy(() -> sut.registerApprovedExam(UUID.randomUUID(), exam, updatedAt)).isInstanceOf(InvalidExamAnalysisException.class);
        }

        @ParameterizedTest
        @MethodSource("invalidSerologicalScreeningExamForRejection")
        @Tag("UnitTest")
        @Tag("Structural")
        @DisplayName("Should throw when analysis for rejecting serological screening exam is invalid")
        void shouldThrowWhenAnalysisForRejectingSerologicalScreeningExamIsInvalid(SerologicalScreeningExamDTO exam) {
            when(examRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(serologicalScreeningExam));

            assertThatThrownBy(() -> sut.registerRejectedExam(UUID.randomUUID(), exam, updatedAt)).isInstanceOf(InvalidExamAnalysisException.class);
        }

        static Stream<Arguments> invalidSerologicalScreeningExamForApproval() {
            return Stream.of(
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(null, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, null, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, null, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, null, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, null, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.NEGATIVE, null))
            );
        }

        static Stream<Arguments> invalidSerologicalScreeningExamForRejection() {
            return Stream.of(
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE, DiseaseDetection.POSITIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.NEGATIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(null, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, null, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, null, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, null, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, null, DiseaseDetection.POSITIVE)),
                    Arguments.of(new SerologicalScreeningExamDTO(DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, DiseaseDetection.POSITIVE, null))
            );
        }
    }
}
