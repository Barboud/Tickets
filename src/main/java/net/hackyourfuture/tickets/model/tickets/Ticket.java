package net.hackyourfuture.tickets.model.tickets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.hackyourfuture.tickets.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private int id;
    private String title;
    private String description;
    private int projectId;
    private TicketStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<User> assignees;
}