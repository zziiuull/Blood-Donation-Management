package br.ifsp.demo.application.service.exam.dto;

import br.ifsp.demo.domain.model.exam.DiseaseDetection;

import java.time.LocalDateTime;

public record SerologicalScreeningExamDTO(
        DiseaseDetection hepatitisB,
        DiseaseDetection hepatitisC,
        DiseaseDetection chagasDisease,
        DiseaseDetection syphilis,
        DiseaseDetection aids,
        DiseaseDetection htlv1_2
) {
}
