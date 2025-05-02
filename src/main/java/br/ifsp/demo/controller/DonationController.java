package br.ifsp.demo.controller;

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

    @PostMapping
    public ResponseEntity<String> register() {
        final UUID userId = authService.getAuthenticatedUserId();
        return ResponseEntity.ok("register: " + userId.toString());
    }

    @GetMapping
    public ResponseEntity<String> view() {
        final UUID userId = authService.getAuthenticatedUserId();
        return ResponseEntity.ok("view: " + userId.toString());
    }

    @PutMapping
    public ResponseEntity<String> update() {
        final UUID userId = authService.getAuthenticatedUserId();
        return ResponseEntity.ok("update: " + userId.toString());
    }
}
