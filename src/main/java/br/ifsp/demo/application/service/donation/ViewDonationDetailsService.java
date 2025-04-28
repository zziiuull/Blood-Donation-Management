package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.repository.DonationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ViewDonationDetailsService {

    private final DonationRepository donationRepository;

    public ViewDonationDetailsService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Donation getDonationDetails(UUID donationId) {
        return donationRepository.findById(donationId)
                .orElseThrow(() -> new IllegalArgumentException("Donation does not exist"));
    }
}
