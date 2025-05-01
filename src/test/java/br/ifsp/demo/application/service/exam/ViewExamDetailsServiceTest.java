package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.Appointment;
import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.DonationStatus;
import br.ifsp.demo.domain.model.Donor;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewExamDetailsServiceTest {
    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private ViewExamDetailsService sut;

    @Test
    @Tag("TDD")
    @Tag("UnitTest")
    @DisplayName("Should view immunohematology exam details and return null subclass fields when immunohematology exam has not been performed")
    void shouldViewImmunohematologyExamDetailsAndReturnNullSubclassFieldsWhenImmunohematologyExamHasNotBeenPerformed(){
        Donor eligibleDonor = mock(Donor.class);
        Appointment appointment = mock(Appointment.class);
        Donation expectedDonation = new Donation(
                eligibleDonor,
                appointment,
                DonationStatus.EM_ANDAMENTO
        );

        UUID donationId = UUID.randomUUID();

        ImmunohematologyExam expectedExam = new ImmunohematologyExam(expectedDonation);

        when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of(expectedExam));

        ImmunohematologyExam result = sut.viewImmunohematologyExam(donationId);

        assertThat(result.getDonation()).isEqualTo(expectedExam.getDonation());
        assertThat(result.getDonation().getStatus()).isEqualTo(expectedExam.getDonation().getStatus());
        assertThat(result.getCreatedAt()).isEqualTo(expectedExam.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(expectedExam.getUpdatedAt());
        assertThat(result.getBloodType()).isNull();
        assertThat(result.getIrregularAntibodies()).isNull();
        assertThat(result.getObservations()).isNull();
    }

    @Test
    @Tag("TDD")
    @Tag("UnitTest")
    @DisplayName("Should view exam details when donation is registered and has an serological screening exam")
    void shouldViewSerologicalScreeningExamDetailsAndReturnNullSubclassFieldsWhenSerologicalScreeningExamHasNotBeenPerformed(){
        Donor eligibleDonor = mock(Donor.class);
        Appointment appointment = mock(Appointment.class);
        Donation expectedDonation = new Donation(
                eligibleDonor,
                appointment,
                DonationStatus.EM_ANDAMENTO
        );

        UUID donationId = UUID.randomUUID();

        SerologicalScreeningExam expectedExam = new SerologicalScreeningExam(expectedDonation);

        when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of(expectedExam));

        SerologicalScreeningExam result = sut.viewSerologicalScreeningExam(donationId);

        assertThat(result.getDonation()).isEqualTo(expectedExam.getDonation());
        assertThat(result.getDonation().getStatus()).isEqualTo(expectedExam.getDonation().getStatus());
        assertThat(result.getCreatedAt()).isEqualTo(expectedExam.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(expectedExam.getUpdatedAt());
        assertThat(result.getHepatitisB()).isNotNull();
        assertThat(result.getHepatitisC()).isNotNull();
        assertThat(result.getChagasDisease()).isNotNull();
        assertThat(result.getSyphilis()).isNotNull();
        assertThat(result.getAids()).isNotNull();
        assertThat(result.getHtlv1_2()).isNotNull();
        assertThat(result.getObservations()).isNull();
    }
}
