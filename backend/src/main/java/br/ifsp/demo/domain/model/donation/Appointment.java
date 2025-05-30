package br.ifsp.demo.domain.model.donation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Setter
    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    @Setter
    private AppointmentStatus status;

    @ManyToOne(optional = false)
    private CollectionSite collectionSite;

    private String notes;

    protected Appointment() {}

    public Appointment(LocalDateTime appointmentDate, AppointmentStatus status, CollectionSite collectionSite, String notes) {
        if (appointmentDate == null || appointmentDate.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Appointment date must be in the future");
        if (collectionSite == null)
            throw new IllegalArgumentException("Collection site must not be null");

        this.appointmentDate = appointmentDate;
        this.status = status;
        this.collectionSite = collectionSite;
        this.notes = notes;
    }
}
