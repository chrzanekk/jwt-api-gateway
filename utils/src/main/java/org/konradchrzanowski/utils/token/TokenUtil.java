package org.konradchrzanowski.utils.token;

import org.konradchrzanowski.utils.common.payload.response.SentEmailResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TokenUtil {

    public static SentEmailResponse validateTokenTime(LocalDateTime createDate, Long tokenValidityInMinutes) {
        if (LocalDateTime.now().isAfter(createDate.plusMinutes(tokenValidityInMinutes))) {
            return new SentEmailResponse("Token expired", false);
        } else {
            return new SentEmailResponse("Token valid", true);
        }
    }
}
