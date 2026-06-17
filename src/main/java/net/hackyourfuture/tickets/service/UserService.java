package net.hackyourfuture.tickets.service;

import lombok.RequiredArgsConstructor;
import net.hackyourfuture.tickets.dto.users.CreateUserDTO;
import net.hackyourfuture.tickets.dto.users.UpdateUserDTO;
import net.hackyourfuture.tickets.model.User;
import net.hackyourfuture.tickets.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }
}