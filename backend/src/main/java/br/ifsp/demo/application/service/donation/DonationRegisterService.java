package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.AppointmentStatus;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.presentation.exception.AppointmentNotFoundException;
import br.ifsp.demo.presentation.exception.DonorNotFoundException;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import br.ifsp.demo.infrastructure.repository.donation.DonationRepository;
import br.ifsp.demo.infrastructure.repository.donor.DonorRepository;
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

    public Donation registerByDonorId(UUID donorId, UUID appointmentId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new DonorNotFoundException("Donor does not exist"));
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment does not exist"));
        if (!donor.isEligibleForDonation()) {
            throw new IllegalArgumentException("Donor is not eligible to donate");
        }
        if (donationRepository.existsByDonorAndAppointment(donor, appointment)) {
            throw new IllegalArgumentException("Donation already exists for this appointment");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        Donation donation = new Donation(donor, appointment, DonationStatus.UNDER_ANALYSIS);
        return donationRepository.save(donation);
    }
}
