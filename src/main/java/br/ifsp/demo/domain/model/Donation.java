package br.ifsp.demo.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    private Donor donor;

    @OneToOne(optional = false)
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Exam> exams = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Donation() {}

    public Donation(Donor donor, Appointment appointment, DonationStatus status) {
        if (donor == null) throw new IllegalArgumentException("Donor must not be null");
        if (appointment == null) throw new IllegalArgumentException("Appointment must not be null");
        if (status == null) throw new IllegalArgumentException("Status must not be null");

        this.donor = donor;
        this.appointment = appointment;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
