package br.ifsp.demo.presentation;

import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.presentation.security.auth.AuthRequest;
import br.ifsp.demo.presentation.security.auth.AuthResponse;
import br.ifsp.demo.presentation.security.user.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static io.restassured.RestAssured.baseURI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseApiIntegrationTest {
    @LocalServerPort
    protected int port = 8080;

    @Autowired
    protected JpaUserRepository repository;

    @BeforeEach
    public void generalSetup() {
        baseURI = "http://localhost:8080";
    }

    protected Physician registerPhysician(String password) {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Physician physician = EntityBuilder.createRandomPhysician(encoder.encode(password));
        repository.save(physician);
        return physician;
    }

    protected String authenticate(String username, String password) {
        RestTemplate restTemplate = new RestTemplate();
        AuthRequest authRequest = new AuthRequest(username, password);
        final AuthResponse response = restTemplate.postForObject(baseURI + "/api/v1/authenticate", authRequest, AuthResponse.class);
        return response.token();
    }
}
