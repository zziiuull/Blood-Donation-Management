package br.ifsp.demo.domain.model.exam;

import br.ifsp.demo.application.service.dto.exam.ImmunohematologyExamDTO;
import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.donation.Donation;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
@Getter
public class ImmunohematologyExam extends Exam {
    private BloodType bloodType;
    private IrregularAntibodies irregularAntibodies;

    public ImmunohematologyExam(Donation donation) {
        super(donation, ExamStatus.UNDER_ANALYSIS);
    }

    public ImmunohematologyExam() {
        super();
    }

    @Override
    public boolean isFieldsValidForApproval() {
        if (bloodType == null) return false;
        return irregularAntibodies == IrregularAntibodies.NEGATIVE;
    }

    @Override
    public boolean isFieldsValidForRejection() {
        if (bloodType == null) return false;
        return irregularAntibodies == IrregularAntibodies.POSITIVE;
    }

    public void updateResults(ImmunohematologyExamDTO examDTO) {
        irregularAntibodies = examDTO.irregularAntibodies();
        bloodType = examDTO.bloodType();
    }
}
