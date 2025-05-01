package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.domain.model.Appointment;
import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.DonationStatus;
import br.ifsp.demo.domain.model.Donor;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import br.ifsp.demo.domain.repository.donor.DonorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DonationRegisterService {

    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;

    public DonationRegisterService(DonationRepository donationRepository, DonorRepository donorRepository) {
        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;
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

    public Donation registerByDonorId(UUID donorId, Appointment appointment) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new IllegalArgumentException("Donor does not exist"));
        return register(donor, appointment);
    }
}
