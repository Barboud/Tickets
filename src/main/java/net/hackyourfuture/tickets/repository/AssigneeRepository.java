package net.hackyourfuture.tickets.repository;

import lombok.RequiredArgsConstructor;
import net.hackyourfuture.tickets.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AssigneeRepository {

    private final JdbcTemplate jdbcTemplate;

    // Methods to fetch all users assigned to the specified ticket.
    public List<User> findUsersByTicketId(int ticketId) {
        return jdbcTemplate.query(
                "SELECT users.id, users.name, users.email FROM users INNER JOIN assignees ON assignees.user_id = users.id WHERE assignees.ticket_id = ?",
                (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("name"), rs.getString("email")),
                ticketId
        );
    }

    public void addAssignee(int ticketId, int userId) {
        jdbcTemplate.update(
                "INSERT INTO assignees (ticket_id, user_id) VALUES (?, ?)",
                ticketId, userId
        );
    }

    public void removeAssignee(int ticketId, int userId) {
        jdbcTemplate.update(
                "DELETE FROM assignees WHERE ticket_id = ? AND user_id = ?",
                ticketId, userId
        );
    }
}