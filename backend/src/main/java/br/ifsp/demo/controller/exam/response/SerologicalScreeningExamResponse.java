package br.ifsp.demo.controller.exam.response;

import br.ifsp.demo.domain.model.exam.DiseaseDetection;
import br.ifsp.demo.domain.model.exam.ExamStatus;
import br.ifsp.demo.domain.model.exam.SerologicalScreeningExam;

import java.time.LocalDateTime;
import java.util.UUID;

public record SerologicalScreeningExamResponse(
        UUID id,
        UUID donationId,
        ExamStatus examStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String observations,
        DiseaseDetection hepatitisB,
        DiseaseDetection hepatitisC,
        DiseaseDetection chagasDisease,
        DiseaseDetection syphilis,
        DiseaseDetection aids,
        DiseaseDetection htlv1_2
) {
    public SerologicalScreeningExamResponse(SerologicalScreeningExam exam){
        this(
                exam.getId(),
                exam.getDonation().getId(),
                exam.getStatus(),
                exam.getCreatedAt(),
                exam.getUpdatedAt(),
                exam.getObservations(),
                exam.getHepatitisB(),
                exam.getHepatitisC(),
                exam.getChagasDisease(),
                exam.getSyphilis(),
                exam.getAids(),
                exam.getHtlv1_2()
        );
    }
}
