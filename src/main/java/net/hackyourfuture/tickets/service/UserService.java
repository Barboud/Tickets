package net.hackyourfuture.tickets.service;

import lombok.RequiredArgsConstructor;
import net.hackyourfuture.tickets.dto.users.CreateUserDTO;
import net.hackyourfuture.tickets.dto.users.UpdateUserDTO;
import net.hackyourfuture.tickets.model.User;
import net.hackyourfuture.tickets.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    public User getUserById(int id) {
        return userRepository.findUserById(id);
    }

    public void createUser(CreateUserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        userRepository.saveUser(user);
    }

    public void updateUser(int id, UpdateUserDTO dto) {
        User user = userRepository.findUserById(id);
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        userRepository.updateUser(user);
    }

    public ResponseEntity<Map<String, String>> deleteUser(int id) {
        try {
            userRepository.findUserById(id);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found with id: " + id));
        }
        userRepository.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
}