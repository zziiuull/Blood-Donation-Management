package br.ifsp.demo.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {

    }
    @Nested
    @DisplayName("For invalid tests")
    class InvalidTests {
        @Test
        @DisplayName("Should throw exception when appointment date is in the past")
        void shouldThrowExceptionWhenAppointmentDateIsInThePast() {
            ContactInfo siteContact = new ContactInfo(
                    "doesangue@email.com",
                    "1533761530",
                    "Av. Anhanguera, n. 715, Sorocaba/SP"
            );
            CollectionSite site = new CollectionSite(
                    "Banco de Doação de Sangue de Sorocaba",
                    siteContact
            );

            assertThatThrownBy(() -> new Appointment(
                    LocalDateTime.now().minusDays(1),
                    AppointmentStatus.SCHEDULED,
                    site,
                    "First donation"
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Appointment date must be in the future");

        }
    }



}