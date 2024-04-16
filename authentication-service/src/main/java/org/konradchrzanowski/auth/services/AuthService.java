package org.konradchrzanowski.auth.services;

import lombok.AllArgsConstructor;
import org.konradchrzanowski.clients.user.UserClient;
import org.konradchrzanowski.clients.user.payload.UserVO;
import org.konradchrzanowski.auth.entities.AuthRequest;
import org.konradchrzanowski.auth.entities.AuthResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserClient userClient;
    private final JwtUtil jwtUtil;

    public AuthResponse register(AuthRequest request) {
        request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));

        UserVO registeredUserVO = userClient.saveUser(new UserVO(null, request.getEmail(), request.getPassword(), null));

        assert registeredUserVO != null;
        String accessToken = jwtUtil.generate(registeredUserVO.getId(), registeredUserVO.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(registeredUserVO.getId(), registeredUserVO.getRole(), "REFRESH");
        return new AuthResponse(accessToken, refreshToken);
    }

}
