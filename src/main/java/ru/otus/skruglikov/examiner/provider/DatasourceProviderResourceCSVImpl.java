package ru.otus.skruglikov.examiner.provider;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Component;
import ru.otus.skruglikov.examiner.config.DataPathConfig;

import java.io.IOException;
import java.io.InputStream;

@Component
public class DatasourceProviderResourceCSVImpl implements DatasourceProvider {

    private final String resourcePath;

    public DatasourceProviderResourceCSVImpl(final DataPathConfig config) {
        this.resourcePath = "classpath:"+config.getDataPath() + ".csv";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new DefaultResourceLoader()
            .getResource(resourcePath)
            .getInputStream();
    }
}
