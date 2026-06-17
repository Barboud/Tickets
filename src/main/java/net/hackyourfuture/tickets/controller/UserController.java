package net.hackyourfuture.tickets.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.hackyourfuture.tickets.dto.users.CreateUserDTO;
import net.hackyourfuture.tickets.dto.users.UpdateUserDTO;
import net.hackyourfuture.tickets.model.User;
import net.hackyourfuture.tickets.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public void createUser(@Valid @RequestBody CreateUserDTO dto) {
        userService.createUser(dto);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable int id, @Valid @RequestBody UpdateUserDTO dto) {
        userService.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        // return 204
        return ResponseEntity.noContent().build();
    }
}