package ru.otus.skruglikov.examiner.provider;

import lombok.Getter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Component;
import ru.otus.skruglikov.examiner.config.ExaminerConfig;

import java.io.IOException;
import java.io.InputStream;

@Component
public class DatasourceProviderCSVImpl implements DatasourceProvider {

    @Getter
    private final String resourcePath;

    public DatasourceProviderCSVImpl(final ExaminerConfig config) {
        this.resourcePath = "classpath:"+config.getExamDataPath() + ".csv";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new DefaultResourceLoader()
            .getResource(resourcePath)
            .getInputStream();
    }
}
