package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.skruglikov.examiner.provider.InputOutputProviderConsoleImpl;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@DisplayName("класс IOServiceImpl")
@SpringBootTest
class IOServiceImplTest {

    @Mock
    private InputOutputProviderConsoleImpl ioProvider;

    @InjectMocks
    private IOServiceImpl ioService;

    @DisplayName("должен корректно отправлять данные")
    @Test
    void shouldCorrectWrite() throws IOException {
        final String testData = "test data";
        try(final OutputStream os = new ByteArrayOutputStream()) {
            when(ioProvider.getOutput())
                .thenReturn(new PrintStream(os));
            ioService.write(testData);
            assertEquals(testData+"\n",os.toString());
        }
    }

    @DisplayName("должен корректно читать данные")
    @Test
    void shouldCorrectRead() throws IOException {
        final String testData = "test data";
        try(final InputStream is = new ByteArrayInputStream((testData+"\n").getBytes(StandardCharsets.UTF_8))) {
            when(ioProvider.getInput())
                .thenReturn(is);
            final String testDataActual = ioService.read();
            assertEquals(testData,testDataActual);
        }
    }
}