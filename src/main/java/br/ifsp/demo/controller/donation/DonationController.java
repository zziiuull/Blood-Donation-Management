package br.ifsp.demo.controller.donation;

import br.ifsp.demo.application.service.donation.DonationRegisterService;
import br.ifsp.demo.application.service.donation.UpdateDonationService;
import br.ifsp.demo.application.service.donation.ViewDonationDetailsService;
import br.ifsp.demo.application.service.donation.dto.DonationDetailsDTO;
import br.ifsp.demo.security.auth.AuthenticationInfoService;
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
    private final AuthenticationInfoService authService;
    private final DonationRegisterService donationRegisterService;
    private final ViewDonationDetailsService viewDonationDetailsService;
    private final UpdateDonationService updateDonationService;

    @PostMapping
    public ResponseEntity<String> register() {
        final UUID userId = authService.getAuthenticatedUserId();
        return ResponseEntity.ok("register: " + userId.toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonationDetailsDTO> view(@PathVariable UUID id) {
        DonationDetailsDTO donationDetails = viewDonationDetailsService.getDonationDetails(id);
        return ResponseEntity.ok(donationDetails);
    }

    @PutMapping
    public ResponseEntity<String> update() {
        final UUID userId = authService.getAuthenticatedUserId();
        return ResponseEntity.ok("update: " + userId.toString());
    }
}
