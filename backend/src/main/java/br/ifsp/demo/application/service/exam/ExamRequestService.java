package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.infrastructure.repository.exam.ExamRepository;
import br.ifsp.demo.presentation.exception.ExamRequestNotAllowedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamRequestService {
    private final ExamRepository examRepository;

    public ExamRequestService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public ImmunohematologyExam requestImmunohematologyExam(Donation donation){
        if (donation == null) throw new IllegalArgumentException("Donation must not be null");
        if (donation.getStatus().equals(DonationStatus.APPROVED)) throw new ExamRequestNotAllowedException("Cannot request an immunohematology exam for an approved donation");
        if (donation.getStatus().equals(DonationStatus.REJECTED)) throw new ExamRequestNotAllowedException("Cannot request an immunohematology exam for a rejected donation");

        List<ImmunohematologyExam> exams = examRepository.findAllByDonationId(donation.getId())
                .stream()
                .filter(ImmunohematologyExam.class::isInstance)
                .map(ImmunohematologyExam.class::cast).toList();

        if(!exams.isEmpty()){
            throw new ExamRequestNotAllowedException("An immunohematology exam already exists for this donation");
        }

        ImmunohematologyExam immunohematologyExam = new ImmunohematologyExam(donation);
        return examRepository.save(immunohematologyExam);
    }

    public SerologicalScreeningExam requestSerologicalScreeningExam(Donation donation) {
        if (donation == null) throw new IllegalArgumentException("Donation must not be null");
        if (donation.getStatus().equals(DonationStatus.APPROVED)) throw new ExamRequestNotAllowedException("Cannot request a serological screening exam for an approved donation");
        if (donation.getStatus().equals(DonationStatus.REJECTED)) throw new ExamRequestNotAllowedException("Cannot request a serological screening exam for a rejected donation");

        List<SerologicalScreeningExam> exams = examRepository.findAllByDonationId(donation.getId())
                .stream()
                .filter(SerologicalScreeningExam.class::isInstance)
                .map(SerologicalScreeningExam.class::cast).toList();

        if(!exams.isEmpty()){
            throw new ExamRequestNotAllowedException("A serological screening exam already exists for this donation");
        }

        SerologicalScreeningExam serologicalScreeningExam = new SerologicalScreeningExam(donation);
        return examRepository.save(serologicalScreeningExam);
    }
}
