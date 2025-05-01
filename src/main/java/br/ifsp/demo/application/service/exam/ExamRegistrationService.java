package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.domain.model.exam.*;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamAlreadyAnalyzedException;
import br.ifsp.demo.exception.InvalidExamAnalysisException;
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

        if (!isFieldsValidForApproving(immunohematologyExam)) throw new InvalidExamAnalysisException("Immunohematology exam has invalid field(s) for approving");

        approveExam(immunohematologyExam, updatedAt);

        return examRepository.save(immunohematologyExam);
    }

    private boolean isUnderAnalysis(Exam exam){
        return exam.getStatus() == ExamStatus.UNDER_ANALYSIS;
    }

    private boolean isInTheFuture(LocalDateTime updatedAt) {
        return updatedAt != null && (updatedAt.isEqual(LocalDateTime.now()) || updatedAt.isAfter(LocalDateTime.now()));
    }

    private boolean isFieldsValidForApproving(ImmunohematologyExam exam){
        return exam.getIrregularAntibodies() == IrregularAntibodies.NEGATIVE;
    }

    private void approveExam(Exam exam, LocalDateTime updatedAt) {
        exam.setStatus(ExamStatus.APPROVED);
        exam.setUpdatedAt(updatedAt);
    }

    public SerologicalScreeningExam registerApprovedExam(SerologicalScreeningExam serologicalScreeningExam, LocalDateTime updatedAt) {
        if (!isUnderAnalysis(serologicalScreeningExam)) throw new ExamAlreadyAnalyzedException("Can not approve exam already analyzed");

        if (!isInTheFuture(updatedAt)) throw new InvalidUpdatedTimeException("Updated time must be in the future");

        if (!isFieldsValidForApproving(serologicalScreeningExam)) throw new InvalidExamAnalysisException("Serological screening exam has invalid field(s) for approving");

        approveExam(serologicalScreeningExam, updatedAt);

        return examRepository.save(serologicalScreeningExam);
    }

    private boolean isFieldsValidForApproving(SerologicalScreeningExam exam){
        if (exam.getHepatitisB() != null && exam.getHepatitisB() == DiseaseDetection.POSITIVE) return false;
        if (exam.getHepatitisC() != null && exam.getHepatitisC() == DiseaseDetection.POSITIVE) return false;
        if (exam.getChagasDisease() != null && exam.getChagasDisease() == DiseaseDetection.POSITIVE) return false;
        if (exam.getSyphilis() != null && exam.getSyphilis() == DiseaseDetection.POSITIVE) return false;
        if (exam.getAids() != null && exam.getAids() == DiseaseDetection.POSITIVE) return false;
        return exam.getHtlv1_2() != null && exam.getHtlv1_2() != DiseaseDetection.POSITIVE;
    }

    public ImmunohematologyExam registerRejectedExam(ImmunohematologyExam immunohematologyExam, LocalDateTime updatedAt) {
        if (!isUnderAnalysis(immunohematologyExam)) throw new ExamAlreadyAnalyzedException("Can not approve exam already analyzed");

        if (!isInTheFuture(updatedAt)) throw new InvalidUpdatedTimeException("Updated time must be in the future");

        if (!isFieldsValidForRejecting(immunohematologyExam)) throw new InvalidExamAnalysisException("Immunohematology exam has invalid field(s) for rejecting");

        rejectExam(immunohematologyExam, updatedAt);

        return examRepository.save(immunohematologyExam);
    }

    private boolean isFieldsValidForRejecting(ImmunohematologyExam exam){
        return exam.getIrregularAntibodies() == IrregularAntibodies.POSITIVE;
    }

    private void rejectExam(Exam exam, LocalDateTime updatedAt) {
        exam.setStatus(ExamStatus.REJECTED);
        exam.setUpdatedAt(updatedAt);
    }

    public SerologicalScreeningExam registerRejectedExam(SerologicalScreeningExam serologicalScreeningExam, LocalDateTime updatedAt) {
        if (!isUnderAnalysis(serologicalScreeningExam)) throw new ExamAlreadyAnalyzedException("Can not approve exam already analyzed");

        if (!isInTheFuture(updatedAt)) throw new InvalidUpdatedTimeException("Updated time must be in the future");

        if (!isFieldsValidForRejecting(serologicalScreeningExam)) throw new InvalidExamAnalysisException("Serological screening exam has invalid field(s) for rejecting");

        rejectExam(serologicalScreeningExam, updatedAt);

        return examRepository.save(serologicalScreeningExam);
    }

    private boolean isFieldsValidForRejecting(SerologicalScreeningExam exam){
        if (exam.getHepatitisB() != null && exam.getHepatitisB() == DiseaseDetection.POSITIVE) return true;
        if (exam.getHepatitisC() != null && exam.getHepatitisC() == DiseaseDetection.POSITIVE) return true;
        if (exam.getChagasDisease() != null && exam.getChagasDisease() == DiseaseDetection.POSITIVE) return true;
        if (exam.getSyphilis() != null && exam.getSyphilis() == DiseaseDetection.POSITIVE) return true;
        if (exam.getAids() != null && exam.getAids() == DiseaseDetection.POSITIVE) return true;
        return exam.getHtlv1_2() != null && exam.getHtlv1_2() == DiseaseDetection.POSITIVE;
    }
}
