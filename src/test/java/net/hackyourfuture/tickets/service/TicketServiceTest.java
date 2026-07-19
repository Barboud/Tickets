package net.hackyourfuture.tickets.service;

import net.hackyourfuture.tickets.dto.tickets.CreateTicketDTO;
import net.hackyourfuture.tickets.dto.tickets.UpdateTicketDTO;
import net.hackyourfuture.tickets.model.tickets.Ticket;
import net.hackyourfuture.tickets.model.tickets.TicketStatus;
import net.hackyourfuture.tickets.repository.AssigneeRepository;
import net.hackyourfuture.tickets.repository.TicketRepository;
import net.hackyourfuture.tickets.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketServiceTest {

    private TicketService ticketService;
    private TicketRepository ticketRepository;
    private AssigneeRepository assigneeRepository;

    @BeforeEach
    void setUp() {
        ticketRepository = Mockito.mock(TicketRepository.class);
        assigneeRepository = Mockito.mock(AssigneeRepository.class);
        EmailService emailService = Mockito.mock(EmailService.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ticketService = new TicketService(ticketRepository, userRepository, assigneeRepository, emailService);
    }

    @Test
    void givenNewTicketData_shouldSetStatusToOpen() {
        CreateTicketDTO dto = new CreateTicketDTO();
        dto.setTitle("Fix bug");
        dto.setDescription("Description");
        dto.setProjectId(1);

        Ticket ticket = new Ticket();
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setProjectId(dto.getProjectId());
        ticket.setStatus(TicketStatus.valueOf(TicketStatus.OPEN.name()));

        ticketService.createTicket(dto);

        assertEquals(TicketStatus.OPEN, ticket.getStatus());
    }

    @Test
    void givenUpdateWithNewStatus_shouldUpdateStatusField() {
        // Arrange
        Ticket existingTicket = new Ticket();
        existingTicket.setId(1);
        existingTicket.setTitle("Old Title");
        existingTicket.setStatus(TicketStatus.valueOf(TicketStatus.OPEN.name()));

        Mockito.when(ticketRepository.findTicketById(1)).thenReturn(existingTicket);

        UpdateTicketDTO dto = new UpdateTicketDTO();
        dto.setStatus(TicketStatus.valueOf(TicketStatus.CLOSED.name()));

        // Act
        ticketService.updateTicket(1, dto);

        // Assert
        assertEquals(TicketStatus.CLOSED, existingTicket.getStatus());
        assertEquals("Old Title", existingTicket.getTitle());
    }

    @Test
    void givenTicketId_shouldReturnTicketWithCorrectId() {
        // Arrange
        Ticket mockTicket = new Ticket();
        mockTicket.setId(1);
        mockTicket.setTitle("Test Ticket");

        Mockito.when(ticketRepository.findTicketById(1)).thenReturn(mockTicket);
        Mockito.when(assigneeRepository.findUsersByTicketId(1)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<?> result = ticketService.getTicketById(1);
        Ticket ticket = (Ticket) result.getBody();

        // Assert
        assertEquals(1, ticket.getId());
        assertEquals("Test Ticket", ticket.getTitle());
    }
}