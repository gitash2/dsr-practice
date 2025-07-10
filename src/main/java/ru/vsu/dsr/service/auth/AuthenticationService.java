package ru.vsu.dsr.service.auth;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.vsu.dsr.dto.JWTAuthenticationResponse;
import ru.vsu.dsr.dto.SignInRequest;
import ru.vsu.dsr.repository.UserRepository;

import java.util.Date;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public JWTAuthenticationResponse signIn(SignInRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.username(),
                        req.password()
                )
        );

        UserDetails user = userDetailsService.loadUserByUsername(req.username());
        String jwt = jwtService.generateToken(user);
        Date expiresAt = jwtService.extractExpiration(jwt);

        return new JWTAuthenticationResponse(jwt, expiresAt, userRepository.findByUsername(user.getUsername()).orElse(null));

    }

    public boolean validateToken(String token) {
        String username = jwtService.extractUsername(token);
        log.info("Validating token for user: {}", username);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        boolean isValid = jwtService.isTokenValid(token, user);

        if (isValid) {
            log.info("Token for user: {} is valid", username);
            return true;
        }

        log.info("Token for user: {} is not valid", username);
        return false;
    }
}
