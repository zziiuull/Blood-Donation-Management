package br.ifsp.demo.presentation.security.auth;

public record RegisterPhysicianRequest(
        String name,
        String lastname,
        String email,
        String password,
        String cpf,
        String phone,
        String address,
        String crmNumber,
        String crmState
) {
}
