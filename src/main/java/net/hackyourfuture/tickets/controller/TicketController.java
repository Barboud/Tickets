package net.hackyourfuture.tickets.controller;

import net.hackyourfuture.tickets.dto.tickets.CreateTicketDTO;
import net.hackyourfuture.tickets.dto.tickets.UpdateTicketDTO;
import net.hackyourfuture.tickets.model.tickets.Ticket;
import net.hackyourfuture.tickets.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public List<Ticket> getAllTicketS(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String status
    ) {
        return ticketService.searchTicket(text, status);
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable int id) {
        return ticketService.getTicketById(id);
    }

    @PostMapping
    public void createTicket(@RequestBody CreateTicketDTO dto) {
        ticketService.createTicket(dto);
    }

    @PutMapping("/{id}")
    public void updateTicket(@PathVariable int id, @RequestBody UpdateTicketDTO dto) {
        ticketService.updateTicket(id, dto);
    }

    @PostMapping("/{id}/assignees")
    public void addAssignee(@PathVariable int id, @RequestBody Map<String, Integer> body) {
        ticketService.addAssignee(id, body.get("userId"));
    }

    @DeleteMapping("/{id}/assignees/{userId}")
    public void removeAssignee(@PathVariable int id, @PathVariable int userId) {
        ticketService.removeAssignee(id, userId);
    }
}