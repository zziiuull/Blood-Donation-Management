package br.ifsp.demo.application.service.donation.dto;

import br.ifsp.demo.domain.model.ExamStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ExamDTO(
        UUID id,
        ExamStatus status,
        LocalDateTime performedAt
) {
}
