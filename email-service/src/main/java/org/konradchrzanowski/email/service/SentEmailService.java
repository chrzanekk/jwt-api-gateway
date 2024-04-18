package org.konradchrzanowski.email.service;

import org.konradchrzanowski.email.service.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.email.service.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.utils.common.payload.response.MessageResponse;

import java.util.Locale;

public interface SentEmailService {
    MessageResponse sendAfterRegistration(ConfirmationTokenDTO confirmationTokenDTO, Locale locale);

    MessageResponse sendAfterEmailConfirmation(ConfirmationTokenDTO confirmationTokenDTO, Locale locale);

    MessageResponse sendAfterPasswordChange(PasswordResetTokenDTO passwordResetTokenDTO, Locale locale);

    MessageResponse sendPasswordResetMail(PasswordResetTokenDTO passwordResetTokenDTO, Locale locale);
}
