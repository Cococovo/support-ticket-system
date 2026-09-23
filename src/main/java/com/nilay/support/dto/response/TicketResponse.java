package com.nilay.support.dto.response;

import com.nilay.support.model.TicketCategory;
import com.nilay.support.model.TicketPriority;
import com.nilay.support.model.TicketStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketResponse {

    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private TicketCategory category;
    private String createdBy;
    private LocalDateTime slaDeadline;
    private LocalDateTime createdAt;
}
