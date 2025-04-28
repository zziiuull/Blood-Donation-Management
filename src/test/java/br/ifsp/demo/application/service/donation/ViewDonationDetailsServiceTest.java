package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.domain.model.Appointment;
import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.DonationStatus;
import br.ifsp.demo.domain.model.Donor;
import br.ifsp.demo.domain.repository.DonationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewDonationDetailsServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @InjectMocks
    private ViewDonationDetailsService viewDonationDetailsService;

    @Test
    @Tag("TDD")
    @Tag("UnitTest")
    @DisplayName("Should return donation details when donation exists")
    void shouldReturnDonationDetailsWhenDonationExists() {

        UUID donationId = UUID.randomUUID();

        Donor donor = mock(Donor.class);
        Appointment appointment = mock(Appointment.class);

        Donation donation = new Donation(donor, appointment, DonationStatus.EM_ANDAMENTO);

        when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));

        Donation result = viewDonationDetailsService.getDonationDetails(donationId);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(DonationStatus.EM_ANDAMENTO);

        verify(donationRepository, times(1)).findById(donationId);
    }

    @Test
    @Tag("TDD")
    @Tag("UnitTest")
    @DisplayName("Should throw exception when donation does not exist")
    void shouldThrowExceptionWhenDonationDoesNotExist() {
        UUID donationId = UUID.randomUUID();
        when(donationRepository.findById(donationId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> viewDonationDetailsService.getDonationDetails(donationId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Donation does not exist");

        verify(donationRepository, times(1)).findById(donationId);
    }
}