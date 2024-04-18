package org.konradchrzanowski.utils.dictionary.util;

import org.konradchrzanowski.utils.dictionary.enumeration.Language;
import org.springframework.context.i18n.LocaleContextHolder;


import java.util.Locale;

public class LanguageUtil {

    public static Language getCurrentLanguage() {
        return prepareLanguageFrom(LocaleContextHolder.getLocale());
    }

    public static Language prepareLanguageFrom(Locale locale) {
        return Language.valueOf(locale.getLanguage().toUpperCase());
    }
}
