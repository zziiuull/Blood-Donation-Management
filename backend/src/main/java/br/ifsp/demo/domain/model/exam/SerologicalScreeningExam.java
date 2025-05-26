package br.ifsp.demo.domain.model.exam;

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

    public void updateResults(SerologicalScreeningExamDTO examDTO){
        hepatitisB = examDTO.hepatitisB();
        hepatitisC = examDTO.hepatitisC();
        chagasDisease = examDTO.chagasDisease();
        syphilis = examDTO.syphilis();
        aids = examDTO.aids();
        htlv1_2 = examDTO.htlv1_2();
    }
}
