package org.konradchrzanowski.token.controller;

import lombok.extern.slf4j.Slf4j;
import org.konradchrzanowski.token.service.ConfirmationTokenService;
import org.konradchrzanowski.token.service.PasswordResetTokenService;
import org.konradchrzanowski.utils.common.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.utils.common.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/api/token")
public class TokenController {

    @Value("${tokenValidityTimeInMinutes}")
    private Long tokenValidityTimeInMinutes;

    private final PasswordResetTokenService passwordResetTokenService;
    private final ConfirmationTokenService confirmationTokenService;

    public TokenController(PasswordResetTokenService passwordResetTokenService, ConfirmationTokenService confirmationTokenService) {
        this.passwordResetTokenService = passwordResetTokenService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @GetMapping(path = "/confirm/generate-token")
    public ResponseEntity<String> generateConfirmationToken() {
        log.debug("Generating confirmation token");
        return ResponseEntity.ok().body(confirmationTokenService.generate());
    }

    @PostMapping(path = "/confirm/save")
    public ResponseEntity<ConfirmationTokenDTO> saveConfirmationToken(@RequestBody String token, @RequestBody UserDTO userDTO) {
        log.debug("Saving confirmation token: {}", token);
        ConfirmationTokenDTO result = confirmationTokenService.save(token, userDTO);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping(path = "/confirm/update")
    public ResponseEntity<ConfirmationTokenDTO> updateConfirmationToken(@RequestBody ConfirmationTokenDTO confirmationTokenDTO) {
        log.debug("Updating confirmation token: {}", confirmationTokenDTO);
        ConfirmationTokenDTO result = confirmationTokenService.update(confirmationTokenDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(path = "/confirm/get-by-token")
    public ResponseEntity<ConfirmationTokenDTO> getConfirmationTokenByToken(@RequestBody String token) {
        log.debug("Getting confirmation token by token: {}", token);
        ConfirmationTokenDTO confirmationTokenDTO = confirmationTokenService.getConfirmationToken(token);
        return ResponseEntity.ok().body(confirmationTokenDTO);
    }



    @GetMapping(path = "/password-reset/generate-token")
    public ResponseEntity<String> generatePasswordResetToken() {
        log.debug("Generating password-reset token");
        return ResponseEntity.ok().body(passwordResetTokenService.generate());
    }

    @PostMapping(path = "/password-reset/save")
    public ResponseEntity<PasswordResetTokenDTO> savePasswordResetToken(@RequestBody String token, @RequestBody UserDTO userDTO) {
        log.debug("Saving password-reset token: {}", token);
        PasswordResetTokenDTO result = passwordResetTokenService.save(token, userDTO);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping(path = "/password-reset/update")
    public ResponseEntity<PasswordResetTokenDTO> updatePasswordResetToken(@RequestBody PasswordResetTokenDTO confirmationTokenDTO) {
        log.debug("Updating password-reset token: {}", confirmationTokenDTO);
        PasswordResetTokenDTO result = passwordResetTokenService.update(confirmationTokenDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(path = "/password-reset/get-by-token")
    public ResponseEntity<PasswordResetTokenDTO> getPasswordResetByToken(@RequestBody String token) {
        log.debug("Getting password-reset token by token: {}", token);
        PasswordResetTokenDTO passwordResetTokenDTO = passwordResetTokenService.getPasswordResetToken(token);
        return ResponseEntity.ok().body(passwordResetTokenDTO);
    }
}
