package br.ifsp.demo.controller.exam;

import br.ifsp.demo.application.service.exam.ExamRequestService;
import br.ifsp.demo.controller.exam.response.ImmunohematologyExamResponse;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import br.ifsp.demo.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/exam")
@AllArgsConstructor
@Tag(name = "Exam controller")
public class ExamController {
    private final ExamRequestService examRequestService;
    private final DonationRepository donationRepository;

    @PostMapping("/{donationId}")
    public ResponseEntity<ImmunohematologyExamResponse> requestImmunohematologyExam(
            @PathVariable UUID donationId){
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new EntityNotFoundException("Donation not found"));

        ImmunohematologyExam immunohematologyExam = examRequestService.requestImmunohematologyExam(donation);

        return ResponseEntity.ok(new ImmunohematologyExamResponse(immunohematologyExam));
    }
}
