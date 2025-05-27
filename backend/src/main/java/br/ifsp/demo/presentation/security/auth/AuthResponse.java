package br.ifsp.demo.presentation.security.auth;

import br.ifsp.demo.presentation.security.user.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
        @Schema(
                description = "JTW credential",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRkZm9lQGV4YW1wbGUuY29tIiwiaWF0IjoxNzQxMDYzNzIzLCJleHAiOjE3NDEwNzIzNjN9.EYAEc92Bjf6brHRYGN3VJaGk9Nuy8mMb141cXxmfel4"
        )
        String token,

        @Schema(
                description = "User role",
                example = "PHYSICIAN"
        )
        Role role
){ }
