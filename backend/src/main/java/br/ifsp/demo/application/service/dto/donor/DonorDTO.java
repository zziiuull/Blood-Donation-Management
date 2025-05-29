package br.ifsp.demo.application.service.dto.donor;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.donor.Sex;

import java.time.LocalDate;
import java.util.UUID;

public record DonorDTO(
        UUID id,
        String name,
        Cpf cpf,
        ContactInfo contactInfo,
        LocalDate birthDate,
        Double weight,
        Sex sex,
        BloodType bloodType
) {
    public DonorDTO(Donor donor){
        this(
                donor.getId(),
                donor.getName(),
                donor.getCpf(),
                donor.getContactInfo(),
                donor.getBirthDate(),
                donor.getWeight(),
                donor.getSex(),
                donor.getBloodType()
        );
    }
}
