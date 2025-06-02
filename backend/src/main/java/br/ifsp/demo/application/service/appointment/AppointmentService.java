package br.ifsp.demo.application.service.appointment;

import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.AppointmentStatus;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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

    public List<Appointment> getUpcomingAppointments(LocalDateTime now) {
        if (now == null)
            throw new IllegalArgumentException("Reference datetime must not be null");

        return appointmentRepository.findAll()
                .stream()
                .filter(a -> a.getAppointmentDate().isAfter(now))
                .toList();
    }

    public boolean canReschedule(Appointment appointment, LocalDateTime now) {
        if (appointment == null || now == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        }

        LocalDateTime scheduledDate = appointment.getAppointmentDate();
        AppointmentStatus status = appointment.getStatus();

        return status == AppointmentStatus.SCHEDULED
                && scheduledDate.isAfter(now)
                && Duration.between(now, scheduledDate).toHours() >= 24;
    }
}
