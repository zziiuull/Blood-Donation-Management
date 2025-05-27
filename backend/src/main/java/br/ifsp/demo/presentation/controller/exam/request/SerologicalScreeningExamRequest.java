package br.ifsp.demo.presentation.controller.exam.request;

import br.ifsp.demo.domain.model.exam.DiseaseDetection;
import jakarta.validation.constraints.NotNull;

public record SerologicalScreeningExamRequest(
        @NotNull(message = "hepatitisB field is required")
        DiseaseDetection hepatitisB,

        @NotNull(message = "hepatitisC field is required")
        DiseaseDetection hepatitisC,

        @NotNull(message = "chagasDisease field is required")
        DiseaseDetection chagasDisease,

        @NotNull(message = "syphilis field is required")
        DiseaseDetection syphilis,

        @NotNull(message = "aids field is required")
        DiseaseDetection aids,

        @NotNull(message = "htlv1_2 field is required")
        DiseaseDetection htlv1_2
) {
}
