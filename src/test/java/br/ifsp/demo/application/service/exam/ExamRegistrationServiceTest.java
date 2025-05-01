package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.*;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.IrregularAntibodies;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ExamRegistrationServiceTest {
    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private ExamRegistrationService sut;

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @DisplayName("Should approve exam")
        void shouldApproveExam() {
            Donor eligibleDonor = mock(Donor.class);
            Appointment appointment = mock(Appointment.class);
            Donation donation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.EM_ANDAMENTO
            );

            ImmunohematologyExam immunohematologyExam = new ImmunohematologyExam(donation);
            immunohematologyExam.setBloodType(BloodType.A_POS);
            immunohematologyExam.setIrregularAntibodies(IrregularAntibodies.NEGATIVE);
            LocalDateTime updatedAt = LocalDateTime.of(2025, 5, 5, 12, 0, 0);

            when(examRepository.save(any(ImmunohematologyExam.class))).thenReturn(immunohematologyExam);

            ImmunohematologyExam result = sut.register(immunohematologyExam, updatedAt);

            assertThat(result.getStatus()).isEqualTo(ExamStatus.APPROVED);
            assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
            assertThat(result.getBloodType()).isEqualTo(BloodType.A_POS);
            assertThat(result.getIrregularAntibodies()).isEqualTo(IrregularAntibodies.NEGATIVE);

            verify(examRepository, times(1)).save(any(ImmunohematologyExam.class));
        }
    }
}
