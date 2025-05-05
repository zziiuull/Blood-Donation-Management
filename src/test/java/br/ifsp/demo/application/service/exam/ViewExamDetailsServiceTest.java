package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.exam.DiseaseDetection;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.IrregularAntibodies;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamNotFoundException;
import org.junit.jupiter.api.*;
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

    private UUID donationId;
    private ImmunohematologyExam firstExam;
    private ImmunohematologyExam secondExam;
    private ImmunohematologyExam expectedImuExam;
    private SerologicalScreeningExam expectedSeroExam;

    @BeforeEach
    void setUp() {
        donationId = UUID.randomUUID();
        Donor eligibleDonor = mock(Donor.class);
        Appointment appointment = mock(Appointment.class);
        Donation expectedDonation = new Donation(
                eligibleDonor,
                appointment,
                DonationStatus.UNDER_ANALYSIS
        );
        firstExam = new ImmunohematologyExam(expectedDonation);
        secondExam = new ImmunohematologyExam(expectedDonation);
        expectedImuExam = new ImmunohematologyExam(expectedDonation);
        expectedSeroExam = new SerologicalScreeningExam(expectedDonation);
    }

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should view immunohematology exam details and return null subclass fields when immunohematology exam has not been performed")
        void shouldViewImmunohematologyExamDetailsAndReturnNullSubclassFieldsWhenImmunohematologyExamHasNotBeenPerformed(){
            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of(expectedImuExam));

            ImmunohematologyExam result = sut.viewImmunohematologyExam(donationId);

            assertThat(result.getDonation()).isEqualTo(expectedImuExam.getDonation());
            assertThat(result.getDonation().getStatus()).isEqualTo(expectedImuExam.getDonation().getStatus());
            assertThat(result.getCreatedAt()).isEqualTo(expectedImuExam.getCreatedAt());
            assertThat(result.getUpdatedAt()).isEqualTo(expectedImuExam.getUpdatedAt());
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
            expectedImuExam.setBloodType(BloodType.O_POS);
            expectedImuExam.setIrregularAntibodies(IrregularAntibodies.POSITIVE);
            expectedImuExam.setObservations("No observations.");

            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of(expectedImuExam));

            ImmunohematologyExam result = sut.viewImmunohematologyExam(donationId);

            assertThat(result.getDonation()).isEqualTo(expectedImuExam.getDonation());
            assertThat(result.getDonation().getStatus()).isEqualTo(expectedImuExam.getDonation().getStatus());
            assertThat(result.getCreatedAt()).isEqualTo(expectedImuExam.getCreatedAt());
            assertThat(result.getUpdatedAt()).isEqualTo(expectedImuExam.getUpdatedAt());
            assertThat(result.getBloodType()).isEqualTo(expectedImuExam.getBloodType());
            assertThat(result.getIrregularAntibodies()).isEqualTo(expectedImuExam.getIrregularAntibodies());
            assertThat(result.getObservations()).isEqualTo(expectedImuExam.getObservations());

            verify(examRepository, times(1)).findAllByDonationId(donationId);
        }

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should view serological screening exam details and return null subclass fields when serological screening exam has not been performed")
        void shouldViewSerologicalScreeningExamDetailsAndReturnNullSubclassFieldsWhenSerologicalScreeningExamHasNotBeenPerformed(){
            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of(expectedSeroExam));

            SerologicalScreeningExam result = sut.viewSerologicalScreeningExam(donationId);

            assertThat(result.getDonation()).isEqualTo(expectedSeroExam.getDonation());
            assertThat(result.getDonation().getStatus()).isEqualTo(expectedSeroExam.getDonation().getStatus());
            assertThat(result.getCreatedAt()).isEqualTo(expectedSeroExam.getCreatedAt());
            assertThat(result.getUpdatedAt()).isEqualTo(expectedSeroExam.getUpdatedAt());
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
            expectedSeroExam.setHepatitisB(DiseaseDetection.POSITIVE);
            expectedSeroExam.setHepatitisC(DiseaseDetection.POSITIVE);
            expectedSeroExam.setChagasDisease(DiseaseDetection.POSITIVE);
            expectedSeroExam.setSyphilis(DiseaseDetection.POSITIVE);
            expectedSeroExam.setAids(DiseaseDetection.POSITIVE);
            expectedSeroExam.setHtlv1_2(DiseaseDetection.POSITIVE);
            expectedSeroExam.setObservations("No observations.");

            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of(expectedSeroExam));

            SerologicalScreeningExam result = sut.viewSerologicalScreeningExam(donationId);

            assertThat(result.getDonation()).isEqualTo(expectedSeroExam.getDonation());
            assertThat(result.getDonation().getStatus()).isEqualTo(expectedSeroExam.getDonation().getStatus());
            assertThat(result.getCreatedAt()).isEqualTo(expectedSeroExam.getCreatedAt());
            assertThat(result.getUpdatedAt()).isEqualTo(expectedSeroExam.getUpdatedAt());
            assertThat(result.getHepatitisB()).isEqualTo(expectedSeroExam.getHepatitisB());
            assertThat(result.getHepatitisC()).isEqualTo(expectedSeroExam.getHepatitisC());
            assertThat(result.getChagasDisease()).isEqualTo(expectedSeroExam.getChagasDisease());
            assertThat(result.getSyphilis()).isEqualTo(expectedSeroExam.getSyphilis());
            assertThat(result.getAids()).isEqualTo(expectedSeroExam.getAids());
            assertThat(result.getHtlv1_2()).isEqualTo(expectedSeroExam.getHtlv1_2());
            assertThat(result.getObservations()).isEqualTo(expectedSeroExam.getObservations());

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
            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of());

            assertThatThrownBy(() -> sut.viewImmunohematologyExam(donationId))
                    .isInstanceOf(ExamNotFoundException.class)
                    .hasMessage("Immunohematology exam not found");

            verify(examRepository, times(1)).findAllByDonationId(donationId);
        }

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when serological screening exam does not exist for donation")
        void shouldThrowExceptionWhenSerologicalScreeningExamDoesNotExistForDonation(){
            when(examRepository.findAllByDonationId(donationId)).thenReturn(List.of());

            assertThatThrownBy(() -> sut.viewSerologicalScreeningExam(donationId))
                    .isInstanceOf(ExamNotFoundException.class)
                    .hasMessage("Serological screening exam not found");
            
            verify(examRepository, times(1)).findAllByDonationId(donationId);
        }
    }
}
