package br.ifsp.demo.application.service.donation.dto;

import br.ifsp.demo.domain.model.DonationStatus;

import java.util.List;
import java.util.UUID;

public record DonationDetailsDTO(
        UUID id,
        DonationStatus status,
        List<ExamDTO> exams
) {
}
