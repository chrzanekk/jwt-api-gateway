package org.konradchrzanowski.email.service;

import org.konradchrzanowski.email.service.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.email.service.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.utils.common.payload.response.SentEmailResponse;

import java.util.Locale;

public interface SentEmailService {
    SentEmailResponse sendAfterRegistration(ConfirmationTokenDTO confirmationTokenDTO, Locale locale);

    SentEmailResponse sendAfterEmailConfirmation(ConfirmationTokenDTO confirmationTokenDTO, Locale locale);

    SentEmailResponse sendAfterPasswordChange(PasswordResetTokenDTO passwordResetTokenDTO, Locale locale);

    SentEmailResponse sendPasswordResetMail(PasswordResetTokenDTO passwordResetTokenDTO, Locale locale);
}
