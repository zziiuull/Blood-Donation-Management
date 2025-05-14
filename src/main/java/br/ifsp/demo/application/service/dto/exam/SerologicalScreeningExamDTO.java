package br.ifsp.demo.application.service.dto.exam;

import br.ifsp.demo.controller.exam.request.SerologicalScreeningExamRequest;
import br.ifsp.demo.domain.model.exam.DiseaseDetection;


public record SerologicalScreeningExamDTO(
        DiseaseDetection hepatitisB,
        DiseaseDetection hepatitisC,
        DiseaseDetection chagasDisease,
        DiseaseDetection syphilis,
        DiseaseDetection aids,
        DiseaseDetection htlv1_2
) {
    public static SerologicalScreeningExamDTO fromRequest(SerologicalScreeningExamRequest exam){
        return new SerologicalScreeningExamDTO(
                exam.hepatitisB(),
                exam.hepatitisC(),
                exam.chagasDisease(),
                exam.syphilis(),
                exam.aids(),
                exam.htlv1_2()
        );
    }
}
