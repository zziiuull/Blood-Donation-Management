package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.notifier.NotifierService;
import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.exam.Exam;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.infrastructure.repository.donation.DonationRepository;
import br.ifsp.demo.infrastructure.repository.exam.ExamRepository;
import br.ifsp.demo.presentation.exception.CannotFinishDonationWithExamUnderAnalysisException;
import br.ifsp.demo.presentation.exception.DonationNotFoundException;
import br.ifsp.demo.presentation.exception.ExamNotFoundException;
import br.ifsp.demo.presentation.exception.InvalidDonationAnalysisException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDonationServiceTest {
    @Mock
    private DonationRepository donationRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private NotifierService notifierService;

    @InjectMocks
    private UpdateDonationService sut;

    private Donation donation;
    private Donation approvedDonation;
    private ImmunohematologyExam immunohematologyApproved;
    private ImmunohematologyExam immunohematologyRejected;
    private SerologicalScreeningExam serologicalScreeningApproved;
    private SerologicalScreeningExam serologicalScreeningRejected;
    private LocalDateTime updatedAt;

    @BeforeEach
    void setUp() {
        Donor eligibleDonor = mock(Donor.class);
        Appointment appointment = mock(Appointment.class);
        donation = new Donation(
                eligibleDonor,
                appointment,
                DonationStatus.UNDER_ANALYSIS
        );
        approvedDonation = new Donation(
                eligibleDonor,
                appointment,
                DonationStatus.APPROVED
        );
        immunohematologyApproved = new ImmunohematologyExam(donation);
        immunohematologyApproved.setStatus(ExamStatus.APPROVED);
        immunohematologyRejected = new ImmunohematologyExam(donation);
        immunohematologyRejected.setStatus(ExamStatus.REJECTED);
        serologicalScreeningApproved = new SerologicalScreeningExam(donation);
        serologicalScreeningApproved.setStatus(ExamStatus.APPROVED);
        serologicalScreeningRejected = new SerologicalScreeningExam(donation);
        serologicalScreeningRejected.setStatus(ExamStatus.REJECTED);
        updatedAt = LocalDateTime.of(2020, 5, 10, 10, 10, 10);
    }

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should approve donation")
        void shouldApproveDonation() {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(donation));
            when(examRepository.findAllByDonationId(any(UUID.class))).thenReturn(List.of(immunohematologyApproved, serologicalScreeningApproved));
            when(donationRepository.save(any(Donation.class))).thenReturn(donation);

            Donation result = sut.approve(UUID.randomUUID(), updatedAt);

            assertThat(result.getStatus()).isEqualTo(DonationStatus.APPROVED);
            assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
            verify(donationRepository, times(1)).save(any(Donation.class));
            verify(notifierService, times(1)).notify(any(Donor.class), anyString());
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should reject donation")
        void shouldRejectDonation() {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(donation));
            when(examRepository.findAllByDonationId(any(UUID.class))).thenReturn(List.of(immunohematologyRejected, serologicalScreeningRejected));
            when(donationRepository.save(any(Donation.class))).thenReturn(donation);

            Donation result = sut.reject(UUID.randomUUID(), updatedAt);

            assertThat(result.getStatus()).isEqualTo(DonationStatus.REJECTED);
            assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);            verify(donationRepository, times(1)).save(any(Donation.class));
            verify(notifierService, times(1)).notify(any(Donor.class), anyString());
        }
    }

    @Nested
    @DisplayName("For invalid tests")
    class InvalidTests {
        @ParameterizedTest
        @MethodSource("examsUnderAnalysis")
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw when at least one exam is under analysis for approval")
        void shouldThrowWhenAtLeastOneExamIsUnderAnalysisForApproval(ImmunohematologyExam immunohematologyExam, SerologicalScreeningExam serologicalScreeningExam) {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(donation));
            when(examRepository.findAllByDonationId(any(UUID.class))).thenReturn(List.of(immunohematologyExam, serologicalScreeningExam));

            assertThatThrownBy(() -> sut.approve(UUID.randomUUID(), updatedAt)).isInstanceOf(CannotFinishDonationWithExamUnderAnalysisException.class);
        }

        static Stream<Arguments> examsUnderAnalysis() {
            Donor eligibleDonor = mock(Donor.class);
            Appointment appointment = mock(Appointment.class);
            Donation donation = new Donation(
                    eligibleDonor,
                    appointment,
                    DonationStatus.UNDER_ANALYSIS
            );
            ImmunohematologyExam immunohematologyUnderAnalysis = new ImmunohematologyExam(donation);
            ImmunohematologyExam immunohematologyApproved = new ImmunohematologyExam(donation);
            immunohematologyApproved.setStatus(ExamStatus.APPROVED);
            SerologicalScreeningExam serologicalScreeningUnderAnalysis = new SerologicalScreeningExam(donation);
            SerologicalScreeningExam serologicalScreeningRejected = new SerologicalScreeningExam(donation);
            serologicalScreeningRejected.setStatus(ExamStatus.REJECTED);

            return Stream.of(
                    Arguments.of(immunohematologyUnderAnalysis, serologicalScreeningRejected),
                    Arguments.of(immunohematologyApproved, serologicalScreeningUnderAnalysis)
            );
        }

        @ParameterizedTest
        @MethodSource("examsUnderAnalysis")
        @Tag("Structural")
        @Tag("UnitTest")
        @DisplayName("Should throw when at least one exam is under analysis for rejection")
        void shouldThrowWhenAtLeastOneExamIsUnderAnalysisForRejection(ImmunohematologyExam immunohematologyExam, SerologicalScreeningExam serologicalScreeningExam) {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(donation));
            when(examRepository.findAllByDonationId(any(UUID.class))).thenReturn(List.of(immunohematologyExam, serologicalScreeningExam));

            assertThatThrownBy(() -> sut.reject(UUID.randomUUID(), updatedAt)).isInstanceOf(CannotFinishDonationWithExamUnderAnalysisException.class);
        }

        @ParameterizedTest
        @MethodSource("foundExam")
        @Tag("Functional")
        @Tag("UnitTest")
        @DisplayName("should throw when at least one exam is not found")
        void shouldThrowWhenAtLeastOneExamIsNotFound(Exam exam) {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(donation));
            when(examRepository.findAllByDonationId(any(UUID.class))).thenReturn(List.of(exam));

            assertThatThrownBy(() -> sut.approve(UUID.randomUUID(), updatedAt)).isInstanceOf(ExamNotFoundException.class);
        }

        static Stream<Arguments> foundExam(){
            Donation donation = mock(Donation.class);
            ImmunohematologyExam immunohematologyApproved = new ImmunohematologyExam(donation);
            immunohematologyApproved.setStatus(ExamStatus.APPROVED);
            SerologicalScreeningExam serologicalScreeningApproved = new SerologicalScreeningExam(donation);
            serologicalScreeningApproved.setStatus(ExamStatus.APPROVED);

            return Stream.of(
                    Arguments.of(immunohematologyApproved),
                    Arguments.of(serologicalScreeningApproved)
            );
        }

        @Test
        @Tag("Functional")
        @Tag("UnitTest")
        @DisplayName("should throw when analysis is contradictory for approval")
        void shouldThrowWhenAnalysisIsContradictoryForApproval() {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(donation));
            when(examRepository.findAllByDonationId(any(UUID.class))).thenReturn(List.of(immunohematologyRejected, serologicalScreeningApproved));

            assertThatThrownBy(() -> sut.approve(UUID.randomUUID(), updatedAt)).isInstanceOf(InvalidDonationAnalysisException.class);
        }

        @Test
        @Tag("Functional")
        @Tag("UnitTest")
        @DisplayName("should throw when analysis is contradictory for rejection")
        void shouldThrowWhenAnalysisIsContradictoryForRejection() {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(donation));
            when(examRepository.findAllByDonationId(any(UUID.class))).thenReturn(List.of(immunohematologyApproved, serologicalScreeningRejected));

            assertThatThrownBy(() -> sut.approve(UUID.randomUUID(), updatedAt)).isInstanceOf(InvalidDonationAnalysisException.class);
        }

        @Test
        @Tag("Functional")
        @Tag("UnitTest")
        @DisplayName("should throw DonationNotFoundException when donation is not found for approval")
        void shouldThrowDonationNotFoundExceptionWhenDonationIsNotFoundForApproval() {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.approve(UUID.randomUUID(), updatedAt)).isInstanceOf(DonationNotFoundException.class);
        }

        @Test
        @Tag("UnitTest")
        @Tag("Structural")
        @DisplayName("should throw DonationNotFoundException when donation is not found for rejection")
        void shouldThrowDonationNotFoundExceptionWhenDonationIsNotFoundForRejection() {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.reject(UUID.randomUUID(), updatedAt)).isInstanceOf(DonationNotFoundException.class);
        }

        @Test
        @Tag("UnitTest")
        @Tag("Structural")
        @DisplayName("should throw if donation is not under analysis for approval")
        void shouldThrowIfDonationIsNotUnderAnalysisForApproval() {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(approvedDonation));

            assertThatThrownBy(() -> sut.approve(UUID.randomUUID(), updatedAt)).isInstanceOf(InvalidDonationAnalysisException.class);
        }

        @Test
        @DisplayName("should throw if donation is not under analysis for rejection")
        void shouldThrowIfDonationIsNotUnderAnalysisForRejection() {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(approvedDonation));

            assertThatThrownBy(() -> sut.reject(UUID.randomUUID(), updatedAt)).isInstanceOf(InvalidDonationAnalysisException.class);
        }
    }
}
