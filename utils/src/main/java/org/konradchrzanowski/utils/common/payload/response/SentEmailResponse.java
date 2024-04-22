package org.konradchrzanowski.utils.common.payload.response;

public record SentEmailResponse(
        String message,
        Boolean isSentEmail
) {
}
