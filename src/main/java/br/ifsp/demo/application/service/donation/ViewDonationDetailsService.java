package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.donation.dto.DonationDetailsDTO;
import br.ifsp.demo.application.service.donation.dto.ExamDTO;
import br.ifsp.demo.domain.model.Donation;
import br.ifsp.demo.domain.model.Exam;
import br.ifsp.demo.domain.repository.DonationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ViewDonationDetailsService {

    private final DonationRepository donationRepository;

    public ViewDonationDetailsService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public DonationDetailsDTO getDonationDetails(UUID donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new IllegalArgumentException("Donation does not exist"));

        List<ExamDTO> exams = donation.getExams().stream()
                .map(this::toExamDTO)
                .collect(Collectors.toList());

        return new DonationDetailsDTO(
                donation.getId(),
                donation.getStatus(),
                exams
        );
    }

    private ExamDTO toExamDTO(Exam exam) {
        return new ExamDTO(
                exam.getId(),
                exam.getStatus(),
                exam.getPerformedAt()
        );
    }
}
