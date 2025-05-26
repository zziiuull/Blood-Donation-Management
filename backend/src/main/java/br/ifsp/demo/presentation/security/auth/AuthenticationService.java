package br.ifsp.demo.presentation.security.auth;

import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.physician.Crm;
import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.domain.model.physician.State;
import br.ifsp.demo.presentation.exception.EntityAlreadyExistsException;
import br.ifsp.demo.presentation.security.config.JwtService;
import br.ifsp.demo.presentation.security.user.JpaUserRepository;
import br.ifsp.demo.presentation.security.user.Role;
import br.ifsp.demo.presentation.security.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterUserResponse register(RegisterUserRequest request) {

        userRepository.findByEmail(request.email()).ifPresent(unused -> {
            throw new EntityAlreadyExistsException("Email already registered: " + request.email());});

        String encryptedPassword = passwordEncoder.encode(request.password());

        final UUID id = UUID.randomUUID();
        final User user = User.builder()
                .id(id)
                .name(request.name())
                .lastname(request.lastname())
                .email(request.email())
                .password(encryptedPassword)
                .role(Role.USER)
                .build();

        userRepository.save(user);
        return new RegisterUserResponse(id);
    }

    public RegisterUserResponse registerPhysician(RegisterPhysicianRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(unused -> {
            throw new EntityAlreadyExistsException("Email already registered: " + request.email());});

        final UUID id = UUID.randomUUID();
        final Physician physician = new Physician(
                id,
                request.name(),
                request.lastname(),
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.PHYSICIAN,
                new Cpf(request.cpf()),
                new Crm(request.crmNumber(), State.valueOf(request.crmState()))
        );

        userRepository.save(physician);
        return new RegisterUserResponse(id);
    }

    public AuthResponse authenticate(AuthRequest request) {
        final var authentication = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        authenticationManager.authenticate(authentication);

        final User user = userRepository.findByEmail(request.username()).orElseThrow();
        final String token = jwtService.generateToken(user);
        final Role role = user.getRole();

        return new AuthResponse(token, role);
    }
}
