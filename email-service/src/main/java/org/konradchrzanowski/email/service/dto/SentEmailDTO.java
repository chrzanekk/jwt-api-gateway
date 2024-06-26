package org.konradchrzanowski.email.service.dto;

import org.konradchrzanowski.email.enumeration.MailEvent;
import org.konradchrzanowski.utils.dictionary.enumeration.Language;

import java.time.LocalDateTime;

public record SentEmailDTO(
        Long id,
        Long userId,
        String userEmail,
        String title,
        String content,
        MailEvent mailEvent,
        Language language,
        LocalDateTime createDatetime
) {
}
