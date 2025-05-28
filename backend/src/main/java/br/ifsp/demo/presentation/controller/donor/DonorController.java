package br.ifsp.demo.presentation.controller.donor;

import br.ifsp.demo.application.service.donation.DonationRegisterService;
import br.ifsp.demo.application.service.donor.DonorService;
import br.ifsp.demo.application.service.dto.donor.DonorDTO;
import br.ifsp.demo.domain.model.donor.Donor;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/donor")
@AllArgsConstructor
@Tag(name = "Donor API")
public class DonorController {
    private final DonorService donorService;

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
                    description = "Donors do not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping
    public ResponseEntity<List<DonorDTO>> view() {
        List<Donor> donors = donorService.getAll();
        return ResponseEntity.ok(donors.stream().map(DonorDTO::new).toList());
    }
}
