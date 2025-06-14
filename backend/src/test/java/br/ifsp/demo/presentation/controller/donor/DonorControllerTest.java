package br.ifsp.demo.presentation.controller.donor;

import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.presentation.BaseApiIntegrationTest;
import org.junit.jupiter.api.BeforeEach;

class DonorControllerTest extends BaseApiIntegrationTest {

    private Physician user;
    private String token;

    @BeforeEach
    void setUp() {
        this.user = registerPhysician("Teste123");
        this.token = authenticate(user.getEmail(), "Teste123");
    }
}