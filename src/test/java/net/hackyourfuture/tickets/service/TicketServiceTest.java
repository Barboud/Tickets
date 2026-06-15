package net.hackyourfuture.tickets.service;

import net.hackyourfuture.tickets.dto.CreateTicketDTO;
import net.hackyourfuture.tickets.dto.UpdateTicketDTO;
import net.hackyourfuture.tickets.model.tickets.Ticket;
import net.hackyourfuture.tickets.model.tickets.TicketStatus;
import net.hackyourfuture.tickets.repository.AssigneeRepository;
import net.hackyourfuture.tickets.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        ticketService = new TicketService(ticketRepository, assigneeRepository);
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

        assertEquals("open", ticket.getStatus());
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
        dto.setStatus(TicketStatus.valueOf(TicketStatus.OPEN.name()));

        // Act
        ticketService.updateTicket(1, dto);

        // Assert
        assertEquals("closed", existingTicket.getStatus());
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
        Ticket result = ticketService.getTicketById(1);

        // Assert
        assertEquals(1, result.getId());
        assertEquals("Test Ticket", result.getTitle());
    }
}