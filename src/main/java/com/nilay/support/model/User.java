package com.nilay.support.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // name — display name, cannot be null
    @Column(nullable = false    )
    private String name;

    // email — login credential, must be unique
    @Column(unique = true, nullable = false)
    private String email;

    // password — BCrypt hashed, never plain text
    private String password;

    // role — only CUSTOMER, AGENT, or ADMIN allowed
    @Enumerated(EnumType.STRING)
    private Role role;

    // createdAt — set automatically when user is created
    @CreationTimestamp
    private LocalDateTime createdAt;

}
