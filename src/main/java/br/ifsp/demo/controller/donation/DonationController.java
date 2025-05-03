package br.ifsp.demo.controller.donation;

import br.ifsp.demo.application.service.donation.DonationRegisterService;
import br.ifsp.demo.application.service.donation.UpdateDonationService;
import br.ifsp.demo.application.service.donation.ViewDonationDetailsService;
import br.ifsp.demo.application.service.donation.dto.DonationDetailsDTO;
import br.ifsp.demo.domain.model.donation.Donation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/donation")
@AllArgsConstructor
@Tag(name = "Donation API")
public class DonationController {
    private final DonationRegisterService donationRegisterService;
    private final ViewDonationDetailsService viewDonationDetailsService;
    private final UpdateDonationService updateDonationService;

    @PostMapping
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        Donation donation = donationRegisterService.registerByDonorId(request.donorId(), request.appointmentId());
        return ResponseEntity.ok(new RegisterResponse(donation));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonationDetailsDTO> view(@PathVariable UUID id) {
        DonationDetailsDTO donationDetails = viewDonationDetailsService.getDonationDetails(id);
        return ResponseEntity.ok(donationDetails);
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<DonationResponse> approve(@PathVariable UUID id) {
        Donation donation = updateDonationService.approve(id);
        return ResponseEntity.ok(new DonationResponse(donation));
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<DonationResponse> reject(@PathVariable UUID id) {
        Donation donation = updateDonationService.reject(id);
        return ResponseEntity.ok(new DonationResponse(donation));
    }
}
