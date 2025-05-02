package br.ifsp.demo.domain.model;

import br.ifsp.demo.domain.model.common.Cpf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CpfTest {

    @Test
    @Tag("UnitTest")
    @DisplayName("Should create CPF when valid")
    void shouldCreateCpfWhenValid() {
        Cpf cpf = new Cpf("71742125140");
        assertThat(cpf.getNumber()).isEqualTo("71742125140");
    }

    @Test
    @Tag("UnitTest")
    @DisplayName("Should throw exception when CPF is invalid")
    void shouldThrowExceptionWhenCpfIsInvalid() {
        assertThatThrownBy(() -> new Cpf("1234567"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid CPF format");
    }

}