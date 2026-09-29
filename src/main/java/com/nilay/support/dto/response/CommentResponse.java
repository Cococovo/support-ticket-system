package com.nilay.support.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {

    private Long id;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
}
