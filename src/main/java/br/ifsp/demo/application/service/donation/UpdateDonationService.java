package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.notifier.NotifierService;
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
    private NotifierService notifierService;

    public UpdateDonationService() {
    }

    public UpdateDonationService(DonationRepository donationRepository, NotifierService notifierService) {
        this.donationRepository = donationRepository;
        this.notifierService = notifierService;
    }

    public Donation approve(UUID donationId){
        Optional<Donation> optionalDonation = donationRepository.findById(donationId);
        if (optionalDonation.isEmpty()) throw new EntityNotFoundException("Donation not found");
        Donation donation = optionalDonation.get();

        donation.setStatus(DonationStatus.APPROVED);
        donation.setUpdatedAt(LocalDateTime.now());

        Donation saved = donationRepository.save(donation);

        notifierService.notify(donation.getDonor(), "Donation approved");

        return saved;
    }

    public Donation reject(UUID donationId){
        Optional<Donation> optionalDonation = donationRepository.findById(donationId);
        if (optionalDonation.isEmpty()) throw new EntityNotFoundException("Donation not found");
        Donation donation = optionalDonation.get();

        donation.setStatus(DonationStatus.REJECTED);
        donation.setUpdatedAt(LocalDateTime.now());

        Donation saved = donationRepository.save(donation);

        notifierService.notify(donation.getDonor(), "Donation rejected");

        return saved;
    }
}
