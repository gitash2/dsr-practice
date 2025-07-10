package ru.vsu.dsr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.dsr.dto.UserDTO;
import ru.vsu.dsr.model.User;
import ru.vsu.dsr.service.UserService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<User> getUser(@RequestParam String username) {
        return ok(userService.getUserByUsername(username));
    }

    @GetMapping("/search")
    public List<UserDTO> searchUser(@RequestParam String username) {
        return userService.searchUsersByUsername(username);
    }
}
