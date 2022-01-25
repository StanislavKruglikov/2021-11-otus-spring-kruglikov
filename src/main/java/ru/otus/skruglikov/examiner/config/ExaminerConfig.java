package ru.otus.skruglikov.examiner.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;

@ConfigurationProperties(prefix = "examiner")
@Component
@Data
public class ExaminerConfig implements DataPathConfig, ExamScorePassConfig, LocaleConfig {
    private int examScorePass;
    private Locale locale;
    private String examDataPath;

    public void setLocale(String languageTag) {
        locale = Locale.forLanguageTag(languageTag);
    }

    @Override
    public String getDataPath() {
        final StringBuilder stringBuilder = new StringBuilder(this.examDataPath);
        if(locale != null) {
            stringBuilder
                .append("_")
                .append(locale.toLanguageTag().replace("-","_"));
        }
        return stringBuilder.toString();
    }
}