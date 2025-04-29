package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import org.springframework.stereotype.Service;

@Service
public class ExamRequestService {
    private final ExamRepository examRepository;

    public ExamRequestService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public ImmunohematologyExam requestImmunohematologyExam(Donation donation){
        ImmunohematologyExam immunohematologyExam = new ImmunohematologyExam(donation);
        return examRepository.save(immunohematologyExam);
    }
}
