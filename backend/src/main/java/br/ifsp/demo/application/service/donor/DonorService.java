package br.ifsp.demo.application.service.donor;

import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.infrastructure.repository.donor.DonorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonorService {
    private final DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public List<Donor> getAll(){
        return donorRepository.findAll();
    }
}
