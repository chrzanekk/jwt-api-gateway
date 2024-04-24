package org.konradchrzanowski.clients.email;


import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.payload.response.SentEmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "EMAIL-SERVICE",
url = "${clients.email.url}")
@RequestMapping(path = "/api/sendemail")
public interface EmailClient {

    @PostMapping(path = "/after-registration")
    ResponseEntity<SentEmailResponse> sendEmailAfterRegistration(@RequestBody UserDTO userDTO);

    @PostMapping(path = "/after-email-confirmation")
    ResponseEntity<SentEmailResponse> sendEmailAfterEmailConfirmation(@RequestBody String token);

    @PostMapping(path = "/password-reset")
    ResponseEntity<SentEmailResponse> sendPasswordReset(@RequestBody UserDTO userDTO);

    @PostMapping(path = "/after-password-change")
    public ResponseEntity<SentEmailResponse> sendPasswordChange(@RequestBody String token);

}
