package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.DonationStatus;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import br.ifsp.demo.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateDonationService {
    private DonationRepository donationRepository;

    public UpdateDonationService() {
    }

    public UpdateDonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Donation approve(UUID donationId){
        Optional<Donation> optionalDonation = donationRepository.findById(donationId);
        if (optionalDonation.isEmpty()) throw new EntityNotFoundException("Donation not found");
        Donation donation = optionalDonation.get();

        donation.setStatus(DonationStatus.APPROVED);
        donation.setUpdatedAt(LocalDateTime.now());
        
        return donationRepository.save(donation);
    }

    public Donation reject(UUID donationId){
        Optional<Donation> optionalDonation = donationRepository.findById(donationId);
        if (optionalDonation.isEmpty()) throw new EntityNotFoundException("Donation not found");
        Donation donation = optionalDonation.get();

        donation.setStatus(DonationStatus.REJECTED);
        donation.setUpdatedAt(LocalDateTime.now());


        return donationRepository.save(donation);
    }
}
