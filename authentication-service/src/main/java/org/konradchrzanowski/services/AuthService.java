package org.konradchrzanowski.services;

import org.konradchrzanowski.entities.AuthRequest;
import org.konradchrzanowski.entities.AuthResponse;
import org.konradchrzanowski.entities.UserOV;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public AuthService(RestTemplate restTemplate, JwtUtil jwtUtil) {
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(AuthRequest request) {
        request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        UserOV registeredUser = restTemplate.postForObject("http://user-service/users", request, UserOV.class);

        assert registeredUser != null;
        String accessToken = jwtUtil.generate(registeredUser.getId(), registeredUser.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(registeredUser.getId(), registeredUser.getRole(), "REFRESH");
        return new AuthResponse(accessToken, refreshToken);
    }

}
