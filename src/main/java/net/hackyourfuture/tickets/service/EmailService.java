package net.hackyourfuture.tickets.service;

import net.hackyourfuture.tickets.model.User;
import net.hackyourfuture.tickets.model.tickets.Ticket;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private final RestClient resendClient;

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.from}")
    private String from;

    public EmailService(@Qualifier("resendClient") RestClient resendClient) {
        this.resendClient = resendClient;
    }

    public void sendTicketUpdate(Ticket ticket) {

        // 1. no assignees just return
        if (ticket.getAssignees() == null || ticket.getAssignees().isEmpty()) return;

        // 2. collect all assignees in one line
        String names = ticket.getAssignees().stream()
                .map(User::getName)
                .collect(Collectors.joining(", "));

        // 3. build the message
        String body = "Ticket #" + ticket.getId() + " updated:\n"
                + "Title: " + ticket.getTitle() + "\n"
                + "Status: " + ticket.getStatus().name() + "\n\n"
                + "Current assignees: " + names;

        // 4. send to all assignees.
        for (User user : ticket.getAssignees()) {
            try {
                Map<String, Object> payload = Map.of(
                        "from", from,
                        "to", List.of(user.getEmail()),
                        "subject", "Ticket #" + ticket.getId() + " updated",
                        "text", body
                );

                resendClient.post()
                        .uri("/emails")
                        .header("Authorization", "Bearer " + apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(payload)
                        .retrieve()
                        .toBodilessEntity();

            } catch (Exception e) {
                System.out.println("Failed to send email to " + user.getEmail());
            }
        }

    }
}