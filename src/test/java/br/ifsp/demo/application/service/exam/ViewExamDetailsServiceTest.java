package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.Appointment;
import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.DonationStatus;
import br.ifsp.demo.domain.model.Donor;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ViewExamDetailsServiceTest {
    private ViewExamDetailsService sut = new ViewExamDetailsService();

    @Test
    @Tag("TDD")
    @Tag("UnitTest")
    @DisplayName("Should view exam details when donation is registered and has an immunohematology exam")
    void shouldViewExamDetailsWhenDonationIsRegisteredAndHasAnImmunohematologyExam(){
        Donor eligibleDonor = mock(Donor.class);
        Appointment appointment = mock(Appointment.class);
        Donation expectedDonation = new Donation(
                eligibleDonor,
                appointment,
                DonationStatus.EM_ANDAMENTO
        );

        UUID donationId = UUID.randomUUID();

        ImmunohematologyExam expectedExam = new ImmunohematologyExam(expectedDonation);
        ImmunohematologyExam result = sut.viewImmunohematologyExam(donationId);

        assertThat(result.getDonation()).isEqualTo(expectedExam.getDonation());
        assertThat(result.getDonation().getStatus()).isEqualTo(expectedExam.getDonation().getStatus());
        assertThat(result.getCreatedAt()).isEqualTo(expectedExam.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(expectedExam.getUpdatedAt());
        assertThat(result.getBloodType()).isEqualTo(expectedExam.getBloodType());
        assertThat(result.getIrregularAntibodies()).isEqualTo(expectedExam.getIrregularAntibodies());
        assertThat(result.getObservations()).isEqualTo(expectedExam.getObservations());
    }
}
