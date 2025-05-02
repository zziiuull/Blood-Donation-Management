package br.ifsp.demo.domain.model;

import br.ifsp.demo.domain.model.common.ContactInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContactInfoTest {

    @Test
    @Tag("UnitTest")
    @DisplayName("Should create Contact Info when valid")
    void shouldCreateContactInfoWhenValid() {
        ContactInfo contactInfo = new ContactInfo(
                "doesangue@email.com",
                "1533761530",
                "Av. Anhanguera, n. 715, Sorocaba/SP"
        );
        assertThat(contactInfo.getEmail()).isEqualTo("doesangue@email.com");
        assertThat(contactInfo.getPhone()).isEqualTo("1533761530");
        assertThat(contactInfo.getAddress()).isEqualTo("Av. Anhanguera, n. 715, Sorocaba/SP");
    }

    @Test
    @Tag("UnitTest")
    @DisplayName("Should throw exception when email is null")
    void shouldThrowExceptionWhenEmailIsNull(){
        assertThatThrownBy(() -> new ContactInfo(null, "1533761530", "Av. Anhanguera, n. 715, Sorocaba/SP"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email must not be blank");
    }

    @Test
    @Tag("UnitTest")
    @DisplayName("Should throw exception when phone is null")
    void shouldThrowExceptionWhenPhoneIsNull(){
        assertThatThrownBy(() -> new ContactInfo("doesangue@email.com", null, "Av. Anhanguera, n. 715, Sorocaba/SP"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Phone must not be blank");
    }

    @Test
    @Tag("UnitTest")
    @DisplayName("Should throw exception when address is null")
    void shouldThrowExceptionWhenAddressIsNull(){
        assertThatThrownBy(() -> new ContactInfo("doesangue@email.com", "1533761530", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Address must not be blank");
    }
}