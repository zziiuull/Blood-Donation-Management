package br.ifsp.demo.application.service.dto.appointment;

import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.AppointmentStatus;
import br.ifsp.demo.domain.model.donation.CollectionSite;


import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentDTO(
        UUID id,
        LocalDateTime appointmentDate,
        AppointmentStatus status,
        CollectionSite collectionSite,
        String notes
) {
    public AppointmentDTO(Appointment appointment) {
        this(
                appointment.getId(),
                appointment.getAppointmentDate(),
                appointment.getStatus(),
                appointment.getCollectionSite(),
                appointment.getNotes()
        );
    }
}
