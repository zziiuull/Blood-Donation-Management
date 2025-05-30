package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.notifier.NotifierService;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.donation.DonationStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.infrastructure.repository.donation.DonationRepository;
import br.ifsp.demo.infrastructure.repository.exam.ExamRepository;
import br.ifsp.demo.presentation.exception.DonationNotFoundException;
import br.ifsp.demo.presentation.exception.ExamNotFoundException;
import br.ifsp.demo.presentation.exception.InvalidDonationAnalysisException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateDonationService {
    private final DonationRepository donationRepository;
    private final ExamRepository examRepository;
    private final NotifierService notifierService;

    public UpdateDonationService(DonationRepository donationRepository, ExamRepository examRepository, NotifierService notifierService) {
        this.donationRepository = donationRepository;
        this.examRepository = examRepository;
        this.notifierService = notifierService;
    }

    public Donation approve(UUID donationId, LocalDateTime updatedAt){
        Donation donation = retrieveDonationById(donationId);
        if (donation.getStatus() != DonationStatus.UNDER_ANALYSIS)
            throw new InvalidDonationAnalysisException("Donation status is not under analysis");

        ImmunohematologyExam immunohematologyExam = retrieveImmunohematologyExam(donationId);
        SerologicalScreeningExam serologicalScreeningExam = retrieveSerologicalScreeningExam(donationId);

        donation.verifyExamsToApprove(immunohematologyExam, serologicalScreeningExam);
        donation.approve(updatedAt);

        Donation saved = donationRepository.save(donation);

        notifierService.notify(donation.getDonor(), "Donation approved");

        return saved;
    }

    private Donation retrieveDonationById(UUID donationId) {
        Optional<Donation> optionalDonation = donationRepository.findById(donationId);
        if (optionalDonation.isEmpty()) throw new DonationNotFoundException("Donation not found");
        return optionalDonation.get();
    }

    private ImmunohematologyExam retrieveImmunohematologyExam(UUID donationId) {
        return examRepository.findAllByDonationId(donationId).stream()
                .filter(ImmunohematologyExam.class::isInstance)
                .map(ImmunohematologyExam.class::cast)
                .findFirst()
                .orElseThrow(() -> new ExamNotFoundException("Immunohematology exam not found"));
    }

    private SerologicalScreeningExam retrieveSerologicalScreeningExam(UUID donationId) {
        return examRepository.findAllByDonationId(donationId)
                .stream()
                .filter(SerologicalScreeningExam.class::isInstance)
                .map(SerologicalScreeningExam.class::cast)
                .findFirst()
                .orElseThrow(() -> new ExamNotFoundException("Serological screening exam not found"));
    }

    public Donation reject(UUID donationId, LocalDateTime updatedAt){
        Donation donation = retrieveDonationById(donationId);
        if (donation.getStatus() != DonationStatus.UNDER_ANALYSIS)
            throw new InvalidDonationAnalysisException("Donation status is not under analysis");

        ImmunohematologyExam immunohematologyExam = retrieveImmunohematologyExam(donationId);
        SerologicalScreeningExam serologicalScreeningExam = retrieveSerologicalScreeningExam(donationId);

        donation.verifyExamsToReject(immunohematologyExam, serologicalScreeningExam);
        donation.reject(updatedAt);

        Donation saved = donationRepository.save(donation);

        notifierService.notify(donation.getDonor(), "Donation rejected");

        return saved;
    }
}
