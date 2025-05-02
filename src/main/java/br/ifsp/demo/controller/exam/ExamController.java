package br.ifsp.demo.controller.exam;
import br.ifsp.demo.application.service.exam.ExamRegistrationService;
import br.ifsp.demo.application.service.exam.ExamRequestService;
import br.ifsp.demo.application.service.exam.dto.ImmunohematologyExamDTO;
import br.ifsp.demo.application.service.exam.dto.SerologicalScreeningExamDTO;
import br.ifsp.demo.controller.exam.request.ImmunohematologyExamRequest;
import br.ifsp.demo.controller.exam.request.SerologicalScreeningExamRequest;
import br.ifsp.demo.controller.exam.response.ImmunohematologyExamResponse;
import br.ifsp.demo.controller.exam.response.SerologicalScreeningExamResponse;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.donation.DonationRepository;
import br.ifsp.demo.exception.EntityNotFoundException;
import br.ifsp.demo.security.auth.AuthenticationInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/exam")
@AllArgsConstructor
@Tag(name = "Exam controller")
public class ExamController {
    private final ExamRequestService examRequestService;
    private final ExamRegistrationService examRegistrationService;
    private final DonationRepository donationRepository;

    @PostMapping("request/immunohematology/{donationId}")
    public ResponseEntity<ImmunohematologyExamResponse> requestImmunohematologyExam(
            @PathVariable UUID donationId){
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new EntityNotFoundException("Donation not found"));

        ImmunohematologyExam immunohematologyExam = examRequestService.requestImmunohematologyExam(donation);

        return ResponseEntity.ok(new ImmunohematologyExamResponse(immunohematologyExam));
    }


    @PostMapping("request/serologicalscreening/{donationId}")
    public ResponseEntity<SerologicalScreeningExamResponse> requestSerologicalScreeningExam(
            @PathVariable UUID donationId){
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new EntityNotFoundException("Donation not found"));

        SerologicalScreeningExam serologicalScreeningExam = examRequestService.requestSerologicalScreeningExam(donation);

        return ResponseEntity.ok(new SerologicalScreeningExamResponse(serologicalScreeningExam));
    }

    @PostMapping("/register/immunohematology/approve/{examId}")
    public ResponseEntity<ImmunohematologyExam> approveImmunohematologyExam(
            @PathVariable UUID examId,
            @RequestBody @Valid ImmunohematologyExamRequest exam) {
        return ResponseEntity.ok(examRegistrationService.registerApprovedExam(
                examId,
                ImmunohematologyExamDTO.fromRequest(exam)));
    }

    @PostMapping("/register/serologicalscreening/approve/{examId}")
    public ResponseEntity<SerologicalScreeningExam> approveSerologicalScreeningExam(
            @PathVariable UUID examId,
            @RequestBody @Valid SerologicalScreeningExamRequest examDTO) {
        return ResponseEntity.ok(examRegistrationService.registerApprovedExam(
                examId,
                SerologicalScreeningExamDTO.fromRequest(examDTO)));
    }

}
