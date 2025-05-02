package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.notifier.NotifierService;
import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.DonationStatus;
import br.ifsp.demo.domain.model.exam.Exam;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.CannotFinishDonationWithExamUnderAnalysisException;
import br.ifsp.demo.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateDonationService {
    private DonationRepository donationRepository;
    private ExamRepository examRepository;
    private NotifierService notifierService;

    public UpdateDonationService() {
    }

    public UpdateDonationService(DonationRepository donationRepository, ExamRepository examRepository, NotifierService notifierService) {
        this.donationRepository = donationRepository;
        this.examRepository = examRepository;
        this.notifierService = notifierService;
    }

    public Donation approve(UUID donationId){
        Optional<Donation> optionalDonation = donationRepository.findById(donationId);
        if (optionalDonation.isEmpty()) throw new EntityNotFoundException("Donation not found");
        Donation donation = optionalDonation.get();

        isExamsAnalyzed(donationId);

        donation.setStatus(DonationStatus.APPROVED);
        donation.setUpdatedAt(LocalDateTime.now());

        Donation saved = donationRepository.save(donation);

        notifierService.notify(donation.getDonor(), "Donation approved");

        return saved;
    }

    private void isExamsAnalyzed(UUID donationId) {
        List<Exam> exams = examRepository.findAllByDonationId(donationId);
        ImmunohematologyExam immunohematologyExam = exams.stream()
                .filter(ImmunohematologyExam.class::isInstance)
                .map(ImmunohematologyExam.class::cast)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Immunohematology exam not found"));
        if (immunohematologyExam.getStatus() == ExamStatus.UNDER_ANALYSIS)
            throw new CannotFinishDonationWithExamUnderAnalysisException("Cannot finish donation with immunohematology exam under analysis");

        SerologicalScreeningExam serologicalScreeningExam = examRepository.findAllByDonationId(donationId)
                .stream()
                .filter(SerologicalScreeningExam.class::isInstance)
                .map(SerologicalScreeningExam.class::cast)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Serological screening exam not found"));
        if (serologicalScreeningExam.getStatus() == ExamStatus.UNDER_ANALYSIS)
            throw new CannotFinishDonationWithExamUnderAnalysisException("Cannot finish donation with serological screening exam under analysis");
    }

    public Donation reject(UUID donationId){
        Optional<Donation> optionalDonation = donationRepository.findById(donationId);
        if (optionalDonation.isEmpty()) throw new EntityNotFoundException("Donation not found");
        Donation donation = optionalDonation.get();

        isExamsAnalyzed(donationId);

        donation.setStatus(DonationStatus.REJECTED);
        donation.setUpdatedAt(LocalDateTime.now());

        Donation saved = donationRepository.save(donation);

        notifierService.notify(donation.getDonor(), "Donation rejected");

        return saved;
    }
}
