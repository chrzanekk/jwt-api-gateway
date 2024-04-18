package org.konradchrzanowski.email.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.konradchrzanowski.email.service.ConfirmationTokenService;
import org.konradchrzanowski.email.service.PasswordResetTokenService;
import org.konradchrzanowski.email.service.SentEmailService;
import org.konradchrzanowski.email.service.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.email.service.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.payload.response.MessageResponse;
import org.konradchrzanowski.utils.dictionary.enumeration.Language;
import org.konradchrzanowski.utils.token.TokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/sendemail")
public class SentEmailController {

    @Value("${tokenValidityTimeInMinutes}")
    private Long tokenValidityTimeInMinutes;

    private final SentEmailService sentEmailService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final ConfirmationTokenService confirmationTokenService;


    @PostMapping(path = "/after-registration")
    public ResponseEntity<MessageResponse> sendEmailAfterRegistration(UserDTO userDTO) {
        String generatedToken = confirmationTokenService.generateToken();
        ConfirmationTokenDTO confirmationTokenDTO = confirmationTokenService.saveToken(generatedToken, userDTO);
        MessageResponse messageResponse = sentEmailService.sendAfterRegistration(confirmationTokenDTO, new Locale(Language.POLISH.getCode()));
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping(path = "/after-email-confirmation")
    public ResponseEntity<MessageResponse> sendEmailAfterEmailConfirmation(@RequestBody String token) {
        ConfirmationTokenDTO confirmationTokenDTO = confirmationTokenService.getConfirmationToken(token);
        TokenUtil.validateTokenTime(confirmationTokenDTO.createDate(), tokenValidityTimeInMinutes);
        //todo need to send information that authService can confirm user if token is valid
        // so i need to change response type to boolean?
        sentEmailService.sendAfterEmailConfirmation(confirmationTokenDTO, new Locale(Language.POLISH.getCode()));
        return ResponseEntity.ok(new MessageResponse(token));

    }

    @PostMapping(path = "/password-reset")
    public ResponseEntity<MessageResponse> sendEmailPasswordReset(@RequestBody UserDTO userDTO) {
        String generatedToken = passwordResetTokenService.generate();
        PasswordResetTokenDTO passwordResetTokenDTO = passwordResetTokenService.save(generatedToken, userDTO);
        MessageResponse messageResponse = sentEmailService.sendPasswordResetMail(passwordResetTokenDTO, new Locale(Language.POLISH.getCode()));
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping(path = "/after-password-change")
    public ResponseEntity<MessageResponse> sendEmailAfterPasswordChange(@RequestBody String token) {
        PasswordResetTokenDTO passwordResetTokenDTO = passwordResetTokenService.get(token);

        TokenUtil.validateTokenTime(passwordResetTokenDTO.createDate(), tokenValidityTimeInMinutes);
        //todo below here need to send information that authService can save new password if token is valid
        sentEmailService.sendAfterPasswordChange(passwordResetTokenDTO, new Locale(Language.POLISH.getCode()));
        return ResponseEntity.ok().body(new MessageResponse("Saved"));
    }
}
