package org.konradchrzanowski.email.service;


import org.konradchrzanowski.email.service.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.utils.common.dto.UserDTO;

public interface PasswordResetTokenService {

    String generate();

    PasswordResetTokenDTO save(String token, UserDTO userDTO);

    PasswordResetTokenDTO update(PasswordResetTokenDTO passwordResetTokenDTO);

    PasswordResetTokenDTO get(String token);

    void delete(Long id);
}
