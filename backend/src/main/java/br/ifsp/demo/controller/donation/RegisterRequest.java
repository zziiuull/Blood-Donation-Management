package br.ifsp.demo.controller.donation;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegisterRequest (
        @NotNull(message = "donorId field is required")
        UUID donorId,
        @NotNull(message = "appointmentId field is required")
        UUID appointmentId
) {
}
