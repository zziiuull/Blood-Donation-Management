package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.DonationRepository;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
                .stream().filter(exam -> exam instanceof ImmunohematologyExam)
                .map(exam -> (ImmunohematologyExam) exam)
                .findFirst()
                .orElseThrow(() -> new ExamNotFoundException("Immunohematology exam not found"));
    }

    public SerologicalScreeningExam viewSerologicalScreeningExam(UUID donationId) {
        return examRepository.findAllByDonationId(donationId)
                .stream().filter(exam -> exam instanceof SerologicalScreeningExam)
                .map(exam -> (SerologicalScreeningExam) exam)
                .findFirst()
                .orElseThrow(() -> new ExamNotFoundException("Serological screening exam not found"));
    }
}
