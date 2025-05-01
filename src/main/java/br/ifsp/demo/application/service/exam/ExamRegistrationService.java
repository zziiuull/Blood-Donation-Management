package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExamRegistrationService {
    private ExamRepository examRepository;

    public ExamRegistrationService() {
    }

    public ExamRegistrationService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public ImmunohematologyExam register(ImmunohematologyExam immunohematologyExam, LocalDateTime updatedAt) {
        immunohematologyExam.setStatus(ExamStatus.APPROVED);
        immunohematologyExam.setUpdatedAt(updatedAt);

        return examRepository.save(immunohematologyExam);
    }

    public SerologicalScreeningExam register(SerologicalScreeningExam serologicalScreeningExam, LocalDateTime updatedAt) {
        serologicalScreeningExam.setStatus(ExamStatus.APPROVED);
        serologicalScreeningExam.setUpdatedAt(updatedAt);

        return examRepository.save(serologicalScreeningExam);
    }
}
