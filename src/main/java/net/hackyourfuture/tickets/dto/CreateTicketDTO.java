package net.hackyourfuture.tickets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketDTO {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Integer projectId;
}