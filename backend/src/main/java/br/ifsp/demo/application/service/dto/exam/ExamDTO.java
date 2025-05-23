package br.ifsp.demo.application.service.dto.exam;

import br.ifsp.demo.domain.model.exam.ExamStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ExamDTO(
        UUID id,
        ExamStatus status,
        LocalDateTime performedAt
) {
}
