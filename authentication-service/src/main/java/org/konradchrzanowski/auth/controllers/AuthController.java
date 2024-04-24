package org.konradchrzanowski.auth.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.konradchrzanowski.auth.entities.AuthRequest;
import org.konradchrzanowski.auth.entities.AuthResponse;
import org.konradchrzanowski.auth.services.AuthService;
import org.konradchrzanowski.auth.services.impl.AuthServiceImpl;
import org.konradchrzanowski.clients.email.EmailClient;
import org.konradchrzanowski.clients.token.TokenClient;
import org.konradchrzanowski.clients.user.UserClient;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.payload.request.LoginRequest;
import org.konradchrzanowski.utils.common.payload.request.NewPasswordPutRequest;
import org.konradchrzanowski.utils.common.payload.request.PasswordResetRequest;
import org.konradchrzanowski.utils.common.payload.request.RegisterRequest;
import org.konradchrzanowski.utils.common.payload.response.MessageResponse;
import org.konradchrzanowski.utils.common.payload.response.SentEmailResponse;
import org.konradchrzanowski.utils.common.payload.response.UserInfoResponse;
import org.konradchrzanowski.utils.exception.EmailAlreadyExistsException;
import org.konradchrzanowski.utils.exception.EmailNotFoundException;
import org.konradchrzanowski.utils.exception.PasswordNotMatchException;
import org.konradchrzanowski.utils.exception.UsernameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    @Value("${tokenValidityTimeInMinutes}")
    private Long tokenValidityTimeInMinutes;

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    private final UserClient userClient;
    private final EmailClient emailClient;
    private final TokenClient tokenClient;

    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    @PostMapping("/login")
    public ResponseEntity<JWTToken> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("REST request to login user {}", loginRequest);
        LoginRequest updatedRequest =
                LoginRequest.builder(loginRequest).username(loginRequest.getUsername().toLowerCase()).build();
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(updatedRequest.getUsername(), updatedRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthTokenFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok().build();
    }

    static class JWTToken {

        private String tokenValue;

        JWTToken(String tokenValue) {
            this.tokenValue = tokenValue;
        }

        @JsonProperty("id_token")
        String getTokenValue() {
            return tokenValue;
        }

        void setTokenValue(String tokenValue) {
            this.tokenValue = tokenValue;
        }
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        log.debug("REST request to register new user {}", registerRequest);
        RegisterRequest updatedRequest =
                RegisterRequest.builder(registerRequest)
                        .username(registerRequest.getUsername()
                                .toLowerCase()).build();
        if (isUsernameTaken(updatedRequest.getUsername())) {
            throw new UsernameAlreadyExistsException("Error. Username is already in use.");
        }

        if (isEmailTaken(updatedRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Error. Email is already in use!");
        }

        UserDTO savedUser = authService.register(updatedRequest);

        SentEmailResponse response = emailClient.sendEmailAfterRegistration(savedUser).getBody();
        //todo validate response
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/confirm")
    public String confirmRegistration(@RequestParam("token") String token) {
        log.debug("REST request to confirm user registration. Token: {}", token);
        SentEmailResponse response = emailClient.sendEmailAfterEmailConfirmation(token).getBody();
        //todo validate response
        return authService.confirmUser(token);
    }

    @PutMapping("/request-password-reset")
    @Transactional
    public ResponseEntity<?> passwordReset(@Valid @NotNull @RequestBody PasswordResetRequest passwordResetRequest) {
        log.debug("REST request to set new password for user: {}", passwordResetRequest.getEmail());

        if (!isEmailTaken(passwordResetRequest.email())) {
            throw new EmailNotFoundException("Email not found");
        }
        UserDTO userDTO = userClient.getUserByEmail(passwordResetRequest.email()).getBody();
        SentEmailResponse response = emailClient.sendPasswordReset(userDTO).getBody();
        //todo validate response
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> newPasswordPut(@RequestBody NewPasswordPutRequest request) {
        log.debug("REST request to set new password by token: {}", request.token());
        validatePasswordMatch(request);
        UserDTO updatedUser = authService.saveNewPassword(request);
        SentEmailResponse response = emailClient.sendPasswordChange(updatedUser).getBody();
        //todo validate response
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-account")
    public UserInfoResponse getAccount() {
        return authService.getUserWithAuthorities();
    }

    private boolean isEmailTaken(String email) {
        return Boolean.TRUE.equals(userClient.isEmailExists(email));
    }

    private boolean isUsernameTaken(String userName) {
        return Boolean.TRUE.equals(userClient.isUserExists(userName));
    }

    private void validatePasswordMatch(NewPasswordPutRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new PasswordNotMatchException("Password not match");
        }
    }


}
