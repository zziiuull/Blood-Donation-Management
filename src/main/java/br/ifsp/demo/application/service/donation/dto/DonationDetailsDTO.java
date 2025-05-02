package br.ifsp.demo.application.service.donation.dto;

import br.ifsp.demo.domain.model.donation.DonationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DonationDetailsDTO(
        UUID id,
        DonationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ExamDTO> exams
) {
}
