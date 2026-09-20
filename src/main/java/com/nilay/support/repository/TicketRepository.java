package com.nilay.support.repository;

import com.nilay.support.model.Ticket;
import com.nilay.support.model.TicketStatus;
import com.nilay.support.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Find all tickets created by a specific user (customer view)
    List<Ticket> findTicketsByCreatedBy (User createdBy);

    // Find all tickets assigned to a specific user (agent view)
    List<Ticket> findTicketsByAssignedTo (User assignedTo);

    // Find all tickets with a specific status (admin filter)
    List<Ticket> findByStatus(TicketStatus status);

    // Find tickets by created by AND status (customer filtering their own)
    List<Ticket> findByCreatedByAndStatus (User createdBy, TicketStatus status);
}