package br.ifsp.demo.domain.model.exam;

import br.ifsp.demo.domain.model.donation.Donation;
import br.ifsp.demo.presentation.exception.InvalidExamAnalysisException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    private Donation donation;

    @Enumerated(EnumType.STRING)
    @Setter
    private ExamStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Setter
    private String observations;

    protected Exam() {}

    protected Exam(Donation donation, ExamStatus status) {
        if (donation == null) throw new IllegalArgumentException("Donation must not be null");
        if (status == null) throw new IllegalArgumentException("Status must not be null");

        this.donation = donation;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void approve(LocalDateTime updatedAt) {
        if (!isFieldsValidForApproval())
            throw new InvalidExamAnalysisException("Exam has invalid field(s) for approval");
        this.status = ExamStatus.APPROVED;
        this.updatedAt = updatedAt;
    }

    public void reject(LocalDateTime updatedAt) {
        if (!isFieldsValidForRejection())
            throw new InvalidExamAnalysisException("Exam has invalid field(s) for rejection");
        this.status = ExamStatus.REJECTED;
        this.updatedAt = updatedAt;
    }

    abstract public boolean isFieldsValidForApproval();
    abstract public boolean isFieldsValidForRejection();
}
