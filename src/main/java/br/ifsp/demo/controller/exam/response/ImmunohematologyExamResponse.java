package br.ifsp.demo.controller.exam.response;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.IrregularAntibodies;

import java.time.LocalDateTime;
import java.util.UUID;

public record ImmunohematologyExamResponse(
        UUID id,
        UUID donationId,
        ExamStatus examStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String observations,
        BloodType bloodType,
        IrregularAntibodies irregularAntibodies
) {
    public ImmunohematologyExamResponse(ImmunohematologyExam exam){
        this(
                exam.getId(),
                exam.getDonation().getId(),
                exam.getStatus(),
                exam.getCreatedAt(),
                exam.getUpdatedAt(),
                exam.getObservations(),
                exam.getBloodType(),
                exam.getIrregularAntibodies()
        );
    }
}
