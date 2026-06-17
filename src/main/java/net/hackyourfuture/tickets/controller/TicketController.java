package net.hackyourfuture.tickets.controller;

import jakarta.validation.Valid;
import net.hackyourfuture.tickets.dto.tickets.CreateTicketDTO;
import net.hackyourfuture.tickets.dto.tickets.UpdateTicketDTO;
import net.hackyourfuture.tickets.model.tickets.Ticket;
import net.hackyourfuture.tickets.service.TicketService;
import org.springframework.http.ResponseEntity;
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

    // GET //tickets?search=...&status=... - search/filter tickets.
    // No filter return all
    @GetMapping
    public List<Ticket> getAllTicketS(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status
    ) {
        return ticketService.searchTicket(search, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable int id) {
        return ticketService.getTicketById(id);
    }

    @PostMapping
    public void createTicket(@RequestBody CreateTicketDTO dto) {
        ticketService.createTicket(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateTicket(@PathVariable int id,@Valid @RequestBody UpdateTicketDTO dto) {
        return ticketService.updateTicket(id, dto);
    }

    @PostMapping("/{id}/assignees")
    public ResponseEntity<Map<String, String>> addAssignee(@PathVariable int id, @RequestBody Map<String, Integer> body) {
        return ticketService.addAssignee(id, body.get("userId"));
    }

    @DeleteMapping("/{id}/assignees/{userId}")
    public ResponseEntity<Map<String, String>> removeAssignee(@PathVariable int id, @PathVariable int userId) {
        return ticketService.removeAssignee(id, userId);
    }
}