package ru.otus.skruglikov.examiner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import ru.otus.skruglikov.examiner.provider.InputOutputProvider;
import ru.otus.skruglikov.examiner.provider.LocaleProvider;

import java.util.Locale;

@RequiredArgsConstructor
public abstract class ServiceMessageAwareAbstract {
    protected final MessageSource messageSource;
    protected final Locale locale;
    protected final InputOutputProvider ioProvider;

    public ServiceMessageAwareAbstract(final MessageSource messageSource, final LocaleProvider localeProvider,
                                       final InputOutputProvider ioProvider) {
        this.messageSource = messageSource;
        this.locale = localeProvider.getLocale();
        this.ioProvider = ioProvider;
    }
}
