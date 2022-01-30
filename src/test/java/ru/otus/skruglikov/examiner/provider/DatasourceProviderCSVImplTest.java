package ru.otus.skruglikov.examiner.provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import ru.otus.skruglikov.examiner.config.ExaminerConfig;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс DatasourceProviderCSVImplTest")
@SpringBootTest
class DatasourceProviderCSVImplTest {

    @Value(value = "classpath:${examiner.exam-data-path}_ru_RU.csv")
    private String testResourceDataPath;

    @Autowired
    private ExaminerConfig config;
    @Autowired
    private DatasourceProviderResourceCSVImpl datasourceProviderResourceCSVImpl;

    @DisplayName("корректно сздается конструктором с учетом локали из конфигурации")
    @Test
    void shouldCorrectCreateByConstructorWithConfigLocale() throws IOException {
        try (final InputStream inputStreamExpected = new DefaultResourceLoader().getResource(testResourceDataPath)
            .getInputStream();
             final InputStream inputStreamActual = datasourceProviderResourceCSVImpl.getInputStream()) {
            assertArrayEquals(inputStreamExpected.readAllBytes(),inputStreamActual.readAllBytes());
        }
    }
}