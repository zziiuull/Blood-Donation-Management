package br.ifsp.demo.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    @Getter
    private Donor donor;

    @OneToOne(optional = false)
    @Getter
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    @Getter
    private DonationStatus status;

    protected Donation() {}

    public Donation(Donor donor, Appointment appointment, DonationStatus status) {
        this.donor = donor;
        this.appointment = appointment;
        this.status = status;
    }
}
