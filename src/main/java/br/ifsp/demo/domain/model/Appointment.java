package br.ifsp.demo.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Getter
    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    @Getter
    private AppointmentStatus status;

    @ManyToOne(optional = false)
    @Getter
    private CollectionSite collectionSite;

    @Getter
    private String notes;

    protected Appointment() {}

    public Appointment(LocalDateTime appointmentDate, AppointmentStatus status, CollectionSite collectionSite, String notes) {
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.collectionSite = collectionSite;
        this.notes = notes;
    }
}
