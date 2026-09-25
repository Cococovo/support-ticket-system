package com.nilay.support.controller;

import com.nilay.support.dto.request.CreateTicketRequest;
import org.springframework.security.core.Authentication;
import com.nilay.support.dto.response.TicketResponse;
import com.nilay.support.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping (("/api/tickets"))
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping // -> ("/tickets")
    public ResponseEntity<TicketResponse> createTicket (
            @Valid @RequestBody CreateTicketRequest request,
            Authentication authentication){

        // Extracting the email id from the authenticated user
        String email = authentication.getName();
        return ResponseEntity.ok(ticketService.createTicket(request, email));
    }

    @GetMapping // -> ("/tickets")
    public ResponseEntity<List<TicketResponse>> getMyTickets (
            Authentication authentication){

        String email = authentication.getName();
        return ResponseEntity.ok(ticketService.getMyTickets(email));
    }

    @GetMapping ("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(
                ticketService.getTicketById(id, authentication.getName())
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<TicketResponse>> getAllTickets (){
        return ResponseEntity.ok(ticketService.getAllTickets());
    }
}
