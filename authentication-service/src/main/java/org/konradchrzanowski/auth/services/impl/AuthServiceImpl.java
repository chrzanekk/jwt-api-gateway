package org.konradchrzanowski.auth.services.impl;

import lombok.AllArgsConstructor;
import org.konradchrzanowski.auth.services.AuthService;
import org.konradchrzanowski.auth.security.JwtUtil;
import org.konradchrzanowski.auth.util.SecurityUtils;
import org.konradchrzanowski.clients.token.TokenClient;
import org.konradchrzanowski.clients.user.UserClient;
import org.konradchrzanowski.utils.common.dto.RoleDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.enumeration.ERole;
import org.konradchrzanowski.utils.common.payload.request.NewPasswordPutRequest;
import org.konradchrzanowski.utils.common.payload.request.RegisterRequest;
import org.konradchrzanowski.utils.common.payload.response.UserInfoResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public UserDTO saveNewPassword(NewPasswordPutRequest newPasswordPutRequest) {
        return null;
    }

    private final UserClient userClient;
    private final TokenClient tokenClient;
    private final JwtUtil jwtUtil;

    @Override
    public UserInfoResponse getUserWithAuthorities() {
        //todo need to check if user exists or just take roles from current logged user.
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserDTO currentUser = userClient.getUserByUserName(currentLogin).getBody();
        assert currentUser != null;
        List<ERole> currentRoles = currentUser.getRoles().stream().map(RoleDTO::getName).toList();
        return new UserInfoResponse(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentRoles);
    }

}
