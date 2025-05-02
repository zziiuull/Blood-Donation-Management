package br.ifsp.demo.application.service.donation;

import br.ifsp.demo.application.service.donation.dto.DonationDetailsDTO;
import br.ifsp.demo.application.service.donation.dto.ExamDTO;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.exam.Exam;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("Donation does not exist"));

        List<ExamDTO> exams = examRepository.findAllByDonationId(donationId).stream().map(e -> new ExamDTO(
                e.getId(),
                e.getStatus(),
                e.getCreatedAt()
        )).collect(Collectors.toList());

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
                exam.getCreatedAt()
        );
    }
}
