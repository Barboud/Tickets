package net.hackyourfuture.tickets.repository;

import lombok.RequiredArgsConstructor;
import net.hackyourfuture.tickets.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;


    public List<User> findAllUsers() {
        return jdbcTemplate.query(
                "SELECT * FROM users",
                (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"))
        );
    }

    public User findUserById(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("name"), rs.getString("email")),
                id
        );
    }

    public User findUserByEmail(String email) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE email = ?",
                (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("name"), rs.getString("email")),
                email
        );
    }

    public void saveUser(User user) {
        jdbcTemplate.update(
                "INSERT INTO users (name, email) VALUES (?, ?)",
                user.getName(), user.getEmail()
        );
    }

    public void updateUser(User user) {
        jdbcTemplate.update(
                "UPDATE users SET name = ?, email = ? WHERE id = ?",
                user.getName(), user.getEmail(), user.getId()
        );
    }

    public void deleteUser(int id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
}