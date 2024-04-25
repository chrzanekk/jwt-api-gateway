package org.konradchrzanowski.auth.services;

import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.payload.request.NewPasswordPutRequest;
import org.konradchrzanowski.utils.common.payload.request.RegisterRequest;
import org.konradchrzanowski.utils.common.payload.response.SentEmailResponse;
import org.konradchrzanowski.utils.common.payload.response.UserInfoResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {


    UserDTO register(RegisterRequest registerRequest);

    SentEmailResponse confirmUser(String token);

    UserDTO saveNewPassword(NewPasswordPutRequest newPasswordPutRequest);

    UserInfoResponse getUserWithAuthorities();

    String generateToken(Authentication authentication);

    boolean validateToken(String token);
}
