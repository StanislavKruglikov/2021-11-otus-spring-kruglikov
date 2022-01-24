package ru.otus.skruglikov.examiner.provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс DatasourceProviderCSVImplTest")
@SpringBootTest
class DatasourceProviderCSVImplTest {

    @Value(value = "classpath:${examiner.exam-data-path}.csv")
    private String testResourceDataPath;

    @Autowired
    private DatasourceProviderCSVImpl datasourceProviderCSVImpl;

    @DisplayName("корректно сздается конструктором")
    @Test
    void shouldCorrectCreateByConstructor() throws IOException {
        try (final InputStream inputStreamExpected = new DefaultResourceLoader().getResource(testResourceDataPath)
            .getInputStream();
             final InputStream inputStreamActual = datasourceProviderCSVImpl.getInputStream()) {
            assertArrayEquals(inputStreamExpected.readAllBytes(),inputStreamActual.readAllBytes());
        }
    }
}