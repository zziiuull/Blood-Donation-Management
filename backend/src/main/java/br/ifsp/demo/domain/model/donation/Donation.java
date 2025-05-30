package br.ifsp.demo.domain.model.donation;

import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.presentation.exception.CannotFinishDonationWithExamUnderAnalysisException;
import br.ifsp.demo.presentation.exception.InvalidDonationAnalysisException;
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

    public void verifyExamsToApprove(ImmunohematologyExam immunohematologyExam, SerologicalScreeningExam serologicalScreeningExam){
        if (immunohematologyExam.getStatus() == ExamStatus.UNDER_ANALYSIS)
            throw new CannotFinishDonationWithExamUnderAnalysisException("Cannot finish donation with immunohematology exam under analysis");
        if (serologicalScreeningExam.getStatus() == ExamStatus.UNDER_ANALYSIS)
            throw new CannotFinishDonationWithExamUnderAnalysisException("Cannot finish donation with serological screening exam under analysis");
        if (immunohematologyExam.getStatus() == ExamStatus.REJECTED)
            throw new InvalidDonationAnalysisException("Immunohematology exam doesn't have correct status for this donation analysis");
        if (serologicalScreeningExam.getStatus() == ExamStatus.REJECTED)
            throw new InvalidDonationAnalysisException("Serological screening exam doesn't have correct status for this donation analysis");
    }

    public void verifyExamsToReject(ImmunohematologyExam immunohematologyExam, SerologicalScreeningExam serologicalScreeningExam){
        if (immunohematologyExam.getStatus() == ExamStatus.UNDER_ANALYSIS)
            throw new CannotFinishDonationWithExamUnderAnalysisException("Cannot finish donation with immunohematology exam under analysis");
        if (serologicalScreeningExam.getStatus() == ExamStatus.UNDER_ANALYSIS)
            throw new CannotFinishDonationWithExamUnderAnalysisException("Cannot finish donation with serological screening exam under analysis");

        if (immunohematologyExam.getStatus() == ExamStatus.REJECTED) return;
        if (serologicalScreeningExam.getStatus() == ExamStatus.REJECTED) return;
        throw new InvalidDonationAnalysisException("Exams don't have correct status for this donation analysis");
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
