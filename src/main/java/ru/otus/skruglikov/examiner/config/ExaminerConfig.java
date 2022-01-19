package ru.otus.skruglikov.examiner.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;

@ConfigurationProperties(prefix = "examiner")
@Component
@Data
public class ExaminerConfig {
    private int scoreExamPass;
    private Locale locale;
    private String examDataPath;

    public void setLocale(String languageTag) {
        locale = Locale.forLanguageTag(languageTag);
    }

    public String getExamDataPath() {
        final StringBuilder stringBuilder = new StringBuilder(this.examDataPath);
        if(locale != null) {
            stringBuilder
                .append("_")
                .append(locale.toLanguageTag().replace("-","_"));
        }
        return stringBuilder.toString();
    }
}