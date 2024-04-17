package org.konradchrzanowski.email.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.konradchrzanowski.email.exception.EmailSendFailException;
import org.konradchrzanowski.email.service.dto.SentEmailDTO;
import org.konradchrzanowski.email.service.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class EmailSenderServiceImpl implements EmailSenderService {

    @Value("${platform.replyToEmail}")
    private String replyToEmail;

    @Value("${platform.fromEmail}")
    private String fromEmail;
    private static final Logger log = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

    private final JavaMailSender javaMailSender;


    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(SentEmailDTO sentEmailDTO) {
        log.debug("Sending email to: {}", sentEmailDTO.userEmail());
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(sentEmailDTO.userEmail());
            helper.setReplyTo(replyToEmail);
            helper.setFrom(fromEmail);
            helper.setSubject(sentEmailDTO.title());
            helper.setText(sentEmailDTO.content(), true);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new EmailSendFailException("Failed to send email");
        }

        sendMail(mail);

    }

    private void sendMail(MimeMessage mail) {
        new Thread(() -> {
            javaMailSender.send(mail);
        }).start();
    }

}
