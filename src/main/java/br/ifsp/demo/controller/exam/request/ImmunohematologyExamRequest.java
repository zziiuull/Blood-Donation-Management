package br.ifsp.demo.controller.exam.request;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.exam.IrregularAntibodies;
import jakarta.validation.constraints.NotNull;

public record ImmunohematologyExamRequest(
        @NotNull(message = "Blood type field is required")
        BloodType bloodType,

        @NotNull(message = "Irregular antibodies field is required")
        IrregularAntibodies irregularAntibodies
) {
}
