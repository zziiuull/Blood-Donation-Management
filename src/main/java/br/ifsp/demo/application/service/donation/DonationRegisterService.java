package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.domain.model.Appointment;
import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.DonationStatus;
import br.ifsp.demo.domain.model.Donor;
import br.ifsp.demo.domain.repository.DonationRepository;
import org.springframework.stereotype.Service;

@Service
public class DonationRegisterService {

    private final DonationRepository donationRepository;

    public DonationRegisterService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Donation register(Donor donor, Appointment appointment) {
        if (donor == null) {
            throw new IllegalArgumentException("Donor must not be null");
        }
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment must not be null");
        }
        if (!donor.isEligibleForDonation()) {
            throw new IllegalArgumentException("Donor is not eligible to donate");
        }

        Donation donation = new Donation(donor, appointment, DonationStatus.EM_ANDAMENTO);
        return donationRepository.save(donation);
    }
}
