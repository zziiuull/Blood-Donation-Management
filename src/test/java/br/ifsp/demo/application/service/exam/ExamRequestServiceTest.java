package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.Appointment;
import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.DonationStatus;
import br.ifsp.demo.domain.model.Donor;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ExamRequestServiceTest {
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

            ExamRequestService examRequestService = new ExamRequestService();

            ImmunohematologyExam requestedExam = examRequestService.requestImmunohematologyExam(expectedDonation);

            assertThat(requestedExam).isNotNull();
            assertThat(requestedExam.getDonation()).isEqualTo(expectedDonation);
            assertThat(requestedExam.getStatus()).isEqualTo(ExamStatus.UNDER_ANALYSIS);
            assertThat(requestedExam.getCreatedAt()).isNotNull();
            assertThat(requestedExam.getUpdatedAt()).isNotNull();
        }
    }
}
