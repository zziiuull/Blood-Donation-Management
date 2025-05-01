package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.exam.Exam;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.ImmunohematologyExam;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamAlreadyAnalyzedException;
import br.ifsp.demo.exception.InvalidUpdatedTimeException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExamRegistrationService {
    private ExamRepository examRepository;

    public ExamRegistrationService() {
    }

    public ExamRegistrationService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public ImmunohematologyExam registerApprovedExam(ImmunohematologyExam immunohematologyExam, LocalDateTime updatedAt) {
        if (!isUnderAnalysis(immunohematologyExam)) throw new ExamAlreadyAnalyzedException("Can not approve exam already analyzed");

        if (!isInTheFuture(updatedAt)) throw new InvalidUpdatedTimeException("Updated time must be in the future");

        approveExam(immunohematologyExam, updatedAt);

        return examRepository.save(immunohematologyExam);
    }

    public SerologicalScreeningExam registerApprovedExam(SerologicalScreeningExam serologicalScreeningExam, LocalDateTime updatedAt) {
        if (!isInTheFuture(updatedAt)) throw new InvalidUpdatedTimeException("Updated time must be in the future");

        approveExam(serologicalScreeningExam, updatedAt);

        return examRepository.save(serologicalScreeningExam);
    }

    private boolean isUnderAnalysis(Exam exam){
        return exam.getStatus() == ExamStatus.UNDER_ANALYSIS;
    }

    private boolean isInTheFuture(LocalDateTime updatedAt) {
        return updatedAt != null && (updatedAt.isEqual(LocalDateTime.now()) || updatedAt.isAfter(LocalDateTime.now()));
    }

    private void approveExam(Exam exam, LocalDateTime updatedAt) {
        exam.setStatus(ExamStatus.APPROVED);
        exam.setUpdatedAt(updatedAt);
    }

    public ImmunohematologyExam registerRejectedExam(ImmunohematologyExam immunohematologyExam, LocalDateTime updatedAt) {
        if (!isUnderAnalysis(immunohematologyExam)) throw new ExamAlreadyAnalyzedException("Can not approve exam already analyzed");

        if (!isInTheFuture(updatedAt)) throw new InvalidUpdatedTimeException("Updated time must be in the future");

        rejectExam(immunohematologyExam, updatedAt);

        return examRepository.save(immunohematologyExam);
    }

    public SerologicalScreeningExam registerRejectedExam(SerologicalScreeningExam serologicalScreeningExam, LocalDateTime updatedAt) {
        if (!isUnderAnalysis(serologicalScreeningExam)) throw new ExamAlreadyAnalyzedException("Can not approve exam already analyzed");

        if (!isInTheFuture(updatedAt)) throw new InvalidUpdatedTimeException("Updated time must be in the future");

        rejectExam(serologicalScreeningExam, updatedAt);

        return examRepository.save(serologicalScreeningExam);
    }

    private void rejectExam(Exam exam, LocalDateTime updatedAt) {
        exam.setStatus(ExamStatus.REJECTED);
        exam.setUpdatedAt(updatedAt);
    }
}
