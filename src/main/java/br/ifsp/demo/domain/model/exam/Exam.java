package br.ifsp.demo.domain.model.exam;

import br.ifsp.demo.domain.model.Donation;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
public abstract class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    private Donation donation;

    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Exam() {}

    public Exam(Donation donation, ExamStatus status) {
        if (donation == null) throw new IllegalArgumentException("Donation must not be null");
        if (status == null) throw new IllegalArgumentException("Status must not be null");

        this.donation = donation;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
