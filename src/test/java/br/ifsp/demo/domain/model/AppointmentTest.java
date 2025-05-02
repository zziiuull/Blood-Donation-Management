package br.ifsp.demo.domain.model;

import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.AppointmentStatus;
import br.ifsp.demo.domain.model.donation.CollectionSite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppointmentTest {

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("UnitTest")
        @DisplayName("Should create appointment when data is valid")
        void shouldCreateAppointmentWhenDataIsValid() {
            ContactInfo siteContact = new ContactInfo(
                    "doesangue@email.com",
                    "1533761530",
                    "Av. Anhanguera, n. 715, Sorocaba/SP"
            );
            CollectionSite site = new CollectionSite(
                    "Banco de Doação de Sangue de Sorocaba",
                    siteContact
            );
            Appointment appointment = new Appointment(
                    LocalDateTime.now().plusDays(2),
                    AppointmentStatus.SCHEDULED,
                    site,
                    "First donation"
            );
            assertThat(appointment).isNotNull();
            assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
            assertThat(appointment.getCollectionSite()).isEqualTo(site);
        }
    }

    @Nested
    @DisplayName("For invalid tests")
    class InvalidTests {
        @Test
        @Tag("UnitTest")
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

    @Test
    @Tag("UnitTest")
    @DisplayName("Should throw exception when colletion site is null")
    void shouldThrowExceptionWhenCollectionSiteIsNull() {
        assertThatThrownBy(() -> new Appointment(
                LocalDateTime.now().plusDays(1),
                AppointmentStatus.SCHEDULED,
                null,
                "first donation"
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Collection site must not be null");
    }

}