package com.nilay.support.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    // Only token and role go back to frontend
    // Never send password, never send full user object

    private String token;
    private String role;
}