package org.konradchrzanowski.auth.services.impl;

import lombok.AllArgsConstructor;
import org.konradchrzanowski.auth.entities.AuthRequest;
import org.konradchrzanowski.auth.entities.AuthResponse;
import org.konradchrzanowski.auth.services.AuthService;
import org.konradchrzanowski.auth.services.JwtUtil;
import org.konradchrzanowski.clients.token.TokenClient;
import org.konradchrzanowski.clients.user.UserClient;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.payload.request.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Override
    public UserDTO register(RegisterRequest registerRequest) {
        return null;
    }

    @Override
    public String confirmUser(String token) {
        return "";
    }

    private final UserClient userClient;
    private final TokenClient tokenClient;
    private final JwtUtil jwtUtil;



}
