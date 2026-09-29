package com.nilay.support.service;

import com.nilay.support.dto.request.CommentRequest;
import com.nilay.support.dto.response.CommentResponse;
import com.nilay.support.model.Comment;
import com.nilay.support.model.User;
import com.nilay.support.repository.CommentRepository;
import com.nilay.support.repository.TicketRepository;
import com.nilay.support.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public CommentResponse addComment(Long ticketId, String email, CommentRequest request){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(user);
        comment.setTicket(ticketRepository.getReferenceById(ticketId));

        Comment savedComment = commentRepository.save(comment);

        return mapToResponse(savedComment);
    }

    public List<CommentResponse> getComments(Long ticketId){
        List<Comment> commentList = commentRepository.findByTicketId(ticketId);

       return commentList.stream()
                .map(this ::mapToResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse mapToResponse (Comment comment){
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setAuthorName(comment.getAuthor().getName());
        response.setCreatedAt(comment.getCreatedAt());

        return response;
    }
}
