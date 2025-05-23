package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.infrastructure.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamRequestNotAllowedException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

    private Donation expectedDonation;

    @BeforeEach
    void setUp() {
        Donor eligibleDonor = mock(Donor.class);
        Appointment appointment = mock(Appointment.class);
        expectedDonation = new Donation(
                eligibleDonor,
                appointment,
                DonationStatus.UNDER_ANALYSIS
        );
    }

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should request immunohematology exam if donation is registered")
        void shouldRequestImmunohematologyExamIfDonationIsRegistered(){
            ImmunohematologyExam expectedExam = new ImmunohematologyExam(expectedDonation);

            when(examRepository.save(any(ImmunohematologyExam.class))).thenReturn(expectedExam);

            ImmunohematologyExam result = sut.requestImmunohematologyExam(expectedDonation);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(expectedExam);
            assertThat(result.getDonation()).isEqualTo(expectedDonation);
            assertThat(result.getStatus()).isEqualTo(ExamStatus.UNDER_ANALYSIS);
            assertThat(result.getCreatedAt()).isNotNull();
            assertThat(result.getUpdatedAt()).isNotNull();

            verify(examRepository, times(1)).save(any(ImmunohematologyExam.class));
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should request serological screening exam if donation is registered")
        void shouldRequestSerologicalScreeningExamIfDonationIsRegistered(){
            SerologicalScreeningExam expectedExam = new SerologicalScreeningExam(expectedDonation);

            when(examRepository.save(any(SerologicalScreeningExam.class))).thenReturn(expectedExam);

            SerologicalScreeningExam result = sut.requestSerologicalScreeningExam(expectedDonation);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(expectedExam);
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
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw ExamRequestNotAllowedException when requesting immunohematology exam for approved donation")
        void shouldThrowExamRequestNotAllowedExceptionWhenRequestingImmunohematologyExamForApprovedDonation() {
            expectedDonation.setStatus(DonationStatus.APPROVED);

            assertThatThrownBy(() -> sut.requestImmunohematologyExam(expectedDonation))
                    .isInstanceOf(ExamRequestNotAllowedException.class)
                    .hasMessage("Cannot request an immunohematology exam for an approved donation");

            verifyNoInteractions(examRepository);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw ExamRequestNotAllowedException when requesting serological screening exam for approved donation")
        void shouldThrowExamRequestNotAllowedExceptionWhenRequestingSerologicalScreeningExamForApprovedDonation() {
            expectedDonation.setStatus(DonationStatus.APPROVED);

            assertThatThrownBy(() -> sut.requestSerologicalScreeningExam(expectedDonation))
                    .isInstanceOf(ExamRequestNotAllowedException.class)
                    .hasMessage("Cannot request a serological screening exam for an approved donation");

            verifyNoInteractions(examRepository);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw ExamRequestNotAllowedException when requesting immunohematology exam for rejected donation")
        void shouldThrowExamRequestNotAllowedExceptionWhenRequestingImmunohematologyExamForRejectedDonation() {
            expectedDonation.setStatus(DonationStatus.REJECTED);

            assertThatThrownBy(() -> sut.requestImmunohematologyExam(expectedDonation))
                    .isInstanceOf(ExamRequestNotAllowedException.class)
                    .hasMessage("Cannot request an immunohematology exam for a rejected donation");

            verifyNoInteractions(examRepository);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw ExamRequestNotAllowedException when requesting serological screening exam for rejected donation")
        void shouldThrowExamRequestNotAllowedExceptionWhenRequestingSerologicalScreeningExamForRejectedDonation() {
            expectedDonation.setStatus(DonationStatus.REJECTED);

            assertThatThrownBy(() -> sut.requestSerologicalScreeningExam(expectedDonation))
                    .isInstanceOf(ExamRequestNotAllowedException.class)
                    .hasMessage("Cannot request a serological screening exam for a rejected donation");

            verifyNoInteractions(examRepository);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw ExamRequestNotAllowedException when immunohematology exam already exists for donation")
        void shouldThrowExamRequestNotAllowedExceptionWhenImmunohematologyExamAlreadyExistsForDonation(){
            ImmunohematologyExam existingExam = mock(ImmunohematologyExam.class);
            when(examRepository.findAllByDonationId(expectedDonation.getId())).thenReturn(List.of(existingExam));

            assertThatThrownBy(() -> sut.requestImmunohematologyExam(expectedDonation))
                    .isInstanceOf(ExamRequestNotAllowedException.class)
                    .hasMessage("An immunohematology exam already exists for this donation");

            verify(examRepository, times(1)).findAllByDonationId(expectedDonation.getId());
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw ExamRequestNotAllowedException when immunohematology exam already exists for donation")
        void shouldThrowExamRequestNotAllowedExceptionWhenSerologicalScreeningExamAlreadyExistsForDonation(){
            SerologicalScreeningExam existingExam = mock(SerologicalScreeningExam.class);
            when(examRepository.findAllByDonationId(expectedDonation.getId())).thenReturn(List.of(existingExam));

            assertThatThrownBy(() -> sut.requestSerologicalScreeningExam(expectedDonation))
                    .isInstanceOf(ExamRequestNotAllowedException.class)
                    .hasMessage("A serological screening exam already exists for this donation");

            verify(examRepository, times(1)).findAllByDonationId(expectedDonation.getId());
        }

        @Test
        @Tag("UnitTest")
        @Tag("Functional")
        @DisplayName("Should throw IllegalArgumentException when requesting immunohematology exam if donation is null")
        void shouldThrowIllegalArgumentExceptionWhenRequestingImmunohematologyExamIfDonationIsNull(){
            assertThatThrownBy(() -> sut.requestImmunohematologyExam(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Donation must not be null");

            verifyNoInteractions(examRepository);
        }

        @Test
        @Tag("UnitTest")
        @Tag("Functional")
        @DisplayName("Should throw IllegalArgumentException when requesting immunohematology exam if donation is null")
        void shouldThrowIllegalArgumentExceptionWhenRequestingSerologicalScreeningExamIfDonationIsNull(){
            assertThatThrownBy(() -> sut.requestSerologicalScreeningExam(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Donation must not be null");

            verifyNoInteractions(examRepository);
        }
    }
}
