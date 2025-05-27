package br.ifsp.demo.infrastructure.repository.exam;

import br.ifsp.demo.domain.model.exam.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamRepository extends JpaRepository<Exam, UUID> {
    List<Exam> findAllByDonationId(UUID donationId);
}
