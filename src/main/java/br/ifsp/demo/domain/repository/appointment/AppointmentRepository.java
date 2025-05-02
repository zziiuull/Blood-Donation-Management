package br.ifsp.demo.domain.repository.appointment;

import br.ifsp.demo.domain.model.donation.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
}
