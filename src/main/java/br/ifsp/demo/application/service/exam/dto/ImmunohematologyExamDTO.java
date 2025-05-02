package br.ifsp.demo.application.service.exam.dto;

import br.ifsp.demo.domain.model.BloodType;
import br.ifsp.demo.domain.model.exam.IrregularAntibodies;

import java.time.LocalDateTime;

public record ImmunohematologyExamDTO (
        BloodType bloodType,
        IrregularAntibodies irregularAntibodies,
        LocalDateTime updatedAt
) {
}
