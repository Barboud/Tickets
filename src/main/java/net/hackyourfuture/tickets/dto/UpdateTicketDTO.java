package net.hackyourfuture.tickets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.hackyourfuture.tickets.model.tickets.TicketStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketDTO {
    private String title;
    private String description;
    private TicketStatus status;
    private Integer projectId;
}