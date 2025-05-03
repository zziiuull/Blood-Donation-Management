package br.ifsp.demo.security.auth;

public record RegisterMedicRequest(
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
