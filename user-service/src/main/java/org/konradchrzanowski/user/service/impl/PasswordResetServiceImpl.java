package org.konradchrzanowski.user.service.impl;

import org.konradchrzanowski.clients.token.TokenClient;
import org.konradchrzanowski.user.service.PasswordResetService;
import org.konradchrzanowski.user.service.UserService;
import org.konradchrzanowski.utils.common.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.payload.request.NewPasswordPutRequest;
import org.konradchrzanowski.utils.common.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final Logger log = LoggerFactory.getLogger(PasswordResetServiceImpl.class);

    private final TokenClient tokenClient;
    private final UserService userService;
    private final PasswordEncoder encoder;


    public PasswordResetServiceImpl(TokenClient tokenClient, UserService userService, PasswordEncoder encoder) {
        this.tokenClient = tokenClient;
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public MessageResponse saveNewPassword(PasswordResetTokenDTO passwordResetTokenDTO, NewPasswordPutRequest request) {
        log.debug("Request to save new password.");
        UserDTO userDTO = userService.getUser(passwordResetTokenDTO.email());
        UserDTO updatedUserDTO =
                UserDTO.builder(userDTO).password(encoder.encode(request.password())).build();
        userService.save(updatedUserDTO);
        tokenClient.updatePasswordResetToken(new PasswordResetTokenDTO(
                passwordResetTokenDTO.id(),
                passwordResetTokenDTO.passwordResetToken(),
                passwordResetTokenDTO.userId(),
                passwordResetTokenDTO.userName(),
                passwordResetTokenDTO.email(),
                passwordResetTokenDTO.createDate(),
                passwordResetTokenDTO.expireDate(),
                LocalDateTime.now()));
        return new MessageResponse("Password changed successfully.");
    }
}
