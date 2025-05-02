package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.application.service.exam.dto.ImmunohematologyExamDTO;
import br.ifsp.demo.application.service.exam.dto.SerologicalScreeningExamDTO;
import br.ifsp.demo.domain.model.exam.*;
import br.ifsp.demo.domain.repository.exam.ExamRepository;
import br.ifsp.demo.exception.ExamAlreadyAnalyzedException;
import br.ifsp.demo.exception.EntityNotFoundException;
import br.ifsp.demo.exception.InvalidExamAnalysisException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExamRegistrationService {
    private ExamRepository examRepository;

    public ExamRegistrationService() {
    }

    public ExamRegistrationService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public ImmunohematologyExam registerApprovedExam(UUID examId, ImmunohematologyExamDTO examDTO) {
        Optional<Exam> optionalExam = examRepository.findById(examId);
        if (optionalExam.isEmpty()) throw new EntityNotFoundException("Exam not found");
        ImmunohematologyExam exam = (ImmunohematologyExam) optionalExam.get();

        if (!isUnderAnalysis(exam)) throw new ExamAlreadyAnalyzedException("Can not approve exam already analyzed");

        if (!isFieldsValidForApproving(examDTO)) throw new InvalidExamAnalysisException("Immunohematology exam has invalid field(s) for approving");

        editExamData(exam, examDTO);
        approveExam(exam);

        return examRepository.save(exam);
    }

    private boolean isUnderAnalysis(Exam exam){
        return exam.getStatus() == ExamStatus.UNDER_ANALYSIS;
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
        Optional<Exam> optionalExam = examRepository.findById(examId);
        if (optionalExam.isEmpty()) throw new EntityNotFoundException("Exam not found");
        SerologicalScreeningExam exam = (SerologicalScreeningExam) optionalExam.get();

        if (!isUnderAnalysis(exam)) throw new ExamAlreadyAnalyzedException("Can not approve exam already analyzed");

        if (!isFieldsValidForApproving(examDTO)) throw new InvalidExamAnalysisException("Serological screening exam has invalid field(s) for approving");

        editExamData(exam, examDTO);
        approveExam(exam);

        return examRepository.save(exam);
    }

    private boolean isFieldsValidForApproving(SerologicalScreeningExamDTO exam){
        if (exam.hepatitisB() != null && exam.hepatitisB() == DiseaseDetection.POSITIVE) return false;
        if (exam.hepatitisC() != null && exam.hepatitisC() == DiseaseDetection.POSITIVE) return false;
        if (exam.chagasDisease() != null && exam.chagasDisease() == DiseaseDetection.POSITIVE) return false;
        if (exam.syphilis() != null && exam.syphilis() == DiseaseDetection.POSITIVE) return false;
        if (exam.aids() != null && exam.aids() == DiseaseDetection.POSITIVE) return false;
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
        Optional<Exam> optionalExam = examRepository.findById(examId);
        if (optionalExam.isEmpty()) throw new EntityNotFoundException("Exam not found");
        ImmunohematologyExam exam = (ImmunohematologyExam) optionalExam.get();

        if (!isUnderAnalysis(exam)) throw new ExamAlreadyAnalyzedException("Can not approve exam already analyzed");

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
        Optional<Exam> optionalExam = examRepository.findById(examId);
        if (optionalExam.isEmpty()) throw new EntityNotFoundException("Exam not found");
        SerologicalScreeningExam exam = (SerologicalScreeningExam) optionalExam.get();

        if (!isUnderAnalysis(exam)) throw new ExamAlreadyAnalyzedException("Can not approve exam already analyzed");

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
