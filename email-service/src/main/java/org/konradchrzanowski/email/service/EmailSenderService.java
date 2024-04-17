package org.konradchrzanowski.email.service;

import org.konradchrzanowski.email.service.dto.SentEmailDTO;

public interface EmailSenderService {

    void sendEmail(SentEmailDTO sentEmail);
}
