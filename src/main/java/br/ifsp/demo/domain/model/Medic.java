package br.ifsp.demo.domain.model;

import br.ifsp.demo.security.user.Role;
import br.ifsp.demo.security.user.User;
import lombok.NonNull;

import java.util.UUID;

public class Medic extends User {
    Cpf cpf;
    ContactInfo contactInfo;
    Crm crm;

    public Medic(@NonNull UUID id, @NonNull String name, @NonNull String lastname, @NonNull String email, @NonNull String password, Role role, Cpf cpf, ContactInfo contactInfo, Crm crm) {
        super(id, name, lastname, email, password, role);
        this.cpf = cpf;
        this.contactInfo = contactInfo;
        this.crm = crm;
    }
}
