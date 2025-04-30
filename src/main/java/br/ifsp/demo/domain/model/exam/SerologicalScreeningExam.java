package br.ifsp.demo.domain.model.exam;

import br.ifsp.demo.domain.model.Donation;
import jakarta.persistence.Entity;

@Entity
public class SerologicalScreeningExam extends Exam {
    public SerologicalScreeningExam(Donation donatoin) {
        super(donatoin, ExamStatus.UNDER_ANALYSIS);
    }

    public SerologicalScreeningExam() {
        super();
    }
}
