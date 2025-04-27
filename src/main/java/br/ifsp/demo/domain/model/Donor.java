package br.ifsp.demo.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Entity
public class Donor extends User {
    private LocalDate birthDate;
    private Double weight;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    protected Donor() {}

    public Donor(String name, Cpf cpf, ContactInfo contactInfo, LocalDate birthDate, Double weight, Sex sex, BloodType bloodType) {
        super(name, cpf, contactInfo);
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
