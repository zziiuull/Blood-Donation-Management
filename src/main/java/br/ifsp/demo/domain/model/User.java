package br.ifsp.demo.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Getter
    private String name;

    @Embedded
    @Getter
    private CPF cpf;

    @Embedded
    @Getter
    private ContactInfo contactInfo;

    protected User() {}

    protected User(String name, CPF cpf, ContactInfo contactInfo) {
        this.name = name;
        this.cpf = cpf;
        this.contactInfo = contactInfo;
    }
}
