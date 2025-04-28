package br.ifsp.demo.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

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



}