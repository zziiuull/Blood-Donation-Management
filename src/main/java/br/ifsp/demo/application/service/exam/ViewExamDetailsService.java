package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ViewExamDetailsService {
    private ExamRepository examRepository;

    public ViewExamDetailsService() {
    }

    public ViewExamDetailsService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public ImmunohematologyExam viewImmunohematologyExam(UUID donationId){
        return examRepository.findAllByDonationId(donationId)
                .stream()
                .filter(ImmunohematologyExam.class::isInstance)
                .map(ImmunohematologyExam.class::cast)
                .findFirst()
                .orElseThrow(() -> new ExamNotFoundException("Immunohematology exam not found"));
    }

    public SerologicalScreeningExam viewSerologicalScreeningExam(UUID donationId) {
        return examRepository.findAllByDonationId(donationId)
                .stream()
                .filter(SerologicalScreeningExam.class::isInstance)
                .map(SerologicalScreeningExam.class::cast)
                .findFirst()
                .orElseThrow(() -> new ExamNotFoundException("Serological screening exam not found"));
    }
}
