package org.konradchrzanowski.token.service;

import org.konradchrzanowski.utils.common.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;

public interface PasswordResetTokenService {

    String generate();

    PasswordResetTokenDTO save(String token, UserDTO userDTO);

    PasswordResetTokenDTO update(PasswordResetTokenDTO passwordResetTokenDTO);

    PasswordResetTokenDTO getPasswordResetToken(String token);

    void delete(Long id);
}
