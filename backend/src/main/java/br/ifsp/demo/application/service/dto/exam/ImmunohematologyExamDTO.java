package br.ifsp.demo.application.service.dto.exam;

import br.ifsp.demo.presentation.controller.exam.request.ImmunohematologyExamRequest;
import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.exam.IrregularAntibodies;

public record ImmunohematologyExamDTO (
        BloodType bloodType,
        IrregularAntibodies irregularAntibodies
) {

    public static ImmunohematologyExamDTO fromRequest(ImmunohematologyExamRequest exam){
        return new ImmunohematologyExamDTO(
                exam.bloodType(),
                exam.irregularAntibodies()
        );
    }
}
