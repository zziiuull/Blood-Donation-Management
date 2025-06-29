package br.ifsp.demo.presentation.controller.exam;

import br.ifsp.demo.application.service.donation.ViewDonationDetailsService;
import br.ifsp.demo.application.service.exam.ExamRegistrationService;
import br.ifsp.demo.application.service.exam.ExamRequestService;
import br.ifsp.demo.application.service.exam.ViewExamDetailsService;
import br.ifsp.demo.application.service.dto.exam.ImmunohematologyExamDTO;
import br.ifsp.demo.application.service.dto.exam.SerologicalScreeningExamDTO;
import br.ifsp.demo.presentation.controller.exam.request.ImmunohematologyExamRequest;
import br.ifsp.demo.presentation.controller.exam.request.SerologicalScreeningExamRequest;
import br.ifsp.demo.presentation.controller.exam.response.ImmunohematologyExamResponse;
import br.ifsp.demo.presentation.controller.exam.response.SerologicalScreeningExamResponse;
import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/exam")
@AllArgsConstructor
@Tag(name = "Exam API")
public class ExamController {
    private final ExamRequestService examRequestService;
    private final ExamRegistrationService examRegistrationService;
    private final ViewExamDetailsService viewExamDetailsService;
    private final ViewDonationDetailsService viewDonationDetailsService;

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful operation.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImmunohematologyExamResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication fails.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Donation does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Exam already requested.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping("/request/immunohematology/{donationId}")
    public ResponseEntity<ImmunohematologyExamResponse> requestImmunohematologyExam(
            @PathVariable UUID donationId){
        Donation donation = viewDonationDetailsService.getDonation(donationId);

        ImmunohematologyExam immunohematologyExam = examRequestService.requestImmunohematologyExam(donation);

        return ResponseEntity.ok(new ImmunohematologyExamResponse(immunohematologyExam));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful operation.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SerologicalScreeningExamResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication fails.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Donation does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Exam already requested.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping("/request/serologicalscreening/{donationId}")
    public ResponseEntity<SerologicalScreeningExamResponse> requestSerologicalScreeningExam(
            @PathVariable UUID donationId){
        Donation donation = viewDonationDetailsService.getDonation(donationId);

        SerologicalScreeningExam serologicalScreeningExam = examRequestService.requestSerologicalScreeningExam(donation);

        return ResponseEntity.ok(new SerologicalScreeningExamResponse(serologicalScreeningExam));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful operation.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImmunohematologyExam.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Exam has invalid fields for approving.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication fails.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Donation does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Exam already analyzed.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping("/register/donation/{donationId}/immunohematology/approve/{examId}")
    public ResponseEntity<ImmunohematologyExamResponse> approveImmunohematologyExam(
            @PathVariable UUID donationId,
            @PathVariable UUID examId,
            @RequestBody @Valid ImmunohematologyExamRequest exam) {

        validateDonationExists(donationId);
        ImmunohematologyExam immunohematologyExam = examRegistrationService.registerApprovedExam(
                examId,
                ImmunohematologyExamDTO.fromRequest(exam),
                LocalDateTime.now());
        return ResponseEntity.ok(new ImmunohematologyExamResponse(immunohematologyExam));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful operation.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SerologicalScreeningExam.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Exam has invalid fields for approving.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication fails.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Donation does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Exam already analyzed.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping("/register/donation/{donationId}/serologicalscreening/approve/{examId}")
    public ResponseEntity<SerologicalScreeningExamResponse> approveSerologicalScreeningExam(
            @PathVariable UUID donationId,
            @PathVariable UUID examId,
            @RequestBody @Valid SerologicalScreeningExamRequest exam) {

        validateDonationExists(donationId);
        SerologicalScreeningExam serologicalScreeningExam = examRegistrationService.registerApprovedExam(
                examId,
                SerologicalScreeningExamDTO.fromRequest(exam),
                LocalDateTime.now());
        return ResponseEntity.ok(new SerologicalScreeningExamResponse(serologicalScreeningExam));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful operation.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImmunohematologyExam.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Exam has invalid fields for rejecting.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication fails.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Donation does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Exam already analyzed.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping("/register/donation/{donationId}/immunohematology/reject/{examId}")
    public ResponseEntity<ImmunohematologyExamResponse> rejectImmunohematologyExam(
            @PathVariable UUID donationId,
            @PathVariable UUID examId,
            @RequestBody @Valid ImmunohematologyExamRequest exam) {

        validateDonationExists(donationId);
        ImmunohematologyExam immunohematologyExam = examRegistrationService.registerRejectedExam(
                examId,
                ImmunohematologyExamDTO.fromRequest(exam),
                LocalDateTime.now());
        return ResponseEntity.ok(new ImmunohematologyExamResponse(immunohematologyExam));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful operation.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SerologicalScreeningExam.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Exam has invalid fields for rejecting.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication fails.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Donation does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Exam already analyzed.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping("/register/donation/{donationId}/serologicalscreening/reject/{examId}")
    public ResponseEntity<SerologicalScreeningExamResponse> rejectSerologicalScreeningExam(
            @PathVariable UUID donationId,
            @PathVariable UUID examId,
            @RequestBody @Valid SerologicalScreeningExamRequest exam) {

        validateDonationExists(donationId);
        SerologicalScreeningExam serologicalScreeningExam = examRegistrationService.registerRejectedExam(
                examId,
                SerologicalScreeningExamDTO.fromRequest(exam),
                LocalDateTime.now());
        return ResponseEntity.ok(new SerologicalScreeningExamResponse(serologicalScreeningExam));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful operation.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImmunohematologyExamResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication fails.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Donation does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping("/view/immunohematology/{donationId}")
    public ResponseEntity<ImmunohematologyExamResponse> viewImmunohematologyExam(@PathVariable UUID donationId) {
        validateDonationExists(donationId);
        ImmunohematologyExam immunohematologyExam = viewExamDetailsService.viewImmunohematologyExam(donationId);
        return ResponseEntity.ok(new ImmunohematologyExamResponse(immunohematologyExam));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful operation.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SerologicalScreeningExamResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication fails.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Donation does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping("/view/serologicalscreening/{donationId}")
    public ResponseEntity<SerologicalScreeningExamResponse> viewSerologicalScreeningExam(@PathVariable UUID donationId) {
        validateDonationExists(donationId);
        SerologicalScreeningExam serologicalScreeningExam = viewExamDetailsService.viewSerologicalScreeningExam(donationId);
        return ResponseEntity.ok(new SerologicalScreeningExamResponse(serologicalScreeningExam));
    }

    private void validateDonationExists(UUID donationId) {
        viewDonationDetailsService.getDonation(donationId);
    }
}
