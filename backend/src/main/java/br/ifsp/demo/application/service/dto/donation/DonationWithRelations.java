package br.ifsp.demo.application.service.dto.donation;

import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public record DonationWithRelations(
        UUID id,
        Donor donor,
        Appointment appointment,
        DonationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        @Nullable ImmunohematologyExam immunohematologyExam,
        @Nullable SerologicalScreeningExam serologicalScreeningExam
) {
    public DonationWithRelations(
            Donation donation,
            ImmunohematologyExam immunohematologyExam,
            SerologicalScreeningExam serologicalScreeningExam){
        this(
                donation.getId(),
                donation.getDonor(),
                donation.getAppointment(),
                donation.getStatus(),
                donation.getCreatedAt(),
                donation.getUpdatedAt(),
                immunohematologyExam,
                serologicalScreeningExam
        );
    }
}
