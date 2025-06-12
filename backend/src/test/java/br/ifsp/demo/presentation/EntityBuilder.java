package br.ifsp.demo.presentation;

import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.physician.Crm;
import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.domain.model.physician.State;
import br.ifsp.demo.presentation.security.user.Role;
import com.github.javafaker.Faker;

import java.util.UUID;

public class EntityBuilder {
    private static final Faker faker = Faker.instance();

    public static Physician createRandomPhysician(String password){
        String cpf = faker.number().digits(11);
        String crmValue = faker.number().digits(7);
        State state = State.values()[faker.random().nextInt(State.values().length)];

        return new Physician (
                UUID.randomUUID(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                password,
                Role.PHYSICIAN,
                new Cpf(cpf),
                new Crm(crmValue, state)
        );
    }
}
