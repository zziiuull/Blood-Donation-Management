package br.ifsp.demo.domain.model.exam;

import br.ifsp.demo.domain.model.Donation;
import jakarta.persistence.Entity;

@Entity
public class ImmunohematologyExam extends Exam {
    public ImmunohematologyExam(Donation donatoin) {
        super(donatoin, ExamStatus.UNDER_ANALYSIS);
    }

    public ImmunohematologyExam() {
        super();
    }
}
