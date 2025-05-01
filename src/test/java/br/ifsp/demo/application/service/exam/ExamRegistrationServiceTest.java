package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.*;
import br.ifsp.demo.domain.model.exam.*;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamAlreadyAnalyzedException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ExamRegistrationServiceTest {
    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private ExamRegistrationService sut;

    private ImmunohematologyExam immunohematologyExam;
    private SerologicalScreeningExam serologicalScreeningExam;

    @BeforeEach
    void setUp(){
        Donor eligibleDonor = mock(Donor.class);
        Appointment appointment = mock(Appointment.class);
        Donation donation = new Donation(
                eligibleDonor,
                appointment,
                DonationStatus.EM_ANDAMENTO
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
            immunohematologyExam.setBloodType(BloodType.A_POS);
            immunohematologyExam.setIrregularAntibodies(IrregularAntibodies.NEGATIVE);
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            when(examRepository.save(any(ImmunohematologyExam.class))).thenReturn(immunohematologyExam);

            ImmunohematologyExam result = sut.registerApprovedExam(immunohematologyExam, updatedAt);

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
            serologicalScreeningExam.setHepatitisB(DiseaseDetection.NEGATIVE);
            serologicalScreeningExam.setHepatitisC(DiseaseDetection.NEGATIVE);
            serologicalScreeningExam.setChagasDisease(DiseaseDetection.NEGATIVE);
            serologicalScreeningExam.setSyphilis(DiseaseDetection.NEGATIVE);
            serologicalScreeningExam.setAids(DiseaseDetection.NEGATIVE);
            serologicalScreeningExam.setHtlv1_2(DiseaseDetection.NEGATIVE);
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            when(examRepository.save(any(SerologicalScreeningExam.class))).thenReturn(serologicalScreeningExam);

            SerologicalScreeningExam result = sut.registerApprovedExam(serologicalScreeningExam, updatedAt);

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
            immunohematologyExam.setBloodType(BloodType.A_POS);
            immunohematologyExam.setIrregularAntibodies(IrregularAntibodies.POSITIVE);
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            when(examRepository.save(any(ImmunohematologyExam.class))).thenReturn(immunohematologyExam);

            ImmunohematologyExam result = sut.registerRejectedExam(immunohematologyExam, updatedAt);

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
            serologicalScreeningExam.setHepatitisB(DiseaseDetection.POSITIVE);
            serologicalScreeningExam.setHepatitisC(DiseaseDetection.POSITIVE);
            serologicalScreeningExam.setChagasDisease(DiseaseDetection.POSITIVE);
            serologicalScreeningExam.setSyphilis(DiseaseDetection.POSITIVE);
            serologicalScreeningExam.setAids(DiseaseDetection.POSITIVE);
            serologicalScreeningExam.setHtlv1_2(DiseaseDetection.POSITIVE);
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            when(examRepository.save(any(SerologicalScreeningExam.class))).thenReturn(serologicalScreeningExam);

            SerologicalScreeningExam result = sut.registerRejectedExam(serologicalScreeningExam, updatedAt);

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
        @DisplayName("Should throw ExamAlreadyAnalyzedException when exam is no longer under analysis")
        void shouldThrowExamAlreadyAnalyzedExceptionWhenExamIsNoLongerUnderAnalysis() {
            immunohematologyExam.setStatus(ExamStatus.REJECTED);
            LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

            assertThatThrownBy(()->sut.registerApprovedExam(immunohematologyExam, updatedAt)).isInstanceOf(ExamAlreadyAnalyzedException.class);
        }
    }
}
