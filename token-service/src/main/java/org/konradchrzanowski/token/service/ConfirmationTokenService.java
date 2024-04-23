package org.konradchrzanowski.token.service;

import org.konradchrzanowski.utils.common.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;


public interface ConfirmationTokenService {

    String generate();

    ConfirmationTokenDTO save(String token, UserDTO userDTO);

    ConfirmationTokenDTO update(ConfirmationTokenDTO confirmationTokenDTO);

    ConfirmationTokenDTO getConfirmationToken(String token);

    void deleteConfirmationToken(Long id);
}
