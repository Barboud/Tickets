package net.hackyourfuture.tickets.dto.tickets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.hackyourfuture.tickets.model.tickets.TicketStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketDTO {
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private TicketStatus status;
    @NotNull
    private Integer projectId;
}