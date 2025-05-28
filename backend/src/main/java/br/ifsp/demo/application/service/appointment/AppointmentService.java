package br.ifsp.demo.application.service.appointment;

import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAll(){
        return appointmentRepository.findAll();
    }
}
