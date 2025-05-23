package br.ifsp.demo.domain.model.physician;

import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.security.user.Role;
import br.ifsp.demo.security.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Objects;
import java.util.UUID;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("PHYSICIAN")
public class Physician extends User {
    @Embedded
    Cpf cpf;
    @Embedded
    Crm crm;

    public Physician(@NonNull UUID id, @NonNull String name, @NonNull String lastname, @NonNull String email, @NonNull String password, Role role, Cpf cpf, Crm crm) {
        super(id, name, lastname, email, password, role);
        this.cpf = cpf;
        this.crm = crm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Physician physician)) return false;
        return getId().equals(physician.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
