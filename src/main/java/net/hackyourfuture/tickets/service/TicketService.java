package net.hackyourfuture.tickets.service;

import lombok.RequiredArgsConstructor;
import net.hackyourfuture.tickets.dto.CreateTicketDTO;
import net.hackyourfuture.tickets.dto.UpdateTicketDTO;
import net.hackyourfuture.tickets.model.tickets.Ticket;
import net.hackyourfuture.tickets.model.tickets.TicketStatus;
import net.hackyourfuture.tickets.repository.AssigneeRepository;
import net.hackyourfuture.tickets.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final AssigneeRepository assigneeRepository;
    private final EmailService emailService;

    public List<Ticket> searchTicket(String text, String status) {
        List<Ticket> tickets = ticketRepository.searchTicket(text, status);
        for (Ticket ticket : tickets) {
            ticket.setAssignees(assigneeRepository.findUsersByTicketId(ticket.getId()));
        }
        return tickets;
    }

    public Ticket getTicketById(int id) {
        Ticket ticket = ticketRepository.findTicketById(id);
        ticket.setAssignees(assigneeRepository.findUsersByTicketId(id));
        return ticket;
    }

    public void createTicket(CreateTicketDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setProjectId(dto.getProjectId());
        ticket.setStatus(TicketStatus.OPEN);
        ticketRepository.saveTicket(ticket);
    }

    public void updateTicket(int id, UpdateTicketDTO dto) {
        Ticket ticket = ticketRepository.findTicketById(id);
        if (dto.getTitle() != null) ticket.setTitle(dto.getTitle());
        if (dto.getDescription() != null) ticket.setDescription(dto.getDescription());
        if (dto.getStatus() != null) ticket.setStatus(TicketStatus.valueOf(String.valueOf(dto.getStatus())));
        if (dto.getProjectId() != null) ticket.setProjectId(dto.getProjectId());
        ticketRepository.updateTicket(ticket);

        ticket.setAssignees(assigneeRepository.findUsersByTicketId(id));
        emailService.sendTicketUpdate(ticket);
    }

    public void addAssignee(int ticketId, int userId) {
        assigneeRepository.addAssignee(ticketId, userId);
    }

    public void removeAssignee(int ticketId, int userId) {
        assigneeRepository.removeAssignee(ticketId, userId);
    }
}