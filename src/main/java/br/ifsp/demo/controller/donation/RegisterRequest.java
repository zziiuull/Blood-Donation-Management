package br.ifsp.demo.controller.donation;

import java.util.UUID;

public record RegisterRequest (
        UUID donorId,
        UUID appointmentId
) {
}
