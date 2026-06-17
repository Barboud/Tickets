package net.hackyourfuture.tickets.service;

import lombok.RequiredArgsConstructor;
import net.hackyourfuture.tickets.dto.tickets.CreateTicketDTO;
import net.hackyourfuture.tickets.dto.tickets.UpdateTicketDTO;
import net.hackyourfuture.tickets.model.tickets.Ticket;
import net.hackyourfuture.tickets.model.tickets.TicketStatus;
import net.hackyourfuture.tickets.repository.AssigneeRepository;
import net.hackyourfuture.tickets.repository.TicketRepository;
import net.hackyourfuture.tickets.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AssigneeRepository assigneeRepository;
    private final EmailService emailService;

    public List<Ticket> searchTicket(String search, String status) {
        List<Ticket> tickets = ticketRepository.searchTicket(search, status);
        for (Ticket ticket : tickets) {
            ticket.setAssignees(assigneeRepository.findUsersByTicketId(ticket.getId()));
        }
        return tickets;
    }

    public ResponseEntity<?> getTicketById(int id) {
        try {
            Ticket ticket = ticketRepository.findTicketById(id);
            ticket.setAssignees(assigneeRepository.findUsersByTicketId(id));
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", "Ticket not found with id: " + id));
        }
    }

    public void createTicket(CreateTicketDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setProjectId(dto.getProjectId());
        ticket.setStatus(TicketStatus.OPEN);
        ticketRepository.saveTicket(ticket);
    }

    public ResponseEntity<Map<String, String>> updateTicket(int id, UpdateTicketDTO dto) {
        Ticket ticket;

        try {
            ticket = ticketRepository.findTicketById(id);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", "Ticket not found with id: " + id));
        }

        if (dto.getTitle() != null) ticket.setTitle(dto.getTitle());
        if (dto.getDescription() != null) ticket.setDescription(dto.getDescription());
        if (dto.getStatus() != null) ticket.setStatus(dto.getStatus());
        if (dto.getProjectId() != null) ticket.setProjectId(dto.getProjectId());
        ticketRepository.updateTicket(ticket);

        ticket.setAssignees(assigneeRepository.findUsersByTicketId(id));
        emailService.sendTicketUpdate(ticket);

        return ResponseEntity.ok(Map.of("message", "Ticket updated successfully"));
    }

    public ResponseEntity<Map<String, String>> addAssignee(int ticketId, int userId) {
        try {
            ticketRepository.findTicketById(ticketId);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", "Ticket not found with id: " + ticketId));
        }
        try {
            userRepository.findUserById(userId);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found with id: " + userId));
        }
        assigneeRepository.addAssignee(ticketId, userId);
        return ResponseEntity.ok(Map.of("message", "Assignee added successfully"));
    }

    public ResponseEntity<Map<String, String>> removeAssignee(int ticketId, int userId) {
        try {
            ticketRepository.findTicketById(ticketId);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", "Ticket not found with id: " + ticketId));
        }
        try {
            userRepository.findUserById(userId);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found with id: " + userId));
        }
        assigneeRepository.removeAssignee(ticketId, userId);
        return ResponseEntity.ok(Map.of("message", "Assignee removed successfully"));
    }
}