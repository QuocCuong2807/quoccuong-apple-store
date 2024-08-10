package com.springteam.backend.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthResponse {
    private String accessToken;
    private Set<String> role;
}
