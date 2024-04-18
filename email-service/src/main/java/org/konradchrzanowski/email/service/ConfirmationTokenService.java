package org.konradchrzanowski.email.service;

import org.konradchrzanowski.email.service.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;


public interface ConfirmationTokenService {

    String generateToken();

    ConfirmationTokenDTO saveToken(String token, UserDTO userDTO);

    ConfirmationTokenDTO updateToken(ConfirmationTokenDTO confirmationTokenDTO);

    ConfirmationTokenDTO getConfirmationToken(String token);

    void deleteConfirmationToken(Long id);
}
