package br.ifsp.demo.domain.model.common;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Cpf {
    private String number;

    protected Cpf() {}

    public Cpf(String number) {
        if (number == null || !number.matches("\\d{11}"))
            throw new IllegalArgumentException("Invalid CPF format");
        this.number = number;
    }
}
