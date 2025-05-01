package br.ifsp.demo.domain.model.exam;

import br.ifsp.demo.domain.model.BloodType;
import br.ifsp.demo.domain.model.Donation;
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
}
