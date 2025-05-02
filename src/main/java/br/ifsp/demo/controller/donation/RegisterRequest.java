package br.ifsp.demo.controller.donation;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterRequest (
        UUID donorId
) {
}
