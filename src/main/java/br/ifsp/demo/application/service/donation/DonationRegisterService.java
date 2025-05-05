package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.repository.appointment.AppointmentRepository;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import br.ifsp.demo.domain.repository.donor.DonorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DonationRegisterService {

    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;
    private final AppointmentRepository appointmentRepository;

    public DonationRegisterService(DonationRepository donationRepository, DonorRepository donorRepository, AppointmentRepository appointmentRepository) {
        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;
        this.appointmentRepository = appointmentRepository;
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
        if (donationRepository.existsByDonorAndAppointment(donor, appointment)) {
            throw new IllegalArgumentException("Donation already exists for this appointment");
        }

        Donation donation = new Donation(donor, appointment, DonationStatus.UNDER_ANALYSIS);
        return donationRepository.save(donation);
    }

    public Donation registerByDonorId(UUID donorId, UUID appointmentId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new IllegalArgumentException("Donor does not exist"));
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment does not exist"));
        return register(donor, appointment);
    }
}
