package br.ifsp.demo.domain.model.exam;

import br.ifsp.demo.application.service.dto.exam.SerologicalScreeningExamDTO;
import br.ifsp.demo.domain.model.donation.Donation;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SerologicalScreeningExam extends Exam {
    private DiseaseDetection hepatitisB;
    private DiseaseDetection hepatitisC;
    private DiseaseDetection chagasDisease;
    private DiseaseDetection syphilis;
    private DiseaseDetection aids;
    private DiseaseDetection htlv1_2;

    public SerologicalScreeningExam(Donation donation) {
        super(donation, ExamStatus.UNDER_ANALYSIS);
    }

    public SerologicalScreeningExam() {
        super();
    }

    @Override
    public boolean isFieldsValidForApproval() {
        if (hepatitisB != DiseaseDetection.NEGATIVE) return false;
        if (hepatitisC != DiseaseDetection.NEGATIVE) return false;
        if (chagasDisease != DiseaseDetection.NEGATIVE) return false;
        if (syphilis != DiseaseDetection.NEGATIVE) return false;
        if (aids != DiseaseDetection.NEGATIVE) return false;
        return htlv1_2 == DiseaseDetection.NEGATIVE;
    }

    @Override
    public boolean isFieldsValidForRejection() {
        if (hepatitisB == DiseaseDetection.POSITIVE) return true;
        if (hepatitisC == DiseaseDetection.POSITIVE) return true;
        if (chagasDisease == DiseaseDetection.POSITIVE) return true;
        if (syphilis == DiseaseDetection.POSITIVE) return true;
        if (aids == DiseaseDetection.POSITIVE) return true;
        return htlv1_2 == DiseaseDetection.POSITIVE;
    }

    public void updateResults(SerologicalScreeningExamDTO examDTO){
        hepatitisB = examDTO.hepatitisB();
        hepatitisC = examDTO.hepatitisC();
        chagasDisease = examDTO.chagasDisease();
        syphilis = examDTO.syphilis();
        aids = examDTO.aids();
        htlv1_2 = examDTO.htlv1_2();
    }
}
