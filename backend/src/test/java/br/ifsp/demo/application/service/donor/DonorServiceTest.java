package br.ifsp.demo.application.service.donor;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.donor.Sex;
import br.ifsp.demo.infrastructure.repository.donor.DonorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonorServiceTest {
    @Mock
    private DonorRepository donorRepository;

    @InjectMocks
    private DonorService donorService;

    @Nested
    @DisplayName("For valid tests")
    class ValidTests {
        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("UnitTest")
        @DisplayName("Should get all donors")
        void shouldGetAllDonors() {
            List<Donor> donors = List.of(
                    new Donor(
                            "Donor",
                            new Cpf("12345678910"),
                            new ContactInfo("donor@mail.com", "1194301-2389", "Donor street"),
                            LocalDate.of(2000, 10, 9),
                            70.0,
                            Sex.MALE,
                            BloodType.A_POS));

            when(donorRepository.findAll()).thenReturn(donors);

            List<Donor> result = donorService.getAll();

            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(donors);

            verify(donorRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return false if donor is not eligible")
        void shouldReturnFalseIfDonorNotEligible() {
            Donor donor = mock(Donor.class);
            when(donor.isEligibleForDonation()).thenReturn(false);

            boolean result = donorService.canDonateToday(donor, LocalDate.now(), null);
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("For invalid tests")
    class InvalidTests {
        @Test
        @Tag("Structural")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when donor is null")
        void shouldThrowExceptionWhenDonorIsNull() {
            assertThatThrownBy(() -> donorService.canDonateToday(null, LocalDate.now(), null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @Tag("Structural")
        @Tag("UnitTest")
        @DisplayName("Should throw exception when today is null")
        void shouldThrowExceptionWhenTodayIsNull() {
            Donor donor = mock(Donor.class);
            assertThatThrownBy(() -> donorService.canDonateToday(donor, null, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}