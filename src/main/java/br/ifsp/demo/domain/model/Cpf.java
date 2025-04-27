package br.ifsp.demo.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
public class Cpf {
    @Getter
    private String number;

    protected Cpf() {}

    public Cpf(String number) {
        this.number = number;
    }
}
