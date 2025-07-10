package ru.vsu.dsr.dto;

public record SignInRequest(
        String username,
        String password
) {
}
