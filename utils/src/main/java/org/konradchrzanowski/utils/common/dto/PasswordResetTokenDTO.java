package org.konradchrzanowski.utils.common.dto;

import java.time.LocalDateTime;

public record PasswordResetTokenDTO(
        Long id,
        String passwordResetToken,
        Long userId,
        String userName,
        String email,
        LocalDateTime createDate,
        LocalDateTime expireDate,
        LocalDateTime confirmDat
) {
}
