package org.konradchrzanowski.clients.token;

import org.konradchrzanowski.utils.common.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.utils.common.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "TOKEN-SERVICE",
        url = "${clients.token.url}")
public interface TokenClient {

    @GetMapping(path = "/api/token/confirm/generate-token")
    public ResponseEntity<String> generateConfirmationToken();

    @PostMapping(path = "/api/token/confirm/save")
    public ResponseEntity<ConfirmationTokenDTO> saveConfirmationToken(@RequestBody String token, @RequestBody UserDTO userDTO);

    @PutMapping(path = "/api/token/confirm/update")
    public ResponseEntity<ConfirmationTokenDTO> updateConfirmationToken(@RequestBody ConfirmationTokenDTO confirmationTokenDTO);

    @GetMapping(path = "/api/token/confirm/get-by-token")
    public ResponseEntity<ConfirmationTokenDTO> getConfirmationTokenByToken(@RequestBody String token);


    @GetMapping(path = "/api/token//password-reset/generate-token")
    public ResponseEntity<String> generatePasswordResetToken();

    @PostMapping(path = "/api/token/password-reset/save")
    public ResponseEntity<PasswordResetTokenDTO> savePasswordResetToken(@RequestBody String token, @RequestBody UserDTO userDTO);

    @PutMapping(path = "/api/token/password-reset/update")
    public ResponseEntity<PasswordResetTokenDTO> updatePasswordResetToken(@RequestBody PasswordResetTokenDTO confirmationTokenDTO);

    @GetMapping(path = "/api/token/password-reset/get-by-token")
    public ResponseEntity<PasswordResetTokenDTO> getPasswordResetByToken(@RequestBody String token);
}
