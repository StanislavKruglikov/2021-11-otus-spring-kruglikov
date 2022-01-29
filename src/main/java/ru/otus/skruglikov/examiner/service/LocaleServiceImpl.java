package ru.otus.skruglikov.examiner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.provider.LocaleProvider;

@Service
@RequiredArgsConstructor
public class LocaleServiceImpl implements LocaleService {

    private final MessageSource messageSource;
    private final LocaleProvider localeProvider;

    public String getMessage(final String message, final String... arguments) {
        return messageSource.getMessage(message,arguments, localeProvider.getLocale());
    }

}
