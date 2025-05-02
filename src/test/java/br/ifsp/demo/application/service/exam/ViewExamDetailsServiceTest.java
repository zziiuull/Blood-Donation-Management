package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.*;
import br.ifsp.demo.domain.model.exam.DiseaseDetection;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.IrregularAntibodies;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewExamDetailsServiceTest {
    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private ViewExamDetailsService sut;


    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
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
                    DonationStatus.UNDER_ANALYSIS
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

            verify(examRepository, times(1)).findAllByDonationId(donationId);
        }

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should view immunohematology exam details when immunohematology exam has been performed")
        void shouldViewImmunohematologyExamDetailsWhenImmunohematologyExamHasBeenPerformed(){
            Donor eligibleDonor = mock(Donor.class);
            Appointment appointment = mock(Appointment.class);
            Donation expectedDonation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.UNDER_ANALYSIS
            );

            UUID donationId = UUID.randomUUID();

            ImmunohematologyExam expectedExam = new ImmunohematologyExam(expectedDonation);
            expectedExam.setBloodType(BloodType.O_POS);
            expectedExam.setIrregularAntibodies(IrregularAntibodies.POSITIVE);
            expectedExam.setObservations("No observations.");

            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of(expectedExam));

            ImmunohematologyExam result = sut.viewImmunohematologyExam(donationId);

            assertThat(result.getDonation()).isEqualTo(expectedExam.getDonation());
            assertThat(result.getDonation().getStatus()).isEqualTo(expectedExam.getDonation().getStatus());
            assertThat(result.getCreatedAt()).isEqualTo(expectedExam.getCreatedAt());
            assertThat(result.getUpdatedAt()).isEqualTo(expectedExam.getUpdatedAt());
            assertThat(result.getBloodType()).isEqualTo(expectedExam.getBloodType());
            assertThat(result.getIrregularAntibodies()).isEqualTo(expectedExam.getIrregularAntibodies());
            assertThat(result.getObservations()).isEqualTo(expectedExam.getObservations());

            verify(examRepository, times(1)).findAllByDonationId(donationId);
        }

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should view serological screening exam details and return null subclass fields when serological screening exam has not been performed")
        void shouldViewSerologicalScreeningExamDetailsAndReturnNullSubclassFieldsWhenSerologicalScreeningExamHasNotBeenPerformed(){
            Donor eligibleDonor = mock(Donor.class);
            Appointment appointment = mock(Appointment.class);
            Donation expectedDonation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.UNDER_ANALYSIS
            );

            UUID donationId = UUID.randomUUID();

            SerologicalScreeningExam expectedExam = new SerologicalScreeningExam(expectedDonation);

            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of(expectedExam));

            SerologicalScreeningExam result = sut.viewSerologicalScreeningExam(donationId);

            assertThat(result.getDonation()).isEqualTo(expectedExam.getDonation());
            assertThat(result.getDonation().getStatus()).isEqualTo(expectedExam.getDonation().getStatus());
            assertThat(result.getCreatedAt()).isEqualTo(expectedExam.getCreatedAt());
            assertThat(result.getUpdatedAt()).isEqualTo(expectedExam.getUpdatedAt());
            assertThat(result.getHepatitisB()).isNull();
            assertThat(result.getHepatitisC()).isNull();
            assertThat(result.getChagasDisease()).isNull();
            assertThat(result.getSyphilis()).isNull();
            assertThat(result.getAids()).isNull();
            assertThat(result.getHtlv1_2()).isNull();
            assertThat(result.getObservations()).isNull();

            verify(examRepository, times(1)).findAllByDonationId(donationId);
        }


        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should view serological screening exam exam details when serological screening exam has been performed")
        void shouldViewSerologicalScreeningExamDetailsWhenSerologicalScreeningExamHasBeenPerformed(){
            Donor eligibleDonor = mock(Donor.class);
            Appointment appointment = mock(Appointment.class);
            Donation expectedDonation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.UNDER_ANALYSIS
            );

            UUID donationId = UUID.randomUUID();

            SerologicalScreeningExam expectedExam = new SerologicalScreeningExam(expectedDonation);
            expectedExam.setHepatitisB(DiseaseDetection.POSITIVE);
            expectedExam.setHepatitisC(DiseaseDetection.POSITIVE);
            expectedExam.setChagasDisease(DiseaseDetection.POSITIVE);
            expectedExam.setSyphilis(DiseaseDetection.POSITIVE);
            expectedExam.setAids(DiseaseDetection.POSITIVE);
            expectedExam.setHtlv1_2(DiseaseDetection.POSITIVE);
            expectedExam.setObservations("No observations.");

            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of(expectedExam));

            SerologicalScreeningExam result = sut.viewSerologicalScreeningExam(donationId);

            assertThat(result.getDonation()).isEqualTo(expectedExam.getDonation());
            assertThat(result.getDonation().getStatus()).isEqualTo(expectedExam.getDonation().getStatus());
            assertThat(result.getCreatedAt()).isEqualTo(expectedExam.getCreatedAt());
            assertThat(result.getUpdatedAt()).isEqualTo(expectedExam.getUpdatedAt());
            assertThat(result.getHepatitisB()).isEqualTo(expectedExam.getHepatitisB());
            assertThat(result.getHepatitisC()).isEqualTo(expectedExam.getHepatitisC());
            assertThat(result.getChagasDisease()).isEqualTo(expectedExam.getChagasDisease());
            assertThat(result.getSyphilis()).isEqualTo(expectedExam.getSyphilis());
            assertThat(result.getAids()).isEqualTo(expectedExam.getAids());
            assertThat(result.getHtlv1_2()).isEqualTo(expectedExam.getHtlv1_2());
            assertThat(result.getObservations()).isEqualTo(expectedExam.getObservations());

            verify(examRepository, times(1)).findAllByDonationId(donationId);
        }
    }

    @Nested
    @DisplayName("For invalid tests")
    class InvalidTests {
        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when immunohematology exam does not exist for donation")
        void shouldThrowExceptionWhenImmunohematologyExamDoesNotExistForDonation(){
            UUID donationId = UUID.randomUUID();

            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of());

            assertThatThrownBy(() -> sut.viewImmunohematologyExam(donationId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Immunohematology exam not found");

            verify(examRepository, times(1)).findAllByDonationId(donationId);
        }

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when serological screening exam does not exist for donation")
        void shouldThrowExceptionWhenSerologicalScreeningExamDoesNotExistForDonation(){
            UUID donationId = UUID.randomUUID();

            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of());

            assertThatThrownBy(() -> sut.viewSerologicalScreeningExam(donationId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Serological screening exam not found");
            
            verify(examRepository, times(1)).findAllByDonationId(donationId);
        }
    }
}
