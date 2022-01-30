package ru.otus.skruglikov.examiner.provider;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.otus.skruglikov.examiner.config.LocaleConfig;

import java.util.Locale;

@Component
public class LocaleProviderAppConfigImpl implements LocaleProvider {
    @Getter
    private final Locale locale;

    public LocaleProviderAppConfigImpl(final LocaleConfig config) {
        locale = config.getLocale();
    }

}
