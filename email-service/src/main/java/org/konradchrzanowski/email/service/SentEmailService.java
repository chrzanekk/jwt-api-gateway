package org.konradchrzanowski.email.service;

import org.konradchrzanowski.email.payload.response.MessageResponse;
import org.konradchrzanowski.email.service.dto.ConfirmationToken;
import org.konradchrzanowski.email.service.dto.PasswordResetToken;
import java.util.Locale;

public interface SentEmailService {
    MessageResponse sendAfterRegistration(ConfirmationToken confirmationToken, Locale locale);

    MessageResponse sendAfterEmailConfirmation(ConfirmationToken confirmationToken, Locale locale);

    MessageResponse sendAfterPasswordChange(PasswordResetToken passwordResetToken, Locale locale);

    MessageResponse sendPasswordResetMail(PasswordResetToken passwordResetToken, Locale locale);
}
