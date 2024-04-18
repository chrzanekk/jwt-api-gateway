package org.konradchrzanowski.email.service;


import jakarta.transaction.Transactional;
import org.konradchrzanowski.email.enumeration.MailEvent;
import org.konradchrzanowski.utils.cache.CacheType;
import org.konradchrzanowski.utils.dictionary.dto.DictionaryDTO;
import org.konradchrzanowski.utils.dictionary.enumeration.DictionaryType;
import org.konradchrzanowski.utils.dictionary.enumeration.Language;
import org.konradchrzanowski.utils.dictionary.util.LanguageUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.konradchrzanowski.utils.dictionary.enumeration.DictionaryType.EMAIL_TITLES;


@Service
@Transactional
public class DictionaryService {

    public List<DictionaryDTO> getDictionary(DictionaryType type) {
        return getDictionary(type, LanguageUtil.getCurrentLanguage());
    }

    public List<DictionaryDTO> getDictionary(DictionaryType type, Locale locale) {
        return getDictionary(type, LanguageUtil.prepareLanguageFrom(locale));
    }

    @Cacheable(CacheType.DICTIONARIES)
    public List<DictionaryDTO> getDictionary(DictionaryType type, Language language) {
        if (EMAIL_TITLES == type) {
            return getEmailTitles(language);
        }

        throw new IllegalArgumentException("Dictionary no defined: " + type + " for language: " + language);
    }


    private List<DictionaryDTO> getEmailTitles(Language lang) {
        List<DictionaryDTO> list = new ArrayList<>();

        if (Language.US == lang || Language.EN == lang) {
            list.add(DictionaryDTO.builder().code(MailEvent.AFTER_REGISTRATION.getCode()).value("Registration " +
                    "confirmation").language(lang.getCode()).build());
            list.add(DictionaryDTO.builder().code(MailEvent.AFTER_CONFIRMATION.getCode()).value("Email confirmed").language(lang.getCode()).build());
            list.add(DictionaryDTO.builder().code(MailEvent.AFTER_PASSWORD_CHANGE.getCode()).value("Password has been" +
                    " changed").language(lang.getCode()).build());
            list.add(DictionaryDTO.builder().code(MailEvent.PASSWORD_RESET.getCode()).value("Password reset")
                    .language(lang.getCode()).build());
            list.add(DictionaryDTO.builder().code(MailEvent.EMAIL_CONFIRMATION_LINK.getCode()).value("Confirm your " +
                    "email").language(lang.getCode()).build());

        } else if (Language.PL == lang) {
            list.add(DictionaryDTO.builder().code(MailEvent.AFTER_REGISTRATION.getCode())
                    .value("Potwierdzenie rejestracji").language(lang.getCode()).build());
            list.add(DictionaryDTO.builder().code(MailEvent.AFTER_CONFIRMATION.getCode()).value("Email potwierdzony").language(lang.getCode()).build());

            list.add(DictionaryDTO.builder().code(MailEvent.AFTER_PASSWORD_CHANGE.getCode())
                    .value("Hasło zostało zmienione").language(lang.getCode()).build());
            list.add(DictionaryDTO.builder().code(MailEvent.PASSWORD_RESET.getCode()).value("Resetowanie hasła")
                    .language(lang.getCode()).build());
            list.add(DictionaryDTO.builder().code(MailEvent.EMAIL_CONFIRMATION_LINK.getCode())
                    .value("Potwierdź swój email").language(lang.getCode()).build());
        }

        return list;
    }

}
