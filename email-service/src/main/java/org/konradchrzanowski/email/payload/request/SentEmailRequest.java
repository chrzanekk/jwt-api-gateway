package org.konradchrzanowski.email.payload.request;

import java.util.Locale;

public record SentEmailRequest(
        String email,
        String userName,
        Long userId,
        String confirmationToken,
        Locale locale
) {
}
