package ru.otus.skruglikov.examiner.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.provider.LocaleProvider;
import ru.otus.skruglikov.examiner.provider.OutputProvider;

import java.io.PrintStream;

@Service
public class LocaleServiceStreamImpl implements LocaleService {

    private final MessageSource messageSource;
    private final LocaleProvider localeProvider;
    private final OutputProvider<PrintStream> outputProvider;

    public LocaleServiceStreamImpl(final MessageSource messageSource, final LocaleProvider localeProvider,
                                   final OutputProvider<PrintStream> ioProvider) {
        this.messageSource = messageSource;
        this.localeProvider = localeProvider;
        this.outputProvider = ioProvider;
    }

    public void output(final String message,final String... arguments) {
        outputProvider.getOutput().println(messageSource.getMessage(message,arguments, localeProvider.getLocale()));
    }
}
