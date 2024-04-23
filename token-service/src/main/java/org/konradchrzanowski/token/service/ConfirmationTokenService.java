package org.konradchrzanowski.token.service;

import org.konradchrzanowski.utils.common.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;


public interface ConfirmationTokenService {

    String generateToken();

    ConfirmationTokenDTO saveToken(String token, UserDTO userDTO);

    ConfirmationTokenDTO updateToken(ConfirmationTokenDTO confirmationTokenDTO);

    ConfirmationTokenDTO getConfirmationToken(String token);

    void deleteConfirmationToken(Long id);
}
