package net.hackyourfuture.tickets.repository;

import lombok.RequiredArgsConstructor;
import net.hackyourfuture.tickets.model.tickets.Ticket;
import net.hackyourfuture.tickets.model.tickets.TicketStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TicketRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Ticket> findAllTickets() {
        return jdbcTemplate.query(
                "SELECT * FROM tickets",
                (rs, rowNum) -> new Ticket(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("project_id"),
                        TicketStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        null
                )
        );
    }

    public Ticket findTicketById(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM tickets WHERE id = ?",
                (rs, rowNum) -> new Ticket(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("project_id"),
                        TicketStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        null
                ),
                id
        );
    }

    public void saveTicket(Ticket ticket) {
        jdbcTemplate.update(
                "INSERT INTO tickets (title, description, project_id, status) VALUES (?, ?, ?, ?)",
                ticket.getTitle(), ticket.getDescription(), ticket.getProjectId(), ticket.getStatus().name()
        );
    }

    public void updateTicket(Ticket ticket) {
        jdbcTemplate.update(
                "UPDATE tickets SET title = ?, description = ?, status = ?, project_id = ?, updated_at = NOW() WHERE id = ?",
                ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name(), ticket.getProjectId(), ticket.getId()
        );
    }

    public List<Ticket> searchTicket(String text, String status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tickets WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (text != null && !text.isBlank()) {
            sql.append(" AND (title ILIKE ? OR description ILIKE ?)");
            params.add("%" + text + "%");
            params.add("%" + text + "%");
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        return jdbcTemplate.query(sql.toString(),
                (rs, rowNum) -> new Ticket(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("project_id"),
                        TicketStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        null
                ),
                params.toArray()
        );
    }
}
