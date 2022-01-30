package ru.otus.skruglikov.examiner.provider;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.otus.skruglikov.examiner.config.DataPathConfig;
import ru.otus.skruglikov.examiner.config.LocaleConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

@Component
public class DatasourceProviderResourceCSVImpl implements DatasourceProvider {

    private final String resourcePath;

    public DatasourceProviderResourceCSVImpl(final DataPathConfig config, final LocaleConfig localeConfig) {
        this.resourcePath = getFullPath(config.getDataPath(),localeConfig.getLocale());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new DefaultResourceLoader()
            .getResource(resourcePath)
            .getInputStream();
    }

    private String getFullPath(final String dataPath,final Locale locale) {
        String resourceName;
        if(locale != null && validateDataPath(dataPath,locale)) {
            resourceName = generateResourcePath(dataPath,locale);
        } else if(validateDataPath(dataPath,Locale.getDefault())) {
            resourceName = generateResourcePath(dataPath,Locale.getDefault());
        } else {
            resourceName = generateResourcePath(dataPath,null);
        }
        return resourceName;
    }

    private boolean validateDataPath(final String dataPath,final Locale locale) {
        boolean result;
        try {
            ResourceUtils.getFile(generateResourcePath(dataPath,locale));
            result = true;
        } catch (FileNotFoundException e) {
            result = false;
        }
        return result;
    }

    private String generateResourcePath(final String dataPath, final Locale locale) {
        return locale != null ?
            String.format("classpath:%s_%s.csv",dataPath,locale.toLanguageTag().replace("-","_")) :
            String.format("classpath:%s.csv",dataPath);
    }
}
