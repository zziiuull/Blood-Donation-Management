package br.ifsp.demo.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Getter
@Entity
public class Donor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Getter
    private String name;

    @Embedded
    @Getter
    private Cpf cpf;

    @Embedded
    @Getter
    private ContactInfo contactInfo;

    private LocalDate birthDate;
    private Double weight;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    protected Donor() {}

    public Donor(String name, Cpf cpf, ContactInfo contactInfo, LocalDate birthDate, Double weight, Sex sex, BloodType bloodType) {
        this.name = name;
        this.cpf = cpf;
        this.contactInfo = contactInfo;
        this.birthDate = birthDate;
        this.weight = weight;
        this.sex = sex;
        this.bloodType = bloodType;
    }

    public boolean isEligibleForDonation() {
        int age = Period.between(this.birthDate, LocalDate.now()).getYears();
        return age >= 19 && age <= 69 && weight >= 50.0;
    }
}
