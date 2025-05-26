package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.dto.donation.DonationDetailsDTO;
import br.ifsp.demo.application.service.dto.exam.ExamDTO;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.infrastructure.repository.donation.DonationRepository;
import br.ifsp.demo.infrastructure.repository.exam.ExamRepository;
import br.ifsp.demo.presentation.exception.DonationNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ViewDonationDetailsService {

    private final DonationRepository donationRepository;
    private final ExamRepository examRepository;

    public ViewDonationDetailsService(DonationRepository donationRepository, ExamRepository examRepository) {
        this.donationRepository = donationRepository;
        this.examRepository = examRepository;
    }

    public DonationDetailsDTO getDonationDetails(UUID donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new DonationNotFoundException("Donation does not exist"));

        List<ExamDTO> exams = examRepository.findAllByDonationId(donationId).stream().map(e -> new ExamDTO(
                e.getId(),
                e.getStatus(),
                e.getCreatedAt()
        )).collect(Collectors.toList());

        return new DonationDetailsDTO(
                donation.getId(),
                donation.getStatus(),
                donation.getCreatedAt(),
                donation.getUpdatedAt(),
                exams
        );
    }
}
