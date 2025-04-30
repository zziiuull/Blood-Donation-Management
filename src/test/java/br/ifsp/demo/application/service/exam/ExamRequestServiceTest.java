package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.Appointment;
import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.DonationStatus;
import br.ifsp.demo.domain.model.Donor;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamRequestNotAllowedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamRequestServiceTest {
    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private ExamRequestService sut;

    @Nested
    @DisplayName("For valid tests")
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

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should request serological screening exam if donation is registered")
        void shouldRequestSerologicalScreeningExamIfDonationIsRegistered(){
            Donor eligibleDonor = mock(Donor.class);
            Appointment appointment = mock(Appointment.class);
            Donation expectedDonation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.EM_ANDAMENTO
            );

            SerologicalScreeningExam expectedExam = new SerologicalScreeningExam(expectedDonation);

            when(examRepository.save(any(SerologicalScreeningExam.class))).thenReturn(expectedExam);

            SerologicalScreeningExam result = sut.requestSerologicalScreeningExam(expectedDonation);

            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(expectedExam);
            assertThat(result.getDonation()).isEqualTo(expectedDonation);
            assertThat(result.getStatus()).isEqualTo(ExamStatus.UNDER_ANALYSIS);
            assertThat(result.getCreatedAt()).isNotNull();
            assertThat(result.getUpdatedAt()).isNotNull();

            verify(examRepository, times(1)).save(any(SerologicalScreeningExam.class));
        }
    }

    @Nested
    @DisplayName("For invalid tests")
    class InvalidTests {
        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw ExamRequestNotAllowedException when requesting immunohematology exam for approved donation")
        void shouldThrowExamRequestNotAllowedExceptionWhenRequestingImmunohematologyExamForApprovedDonation() {
            Donor eligibleDonor = mock(Donor.class);
            Appointment appointment = mock(Appointment.class);
            Donation expectedDonation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.APROVADO
            );

            assertThatThrownBy(() -> sut.requestImmunohematologyExam(expectedDonation))
                    .isInstanceOf(ExamRequestNotAllowedException.class)
                    .hasMessage("Cannot request an immunohematology exam for an approved donation");

            verifyNoInteractions(examRepository);
        }
        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw ExamRequestNotAllowedException when requesting serological screening exam for approved donation")
        void shouldThrowExamRequestNotAllowedExceptionWhenRequestingSerologicalScreeningExamForApprovedDonation() {
            Donor eligibleDonor = mock(Donor.class);
            Appointment appointment = mock(Appointment.class);
            Donation expectedDonation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.APROVADO
            );

            assertThatThrownBy(() -> sut.requestSerologicalScreeningExam(expectedDonation))
                    .isInstanceOf(ExamRequestNotAllowedException.class)
                    .hasMessage("Cannot request an serological screening exam for an approved donation");
        }
    }
}
