package org.konradchrzanowski.auth.services.impl;

import org.konradchrzanowski.auth.security.JwtUtil;
import org.konradchrzanowski.auth.services.AuthService;
import org.konradchrzanowski.auth.util.SecurityUtils;
import org.konradchrzanowski.clients.user.UserClient;
import org.konradchrzanowski.utils.common.dto.RoleDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.enumeration.ERole;
import org.konradchrzanowski.utils.common.payload.request.NewPasswordPutRequest;
import org.konradchrzanowski.utils.common.payload.request.RegisterRequest;
import org.konradchrzanowski.utils.common.payload.response.SentEmailResponse;
import org.konradchrzanowski.utils.common.payload.response.UserInfoResponse;
import org.konradchrzanowski.utils.exception.EmailAlreadyExistsException;
import org.konradchrzanowski.utils.exception.UsernameAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserClient userClient, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userClient = userClient;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDTO register(RegisterRequest registerRequest) {
        log.info("Register request: {}", registerRequest);
        RegisterRequest updatedRequest = RegisterRequest.builder(registerRequest)
                .username(registerRequest.getUsername().toLowerCase()).build();
        if (userClient.isUserExists(registerRequest.getUsername())) {
            throw new UsernameAlreadyExistsException("Error: User is already in use:" + registerRequest.getUsername());
        }
        if (userClient.isEmailExists(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Error: Email is already in use:" + registerRequest.getEmail());
        }
        RegisterRequest encodedRequest = RegisterRequest.builder(updatedRequest)
                .password(passwordEncoder.encode(registerRequest.getPassword())).build();
        return userClient.registerNewUser(encodedRequest).getBody();
    }

    @Override
    public SentEmailResponse confirmUser(String token) {
        log.info("Confirm user: {}", token);
        String confirmed = userClient.confirm(token).getBody();
        return new SentEmailResponse(confirmed, true);
    }

    @Override
    public UserDTO saveNewPassword(NewPasswordPutRequest newPasswordPutRequest) {
        return null;
    }

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

    @Override
    public String generateToken(Authentication authentication) {
        return jwtUtil.generateJwtToken(authentication);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateJwtToken(token);
    }

}
