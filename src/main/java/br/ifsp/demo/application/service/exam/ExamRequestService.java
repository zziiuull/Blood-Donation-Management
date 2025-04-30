package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.DonationStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamRequestNotAllowedException;
import org.springframework.stereotype.Service;

@Service
public class ExamRequestService {
    private final ExamRepository examRepository;

    public ExamRequestService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public ImmunohematologyExam requestImmunohematologyExam(Donation donation){
        if (donation.getStatus().equals(DonationStatus.APROVADO)) throw new ExamRequestNotAllowedException("Cannot request an immunohematology exam for an approved donation");
        ImmunohematologyExam immunohematologyExam = new ImmunohematologyExam(donation);
        return examRepository.save(immunohematologyExam);
    }

    public SerologicalScreeningExam requestSerologicalScreeningExam(Donation donation) {
        if (donation.getStatus().equals(DonationStatus.APROVADO)) throw new ExamRequestNotAllowedException("Cannot request a serological screening exam for an approved donation");
        SerologicalScreeningExam serologicalScreeningExam = new SerologicalScreeningExam(donation);
        return examRepository.save(serologicalScreeningExam);
    }
}
