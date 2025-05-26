package br.ifsp.demo.presentation.controller.donation;

import br.ifsp.demo.application.service.donation.DonationRegisterService;
import br.ifsp.demo.application.service.donation.UpdateDonationService;
import br.ifsp.demo.application.service.donation.ViewDonationDetailsService;
import br.ifsp.demo.application.service.dto.donation.DonationDetailsDTO;
import br.ifsp.demo.domain.model.donation.Donation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/donation")
@AllArgsConstructor
@Tag(name = "Donation API")
public class DonationController {
    private final DonationRegisterService donationRegisterService;
    private final ViewDonationDetailsService viewDonationDetailsService;
    private final UpdateDonationService updateDonationService;

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful operation.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DonationRegisterService.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Donor is not eligible to donate.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Donation already exists for this appointment.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication fails.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Donor does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment does not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        Donation donation = donationRegisterService.registerByDonorId(request.donorId(), request.appointmentId());
        return ResponseEntity.ok(new RegisterResponse(donation));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful operation.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DonationRegisterService.class)
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
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DonationDetailsDTO> view(@PathVariable UUID id) {
        DonationDetailsDTO donationDetails = viewDonationDetailsService.getDonationDetails(id);
        return ResponseEntity.ok(donationDetails);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful operation.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DonationRegisterService.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cannot finish donation with exam(s) under analysis.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Exam doesn't have correct status for this donation analysis.",
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
            )
    })
    @PutMapping("/approve/{id}")
    public ResponseEntity<DonationResponse> approve(@PathVariable UUID id) {
        Donation donation = updateDonationService.approve(id, LocalDateTime.now());
        return ResponseEntity.ok(new DonationResponse(donation));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful operation.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DonationRegisterService.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cannot finish donation with exam(s) under analysis.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Exam doesn't have correct status for this donation analysis.",
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
            )
    })
    @PutMapping("/reject/{id}")
    public ResponseEntity<DonationResponse> reject(@PathVariable UUID id) {
        Donation donation = updateDonationService.reject(id, LocalDateTime.now());
        return ResponseEntity.ok(new DonationResponse(donation));
    }
}
