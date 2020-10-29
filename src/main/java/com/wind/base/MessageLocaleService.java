package com.wind.base;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageLocaleService {

    private final MessageSource messageSource;

    public MessageLocaleService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String messageId, Object... params) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageId, params, locale);
    }
}
