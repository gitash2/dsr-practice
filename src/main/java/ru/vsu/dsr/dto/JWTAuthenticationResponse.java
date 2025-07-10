package ru.vsu.dsr.dto;

import ru.vsu.dsr.model.User;

import java.util.Date;

public record JWTAuthenticationResponse(
        String token,
        Date expiresAt,
        User user
) {
}
