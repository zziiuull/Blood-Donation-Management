package br.ifsp.demo.infrastructure.repository.donor;

import br.ifsp.demo.domain.model.donor.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DonorRepository extends JpaRepository<Donor, UUID> {
}
