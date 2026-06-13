package net.hackyourfuture.tickets.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assignee {
    private int id;
    private int ticketId;
    private int userId;
}