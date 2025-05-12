package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.application.service.exam.dto.ImmunohematologyExamDTO;
import br.ifsp.demo.application.service.exam.dto.SerologicalScreeningExamDTO;
import br.ifsp.demo.domain.model.exam.*;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamAlreadyAnalyzedException;
import br.ifsp.demo.exception.ExamNotFoundException;
import br.ifsp.demo.exception.InvalidExamAnalysisException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExamRegistrationService {
    private final ExamRepository examRepository;

    public ExamRegistrationService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public ImmunohematologyExam registerApprovedExam(UUID examId, ImmunohematologyExamDTO examDTO) {
        ImmunohematologyExam exam = retrieveImmunohematologyExam(examId);

        isUnderAnalysis(exam);

        if (!isFieldsValidForApproving(examDTO)) throw new InvalidExamAnalysisException("Immunohematology exam has invalid field(s) for approving");

        editExamData(exam, examDTO);
        approveExam(exam);

        return examRepository.save(exam);
    }

    private ImmunohematologyExam retrieveImmunohematologyExam(UUID examId){
        Optional<Exam> optionalExam = examRepository.findById(examId);
        if (optionalExam.isEmpty()) throw new ExamNotFoundException("Exam not found");
        return (ImmunohematologyExam) optionalExam.get();
    }

    private void isUnderAnalysis(Exam exam){
        if (exam.getStatus() != ExamStatus.UNDER_ANALYSIS) throw new ExamAlreadyAnalyzedException("Can not approve exam already analyzed");
    }

    private boolean isFieldsValidForApproving(ImmunohematologyExamDTO exam){
        if (exam.bloodType() == null) return false;
        return exam.irregularAntibodies() == IrregularAntibodies.NEGATIVE;
    }

    private void editExamData(ImmunohematologyExam exam, ImmunohematologyExamDTO examDTO){
        exam.setIrregularAntibodies(examDTO.irregularAntibodies());
        exam.setBloodType(examDTO.bloodType());
    }

    private void approveExam(Exam exam) {
        exam.setStatus(ExamStatus.APPROVED);
        exam.setUpdatedAt(LocalDateTime.now());
    }

    public SerologicalScreeningExam registerApprovedExam(UUID examId, SerologicalScreeningExamDTO examDTO) {
        SerologicalScreeningExam exam = retrieveSerologicalScreeningExam(examId);

        isUnderAnalysis(exam);

        if (!isFieldsValidForApproving(examDTO)) throw new InvalidExamAnalysisException("Serological screening exam has invalid field(s) for approving");

        editExamData(exam, examDTO);
        approveExam(exam);

        return examRepository.save(exam);
    }

    private SerologicalScreeningExam retrieveSerologicalScreeningExam(UUID examId){
        Optional<Exam> optionalExam = examRepository.findById(examId);
        if (optionalExam.isEmpty()) throw new ExamNotFoundException("Exam not found");
        return (SerologicalScreeningExam) optionalExam.get();
    }

    private boolean isFieldsValidForApproving(SerologicalScreeningExamDTO exam){
        if (exam.hepatitisB() == null || exam.hepatitisB() == DiseaseDetection.POSITIVE) return false;
        if (exam.hepatitisC() == null || exam.hepatitisC() == DiseaseDetection.POSITIVE) return false;
        if (exam.chagasDisease() == null || exam.chagasDisease() == DiseaseDetection.POSITIVE) return false;
        if (exam.syphilis() == null || exam.syphilis() == DiseaseDetection.POSITIVE) return false;
        if (exam.aids() == null || exam.aids() == DiseaseDetection.POSITIVE) return false;
        return exam.htlv1_2() != null && exam.htlv1_2() != DiseaseDetection.POSITIVE;
    }

    private void editExamData(SerologicalScreeningExam exam, SerologicalScreeningExamDTO examDTO){
        exam.setHepatitisB(examDTO.hepatitisB());
        exam.setHepatitisC(examDTO.hepatitisC());
        exam.setChagasDisease(examDTO.chagasDisease());
        exam.setSyphilis(examDTO.syphilis());
        exam.setAids(examDTO.aids());
        exam.setHtlv1_2(examDTO.htlv1_2());
    }

    public ImmunohematologyExam registerRejectedExam(UUID examId, ImmunohematologyExamDTO examDTO) {
        ImmunohematologyExam exam = retrieveImmunohematologyExam(examId);

        isUnderAnalysis(exam);

        if (!isFieldsValidForRejecting(examDTO)) throw new InvalidExamAnalysisException("Immunohematology exam has invalid field(s) for rejecting");

        editExamData(exam, examDTO);
        rejectExam(exam);

        return examRepository.save(exam);
    }

    private boolean isFieldsValidForRejecting(ImmunohematologyExamDTO exam){
        if (exam.bloodType() == null) return true;
        return exam.irregularAntibodies() == IrregularAntibodies.POSITIVE;
    }

    private void rejectExam(Exam exam) {
        exam.setStatus(ExamStatus.REJECTED);
        exam.setUpdatedAt(LocalDateTime.now());
    }

    public SerologicalScreeningExam registerRejectedExam(UUID examId, SerologicalScreeningExamDTO examDTO) {
        SerologicalScreeningExam exam = retrieveSerologicalScreeningExam(examId);

        isUnderAnalysis(exam);

        if (!isFieldsValidForRejecting(examDTO)) throw new InvalidExamAnalysisException("Serological screening exam has invalid field(s) for rejecting");

        editExamData(exam, examDTO);
        rejectExam(exam);

        return examRepository.save(exam);
    }

    private boolean isFieldsValidForRejecting(SerologicalScreeningExamDTO exam){
        if (exam.hepatitisB() != null && exam.hepatitisB() == DiseaseDetection.POSITIVE) return true;
        if (exam.hepatitisC() != null && exam.hepatitisC() == DiseaseDetection.POSITIVE) return true;
        if (exam.chagasDisease() != null && exam.chagasDisease() == DiseaseDetection.POSITIVE) return true;
        if (exam.syphilis() != null && exam.syphilis() == DiseaseDetection.POSITIVE) return true;
        if (exam.aids() != null && exam.aids() == DiseaseDetection.POSITIVE) return true;
        return exam.htlv1_2() != null && exam.htlv1_2() == DiseaseDetection.POSITIVE;
    }
}
