package ru.vsu.dsr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vsu.dsr.dto.JWTAuthenticationResponse;
import ru.vsu.dsr.dto.SignInRequest;
import ru.vsu.dsr.dto.UserDTO;
import ru.vsu.dsr.model.User;
import ru.vsu.dsr.service.UserService;
import ru.vsu.dsr.service.auth.AuthenticationService;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<User> register(@RequestBody UserDTO dto) {
        return ok(userService.saveUser(dto));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestHeader(name = "Authorization") String token) {
        boolean res = authenticationService.validateToken(token);
        return ResponseEntity.ok(res);
    }

    @PostMapping("sign-in")
    public ResponseEntity<JWTAuthenticationResponse> signIn(@RequestBody SignInRequest req) {
        return ResponseEntity.ok(authenticationService.signIn(req));
    }
}
