package ru.vsu.dsr.dto;

import ru.vsu.dsr.model.User;

public record UserDTO(
        Long id,
        String username,
        String password
) {
    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword());
    }
}
