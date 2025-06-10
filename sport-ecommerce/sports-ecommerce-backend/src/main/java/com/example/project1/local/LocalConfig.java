package com.example.project1.local;

import com.example.project1.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocalConfig {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver; // lay doi tuong local
    private final TokenUtil tokenUtil;

    public String getlanguage(String keyMessage) {
        HttpServletRequest request = TokenUtil.getCurrentRequest();
        String language = tokenUtil.getLanguage();
        Locale locale = localeResolver.resolveLocale(request);

        return messageSource.getMessage(keyMessage, null, locale);
    }

}
