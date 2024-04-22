package org.konradchrzanowski.utils.common.dto;

import java.time.LocalDateTime;

public record ConfirmationTokenDTO(
        Long id,
        String confirmationToken,
        Long userId,
        String userName,
        String email,
        LocalDateTime createDate,
        LocalDateTime expireDate,
        LocalDateTime confirmDate
) {
}
