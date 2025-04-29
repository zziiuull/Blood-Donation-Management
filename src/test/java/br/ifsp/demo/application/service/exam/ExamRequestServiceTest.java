package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.Appointment;
import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.DonationStatus;
import br.ifsp.demo.domain.model.Donor;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamRequestServiceTest {
    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private ExamRequestService sut;

    @Nested
    @DisplayName("For valid testes")
    class ValidTests {
        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should request immunohematology exam if donation is registered")
        void shouldRequestImmunohematologyExamIfDonationIsRegistered(){
            Donor eligibleDonor = mock(Donor.class);
            Appointment appointment = mock(Appointment.class);
            Donation expectedDonation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.EM_ANDAMENTO
            );

            ImmunohematologyExam expectedExam = new ImmunohematologyExam(expectedDonation);

            when(examRepository.save(any(ImmunohematologyExam.class))).thenReturn(expectedExam);

            ImmunohematologyExam result = sut.requestImmunohematologyExam(expectedDonation);

            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(expectedExam);
            assertThat(result.getDonation()).isEqualTo(expectedDonation);
            assertThat(result.getStatus()).isEqualTo(ExamStatus.UNDER_ANALYSIS);
            assertThat(result.getCreatedAt()).isNotNull();
            assertThat(result.getUpdatedAt()).isNotNull();

            verify(examRepository, times(1)).save(any(ImmunohematologyExam.class));
        }
    }
}
