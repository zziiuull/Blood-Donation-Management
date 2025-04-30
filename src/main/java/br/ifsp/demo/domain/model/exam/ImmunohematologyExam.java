package br.ifsp.demo.domain.model.exam;

import br.ifsp.demo.domain.model.BloodType;
import br.ifsp.demo.domain.model.Donation;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class ImmunohematologyExam extends Exam {
    private BloodType bloodType;
    private IrregularAntibodies irregularAntibodies;

    public ImmunohematologyExam(Donation donatoin) {
        super(donatoin, ExamStatus.UNDER_ANALYSIS);
    }

    public ImmunohematologyExam() {
        super();
    }
}
