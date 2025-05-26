package br.ifsp.demo.domain.model.donation;

import br.ifsp.demo.domain.model.donor.Donor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @Setter
    @Enumerated(EnumType.STRING)
    private DonationStatus status;

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

    public void approve(LocalDateTime updatedAt) {
        status = DonationStatus.APPROVED;
        this.updatedAt = updatedAt;
    }

    public void reject(LocalDateTime updatedAt){
        status = DonationStatus.REJECTED;
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
