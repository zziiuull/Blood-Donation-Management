package br.ifsp.demo.application.service.exam;

import br.ifsp.demo.application.service.dto.exam.ImmunohematologyExamDTO;
import br.ifsp.demo.application.service.dto.exam.SerologicalScreeningExamDTO;
import br.ifsp.demo.domain.model.exam.*;
import br.ifsp.demo.infrastructure.repository.exam.ExamRepository;
import br.ifsp.demo.presentation.exception.ExamAlreadyAnalyzedException;
import br.ifsp.demo.presentation.exception.ExamNotFoundException;
import br.ifsp.demo.presentation.exception.InvalidExamAnalysisException;
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

    public ImmunohematologyExam registerApprovedExam(UUID examId, ImmunohematologyExamDTO examDTO, LocalDateTime updatedAt) {
        ImmunohematologyExam exam = retrieveImmunohematologyExam(examId);

        isUnderAnalysis(exam);
        exam.updateResults(examDTO);
        exam.approve(updatedAt);

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

    public SerologicalScreeningExam registerApprovedExam(UUID examId, SerologicalScreeningExamDTO examDTO, LocalDateTime updatedAt) {
        SerologicalScreeningExam exam = retrieveSerologicalScreeningExam(examId);

        isUnderAnalysis(exam);
        exam.updateResults(examDTO);
        exam.approve(updatedAt);

        return examRepository.save(exam);
    }

    private SerologicalScreeningExam retrieveSerologicalScreeningExam(UUID examId){
        Optional<Exam> optionalExam = examRepository.findById(examId);
        if (optionalExam.isEmpty()) throw new ExamNotFoundException("Exam not found");
        return (SerologicalScreeningExam) optionalExam.get();
    }

    public ImmunohematologyExam registerRejectedExam(UUID examId, ImmunohematologyExamDTO examDTO, LocalDateTime updatedAt) {
        ImmunohematologyExam exam = retrieveImmunohematologyExam(examId);

        isUnderAnalysis(exam);
        exam.updateResults(examDTO);
        exam.reject(updatedAt);

        return examRepository.save(exam);
    }

    public SerologicalScreeningExam registerRejectedExam(UUID examId, SerologicalScreeningExamDTO examDTO, LocalDateTime updatedAt) {
        SerologicalScreeningExam exam = retrieveSerologicalScreeningExam(examId);

        isUnderAnalysis(exam);
        exam.updateResults(examDTO);
        exam.reject(updatedAt);

        return examRepository.save(exam);
    }
}
