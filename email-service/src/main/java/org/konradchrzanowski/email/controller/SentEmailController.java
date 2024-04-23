package org.konradchrzanowski.email.controller;

import lombok.extern.slf4j.Slf4j;
import org.konradchrzanowski.clients.token.TokenClient;
import org.konradchrzanowski.email.service.SentEmailService;
import org.konradchrzanowski.utils.common.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.utils.common.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.payload.response.SentEmailResponse;
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
@RestController
@RequestMapping(path = "/api/sendemail")
public class SentEmailController {

    @Value("${tokenValidityTimeInMinutes}")
    private Long tokenValidityTimeInMinutes;

    private final SentEmailService sentEmailService;
    private final TokenClient tokenClient;

    public SentEmailController(SentEmailService sentEmailService, TokenClient tokenClient) {
        this.sentEmailService = sentEmailService;
        this.tokenClient = tokenClient;
    }


    @PostMapping(path = "/after-registration")
    public ResponseEntity<SentEmailResponse> sendEmailAfterRegistration(@RequestBody UserDTO userDTO) {
        String generatedToken = tokenClient.generateConfirmationToken().getBody();
        ConfirmationTokenDTO confirmationTokenDTO = tokenClient.saveConfirmationToken(generatedToken, userDTO).getBody();
        SentEmailResponse messageResponse = sentEmailService.sendAfterRegistration(confirmationTokenDTO, new Locale(Language.POLISH.getCode()));
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping(path = "/after-email-confirmation")
    public ResponseEntity<SentEmailResponse> sendEmailAfterEmailConfirmation(@RequestBody String token) {
        ConfirmationTokenDTO confirmationTokenDTO = tokenClient.getConfirmationTokenByToken(token).getBody();
        assert confirmationTokenDTO != null;
        SentEmailResponse validationResult = TokenUtil.validateTokenTime(confirmationTokenDTO.createDate(), tokenValidityTimeInMinutes);
        if (!validationResult.isSentEmail()) {
            return ResponseEntity.ok(validationResult);
        } else {
            SentEmailResponse message = sentEmailService.sendAfterEmailConfirmation(confirmationTokenDTO, new Locale(Language.POLISH.getCode()));
            return ResponseEntity.ok(message);
        }
    }

    @PostMapping(path = "/password-reset")
    public ResponseEntity<SentEmailResponse> sendEmailPasswordReset(@RequestBody UserDTO userDTO) {
        String generatedToken = tokenClient.generatePasswordResetToken().getBody();
        PasswordResetTokenDTO passwordResetTokenDTO = tokenClient.savePasswordResetToken(generatedToken, userDTO).getBody();
        SentEmailResponse messageResponse = sentEmailService.sendPasswordResetMail(passwordResetTokenDTO, new Locale(Language.POLISH.getCode()));
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping(path = "/after-password-change")
    public ResponseEntity<SentEmailResponse> sendEmailAfterPasswordChange(@RequestBody String token) {
        PasswordResetTokenDTO passwordResetTokenDTO = tokenClient.getPasswordResetByToken(token).getBody();
        assert passwordResetTokenDTO != null;
        SentEmailResponse validationResult = TokenUtil.validateTokenTime(passwordResetTokenDTO.createDate(), tokenValidityTimeInMinutes);
        if (!validationResult.isSentEmail()) {
            return ResponseEntity.ok(validationResult);
        } else {
            SentEmailResponse message = sentEmailService.sendAfterPasswordChange(passwordResetTokenDTO, new Locale(Language.POLISH.getCode()));
            return ResponseEntity.ok().body(message);
        }
    }
}
