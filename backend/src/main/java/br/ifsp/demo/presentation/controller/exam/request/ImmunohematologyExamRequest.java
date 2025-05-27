package br.ifsp.demo.presentation.controller.exam.request;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.exam.IrregularAntibodies;
import jakarta.validation.constraints.NotNull;

public record ImmunohematologyExamRequest(
        @NotNull(message = "bloodType field is required")
        BloodType bloodType,

        @NotNull(message = "irregularAntibodies field is required")
        IrregularAntibodies irregularAntibodies
) {
}
