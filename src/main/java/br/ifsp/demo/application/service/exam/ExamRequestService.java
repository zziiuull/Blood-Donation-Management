package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;

public class ExamRequestService {
    public ImmunohematologyExam requestImmunohematologyExam(Donation donation){
        return new ImmunohematologyExam(donation);
    }
}
