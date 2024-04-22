package org.konradchrzanowski.email.service.impl;

import org.konradchrzanowski.email.domain.SentEmail;
import org.konradchrzanowski.email.enumeration.MailEvent;
import org.konradchrzanowski.email.mapper.SentEmailMapper;
import org.konradchrzanowski.email.repository.SentEmailRepository;
import org.konradchrzanowski.email.service.DictionaryService;
import org.konradchrzanowski.email.service.EmailSenderService;
import org.konradchrzanowski.email.service.SentEmailService;
import org.konradchrzanowski.email.service.dto.ConfirmationTokenDTO;
import org.konradchrzanowski.email.service.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.email.service.dto.SentEmailDTO;
import org.konradchrzanowski.utils.common.payload.response.SentEmailResponse;
import org.konradchrzanowski.utils.dictionary.dto.DictionaryDTO;
import org.konradchrzanowski.utils.dictionary.enumeration.DictionaryType;
import org.konradchrzanowski.utils.dictionary.enumeration.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class SentEmailServiceImpl implements SentEmailService {

    @Value("${tokenValidityTimeInMinutes}")
    private Long tokenValidityTimeInMinutes;

    @Value("${platform.url}")
    private String scaffoldingAppUrl;

    private final Logger log = LoggerFactory.getLogger(SentEmailServiceImpl.class);

    private static final String API_PATH = "/api/auth";
    private static final String LOGIN_PAGE_URL = "loginPageUrl";

    private final EmailSenderService emailSenderService;
    private final SentEmailRepository sentEmailRepository;
    private final DictionaryService dictionaryService;
    private final TemplateEngine templateEngine;
    private final SentEmailMapper sentEmailMapper;

    public SentEmailServiceImpl(EmailSenderService emailSenderService,
                                SentEmailRepository sentEmailRepository,
                                DictionaryService dictionaryService,
                                TemplateEngine templateEngine,
                                SentEmailMapper sentEmailMapper) {
        this.emailSenderService = emailSenderService;
        this.sentEmailRepository = sentEmailRepository;
        this.dictionaryService = dictionaryService;
        this.templateEngine = templateEngine;
        this.sentEmailMapper = sentEmailMapper;
    }

    @Override
    public SentEmailResponse sendAfterRegistration(ConfirmationTokenDTO confirmationTokenDTO, Locale locale) {
        log.debug("Request to send email to confirm user registration: {}", confirmationTokenDTO.email());
        Context context = new Context(locale);
        context.setVariable(LOGIN_PAGE_URL, scaffoldingAppUrl + "/account/login");
        context.setVariable("emailConfirmationLink",
                scaffoldingAppUrl + "/confirm?token=" + confirmationTokenDTO.confirmationToken());
        context.setVariable("tokenValidityTime", tokenValidityTimeInMinutes);
        //template to send as string
        String content = templateEngine.process("mail-after-registration", context);
        String title = chooseTitle(MailEvent.AFTER_REGISTRATION, locale);

        SentEmailDTO emailDTO = new SentEmailDTO(
                null,
                confirmationTokenDTO.userId(),
                confirmationTokenDTO.email(),
                title,
                content,
                MailEvent.AFTER_REGISTRATION,
                Language.from(locale.getLanguage()),
                LocalDateTime.now());


        emailSenderService.sendEmail(emailDTO);
        SentEmail sentEmail = sentEmailMapper.toEntity(emailDTO);

        sentEmailRepository.save(sentEmail);
        return new SentEmailResponse("Register successful", true);
    }

    @Override
    public SentEmailResponse sendAfterEmailConfirmation(ConfirmationTokenDTO confirmationTokenDTO, Locale locale) {
        log.debug("Request to send email to confirm user activation:");
        Context context = new Context(locale);
        context.setVariable(LOGIN_PAGE_URL, scaffoldingAppUrl + "/account/login");
        //template to send as string
        String content = templateEngine.process("mail-after-confirmation", context);
        String title = chooseTitle(MailEvent.AFTER_CONFIRMATION, locale);
        SentEmailDTO emailDTO = new SentEmailDTO(
                null,
                confirmationTokenDTO.userId(),
                confirmationTokenDTO.email(),
                title,
                content,
                MailEvent.AFTER_CONFIRMATION,
                Language.from(locale.getLanguage()),
                LocalDateTime.now());

        emailSenderService.sendEmail(emailDTO);
        SentEmail sentEmail = sentEmailMapper.toEntity(emailDTO);
        sentEmailRepository.save(sentEmail);
        return new SentEmailResponse("Register successful", true);
    }

    @Override
    public SentEmailResponse sendPasswordResetMail(PasswordResetTokenDTO passwordResetTokenDTO, Locale locale) {
        log.debug("Request to send email to reset password");
        Context context = new Context(locale);
        context.setVariable(LOGIN_PAGE_URL, scaffoldingAppUrl + "/account/login");
        context.setVariable("passwordResetLink",
                scaffoldingAppUrl + "/account/password-reset?token=" + passwordResetTokenDTO.passwordResetToken());
        context.setVariable("tokenValidityTime", tokenValidityTimeInMinutes);
        String content = templateEngine.process("mail-password-reset", context);
        String title = chooseTitle(MailEvent.PASSWORD_RESET, locale);

        SentEmailDTO emailDTO = new SentEmailDTO(
                null,
                passwordResetTokenDTO.userId(),
                passwordResetTokenDTO.email(),
                title,
                content,
                MailEvent.PASSWORD_RESET,
                Language.from(locale.getLanguage()),
                LocalDateTime.now());

        emailSenderService.sendEmail(emailDTO);
        SentEmail sentEmail = sentEmailMapper.toEntity(emailDTO);
        sentEmailRepository.save(sentEmail);
        return new SentEmailResponse("Password reset token sent with token: " + passwordResetTokenDTO.passwordResetToken(), true);
    }

    @Override
    public SentEmailResponse sendAfterPasswordChange(PasswordResetTokenDTO passwordResetTokenDTO, Locale locale) {
        log.debug("Request to send email to confirm password reset.");
        Context context = new Context(locale);
        context.setVariable(LOGIN_PAGE_URL, scaffoldingAppUrl + "/account/login");
        //template to send as string
        String content = templateEngine.process("mail-after-password-change", context);
        String title = chooseTitle(MailEvent.AFTER_PASSWORD_CHANGE, locale);
        SentEmailDTO emailDTO = new SentEmailDTO(
                null,
                passwordResetTokenDTO.userId(),
                passwordResetTokenDTO.email(),
                title,
                content,
                MailEvent.AFTER_PASSWORD_CHANGE,
                Language.from(locale.getLanguage()),
                LocalDateTime.now());

        emailSenderService.sendEmail(emailDTO);
        SentEmail sentEmail = sentEmailMapper.toEntity(emailDTO);
        sentEmailRepository.save(sentEmail);
        return new SentEmailResponse("Password changed successfully", true);
    }

    private String chooseTitle(MailEvent mailEvent, Locale locale) {

        List<DictionaryDTO> list = dictionaryService.getDictionary(DictionaryType.EMAIL_TITLES, locale);

        for (DictionaryDTO dictionaryData : list) {
            if (dictionaryData.code().equals(mailEvent.getCode())) {
                return dictionaryData.value();
            }
        }

        throw new IllegalArgumentException("No email title!");

    }
}
