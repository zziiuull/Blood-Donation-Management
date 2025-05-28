package br.ifsp.demo.application.service.appointment;

import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.AppointmentStatus;
import br.ifsp.demo.domain.model.donation.CollectionSite;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should get all appointments")
        void shouldGetAllAppointments() {
            CollectionSite site = new CollectionSite(
                    "Banco de Doação de Sorocaba",
                    new ContactInfo("doesangue.sorocaba@email.com", "1533761530", "Av. Anhanguera, n. 715, Sorocaba/SP")
            );
            Appointment appointment = new Appointment(
                    LocalDateTime.now().plusDays(1),
                    AppointmentStatus.SCHEDULED,
                    site,
                    "Primeira doação"
            );
            List<Appointment> appointments = List.of(appointment);

            when(appointmentRepository.findAll()).thenReturn(appointments);

            List<Appointment> result = appointmentService.getAll();

            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(appointments);

            verify(appointmentRepository, times(1)).findAll();
        }
    }
}