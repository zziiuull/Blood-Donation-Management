package br.ifsp.demo.presentation.controller.appointment;

import br.ifsp.demo.application.service.appointment.AppointmentService;
import br.ifsp.demo.application.service.donation.DonationRegisterService;
import br.ifsp.demo.application.service.dto.appointment.AppointmentDTO;
import br.ifsp.demo.domain.model.donation.Appointment;
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
@RequestMapping(path = "/api/v1/appointment")
@AllArgsConstructor
@Tag(name = "Appointment API")
public class AppointmentController {
    private final AppointmentService appointmentService;

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
                    description = "Appointments do not exist.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> view() {
        List<Appointment> appointments = appointmentService.getAll();
        return ResponseEntity.ok(appointments.stream().map(AppointmentDTO::new).toList());
    }
}
