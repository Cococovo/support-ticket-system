package com.nilay.support.service;

import com.nilay.support.dto.request.CreateTicketRequest;
import com.nilay.support.dto.response.TicketResponse;
import com.nilay.support.model.Ticket;
import com.nilay.support.model.TicketPriority;
import com.nilay.support.model.TicketStatus;
import com.nilay.support.model.User;
import com.nilay.support.repository.TicketRepository;
import com.nilay.support.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public TicketResponse createTicket(CreateTicketRequest request, String email){

        // Step 1: Load user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 2: Create ticket object
        Ticket ticket = new Ticket();

        // Step 3: Set all fields
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setCategory(request.getCategory());

        ticket.setCreatedBy(user);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.MEDIUM);

        // Step 4: Calculate SLA deadline based on priority
        LocalDateTime slaDeadline = calculateSlaDeadline(TicketPriority.MEDIUM);
        ticket.setSlaDeadline(slaDeadline);

        // Step 5: Save ticket
        Ticket savedTicket = ticketRepository.save(ticket);

        // Step 6: Return mapped response
        return mapToResponse(savedTicket);
    }

    public List<TicketResponse> getMyTickets(String email) {
        // Load user, find their tickets, map to response
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Ticket> ticketList = ticketRepository.findTicketsByCreatedBy(user);

        return ticketList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TicketResponse getTicketById(Long id, String email) {
        // Find ticket by id, throw exception if not found
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        return mapToResponse(ticket);
    }

    public List<TicketResponse> getAllTickets() {
        // Return all tickets (admin)
        List<Ticket> ticketList = ticketRepository.findAll();

        return ticketList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Helper: convert Ticket entity to TicketResponse DTO
    private TicketResponse mapToResponse(Ticket ticket) {
        // Map each field
        // For createdBy: use ticket.getCreatedBy().getName()

        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setTitle(ticket.getTitle());
        response.setDescription(ticket.getDescription());
        response.setStatus(ticket.getStatus());
        response.setPriority(ticket.getPriority());
        response.setCategory(ticket.getCategory());
        response.setCreatedBy(ticket.getCreatedBy().getName());
        response.setSlaDeadline(ticket.getSlaDeadline());
        response.setCreatedAt(ticket.getCreatedAt());

        return response;
    }

    // Helper: calculate SLA deadline based on priority
    private LocalDateTime calculateSlaDeadline(TicketPriority priority) {
        // Use switch statement on priority
        // Return LocalDateTime.now().plusHours(X)
        return switch (priority) {
            case CRITICAL -> LocalDateTime.now().plusHours(2);
            case HIGH     -> LocalDateTime.now().plusHours(8);
            case MEDIUM   -> LocalDateTime.now().plusHours(24);
            case LOW      -> LocalDateTime.now().plusHours(72);
        };
    }
}