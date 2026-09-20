package com.nilay.support.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    // id — primary key, auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // title — short summary, not null
    @Column(nullable = false)
    private String title;

    // description — full problem details, not null
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    // status — current state, default OPEN
    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.OPEN;

    // priority — urgency level, default MEDIUM
    @Enumerated(EnumType.STRING)
    private TicketPriority priority = TicketPriority.MEDIUM;

    // category — type of issue
    @Enumerated(EnumType.STRING)
    private TicketCategory category;

    // createdBy — which user created this ticket
    //             @ManyToOne because many tickets → one user
    //             @JoinColumn stores user_id in tickets table
    @ManyToOne
    @JoinColumn (name = "user_id")
    private User createdBy;

    // assignedTo — which agent handles this
    //              nullable — not assigned immediately
    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    // slaDeadline — deadline based on priority
    private LocalDateTime slaDeadline;

    // createdAt — auto set when ticket created
    @CreationTimestamp
    private LocalDateTime createdAt;

    // resolvedAt — null until ticket is resolved
    private LocalDateTime resolvedAt;
}
