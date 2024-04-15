package org.konradchrzanowski.services;

import lombok.AllArgsConstructor;
import org.konradchrzanowski.entities.AuthRequest;
import org.konradchrzanowski.entities.AuthResponse;

import org.konradchrzanowski.user.UserClient;
import org.konradchrzanowski.user.payload.UserVO;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final UserClient userClient;
    private final JwtUtil jwtUtil;

//    public AuthService(RestTemplate restTemplate, UserClient userClient, JwtUtil jwtUtil) {
//        this.restTemplate = restTemplate;
//        this.userClient = userClient;
//        this.jwtUtil = jwtUtil;
//    }

    public AuthResponse register(AuthRequest request) {
        request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        UserVO registeredUserVO = userClient.saveUser(new UserVO(null,request.getEmail(),request.getPassword(),null));

        assert registeredUserVO != null;
        String accessToken = jwtUtil.generate(registeredUserVO.getId(), registeredUserVO.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(registeredUserVO.getId(), registeredUserVO.getRole(), "REFRESH");
        return new AuthResponse(accessToken, refreshToken);
    }

}
