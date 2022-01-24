package ru.otus.skruglikov.examiner.provider;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.otus.skruglikov.examiner.config.ExaminerConfig;

import java.util.Locale;

@Component
@Data
public class LocaleProviderAppConfigImpl implements LocaleProvider {
    private final Locale locale;

    public LocaleProviderAppConfigImpl(final ExaminerConfig config) {
        locale = config.getLocale();
    }

}
