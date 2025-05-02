package br.ifsp.demo.controller.donation;

import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DonationResponse(UUID id, UUID donorId, DonationStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public DonationResponse(Donation donation) {
        this(donation.getId(), donation.getDonor().getId(), donation.getStatus(), donation.getCreatedAt(), donation.getUpdatedAt());
    }
}
