package ru.vsu.dsr.dto;



public record BearerToken(
        String rawToken
) {
    public String token() {
        return rawToken.substring(7);
    }
}
