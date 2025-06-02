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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

        @Test
        @Tag("Functional")
        @Tag("UnitTest")
        @DisplayName("Should return empty list when there are no appointments")
        void shouldReturnEmptyListIfNoAppointmentsExist() {
            when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());

            List<Appointment> result = appointmentService.getAll();

            assertThat(result).isNotNull();
            assertThat(result).isEmpty();

            verify(appointmentRepository, times(1)).findAll();
        }

        @Test
        @Tag("Structural")
        @Tag("UnitTest")
        @DisplayName("Should return only upcoming appointments")
        void shouldReturnOnlyUpcomingAppointments() {
            LocalDateTime now = LocalDateTime.now();

            Appointment pastAppointment = mock(Appointment.class);
            when(pastAppointment.getAppointmentDate()).thenReturn(now.minusDays(1));

            Appointment futureAppointment = mock(Appointment.class);
            when(futureAppointment.getAppointmentDate()).thenReturn(now.plusDays(1));

            when(appointmentRepository.findAll())
                    .thenReturn(List.of(pastAppointment, futureAppointment));

            List<Appointment> result = appointmentService.getUpcomingAppointments(now);

            assertThat(result)
                    .hasSize(1)
                    .containsExactly(futureAppointment);

            verify(appointmentRepository).findAll();
        }

    }
    @Nested
    @DisplayName("For invalid tests")
    class InvalidTests {
        @Test
        @Tag("Functional")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when repository fails")
        void shouldThrowExceptionWhenRepositoryFails() {
            when(appointmentRepository.findAll()).thenThrow(new RuntimeException("DB is down"));

            assertThatThrownBy(() -> appointmentService.getAll())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("DB is down");

            verify(appointmentRepository).findAll();
        }

        @Test
        @Tag("Structural")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when reference date is null")
        void shouldThrowExceptionWhenReferenceDateIsNull() {
            assertThatThrownBy(() -> appointmentService.getUpcomingAppointments(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Reference datetime must not be null");

        }

        @Test
        @Tag("Structural")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when appointment is null")
        void shouldThrowExceptionWhenAppointmentIsNull() {
            assertThatThrownBy(() -> appointmentService.canReschedule(null, LocalDateTime.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Arguments must not be null");
        }

        @Test
        @Tag("Structural")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when now is null")
        void shouldThrowExceptionWhenNowIsNull() {
            Appointment mockAppointment = mock(Appointment.class);

            assertThatThrownBy(() -> appointmentService.canReschedule(mockAppointment, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Arguments must not be null");
        }
    }
}