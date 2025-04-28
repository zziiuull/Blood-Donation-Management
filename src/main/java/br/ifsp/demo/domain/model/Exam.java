package br.ifsp.demo.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    private Donation donation;

    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    private LocalDateTime performedAt;

    protected Exam() {}

    public Exam(Donation donation, ExamStatus status, LocalDateTime performedAt) {
        if (donation == null) throw new IllegalArgumentException("Donation must not be null");
        if (status == null) throw new IllegalArgumentException("Status must not be null");
        if (performedAt == null) throw new IllegalArgumentException("PerformedAt must not be null");

        this.donation = donation;
        this.status = status;
        this.performedAt = performedAt;
    }
}
