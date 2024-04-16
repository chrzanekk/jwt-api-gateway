package org.konradchrzanowski.services;

import lombok.AllArgsConstructor;
import org.konradchrzanowski.clients.user.UserClient;
import org.konradchrzanowski.clients.user.payload.UserVO;
import org.konradchrzanowski.entities.AuthRequest;
import org.konradchrzanowski.entities.AuthResponse;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthService {

//todo check why UserClient have error with autowired
    private final UserClient userClient;
    private final JwtUtil jwtUtil;

    public AuthResponse register(AuthRequest request) {
        request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        UserVO registeredUserVO = userClient.saveUser(new UserVO(null,request.getEmail(),request.getPassword(),null));

        assert registeredUserVO != null;
        String accessToken = jwtUtil.generate(registeredUserVO.getId(), registeredUserVO.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(registeredUserVO.getId(), registeredUserVO.getRole(), "REFRESH");
        return new AuthResponse(accessToken, refreshToken);
    }

}
