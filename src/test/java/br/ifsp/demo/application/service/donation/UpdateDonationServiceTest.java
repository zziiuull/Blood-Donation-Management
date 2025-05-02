package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.notifier.NotifierService;
import br.ifsp.demo.domain.model.Appointment;
import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.DonationStatus;
import br.ifsp.demo.domain.model.Donor;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    @BeforeEach
    void setUp() {
        Donor eligibleDonor = mock(Donor.class);
        Appointment appointment = mock(Appointment.class);
        donation = new Donation(
                eligibleDonor,
                appointment,
                DonationStatus.UNDER_ANALYSIS
        );
    }

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should approve donation")
        void shouldApproveDonation() {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(donation));
            when(donationRepository.save(any(Donation.class))).thenReturn(donation);

            Donation result = sut.approve(UUID.randomUUID());

            assertThat(result.getStatus()).isEqualTo(DonationStatus.APPROVED);
            assertThat(result.getUpdatedAt()).isNotNull();
            verify(donationRepository, times(1)).save(any(Donation.class));
            verify(notifierService, times(1)).notify(any(Donor.class), anyString());
        }

        @Test
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should reject donation")
        void shouldRejectDonation() {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(donation));
            when(donationRepository.save(any(Donation.class))).thenReturn(donation);

            Donation result = sut.reject(UUID.randomUUID());

            assertThat(result.getStatus()).isEqualTo(DonationStatus.REJECTED);
            assertThat(result.getUpdatedAt()).isNotNull();
            verify(donationRepository, times(1)).save(any(Donation.class));
            verify(notifierService, times(1)).notify(any(Donor.class), anyString());
        }
    }

    @Nested
    @DisplayName("For invalid tests")
    class InvalidTests {
        @ParameterizedTest
        @MethodSource("examsUnderAnalysis")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should throw when at least one exam is under analysis")
        void shouldThrowWhenAtLeastOneExamIsUnderAnalysis(ImmunohematologyExam immunohematologyExam, SerologicalScreeningExam serologicalScreeningExam) {
            when(donationRepository.findById(any(UUID.class))).thenReturn(Optional.of(donation));
            when(examRepository.findAllByDonationId(any(UUID.class))).thenReturn(List.of(immunohematologyExam, serologicalScreeningExam));

            assertThatThrownBy(() -> sut.approve(UUID.randomUUID())).isInstanceOf(IllegalStateException.class);
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
            SerologicalScreeningExam serologicalScreeningUnderAnalysis = new SerologicalScreeningExam(donation);
            SerologicalScreeningExam serologicalScreeningRejected = new SerologicalScreeningExam(donation);

            return Stream.of(
                    Arguments.of(immunohematologyUnderAnalysis, serologicalScreeningRejected),
                    Arguments.of(immunohematologyApproved, serologicalScreeningUnderAnalysis)
            );
        }
    }
}
