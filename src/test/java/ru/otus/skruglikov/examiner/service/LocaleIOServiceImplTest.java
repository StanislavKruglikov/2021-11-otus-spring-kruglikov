package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@DisplayName("класс LocaleIOServiceImpl")
@SpringBootTest
class LocaleIOServiceImplTest {

    @MockBean
    private IOServiceImpl ioService;
    @MockBean
    private LocaleServiceImpl localeService;

    @Autowired
    private LocaleIOServiceImpl localeIOService;

    private final String testData = "TEST DATA";

    @DisplayName("должен корректно выводить данные")
    @Test
    void shouldCorrectWrite() {
        doAnswer(invocation -> {
            assertEquals(testData,invocation.getArgument(0));
            return null;
        })
            .when(ioService)
            .write(testData);
        localeIOService.write(testData);
    }

    @DisplayName("должен корректно читать данные")
    @Test
    void shouldCorrectRead() {
        when(ioService.read())
            .thenReturn(testData);
        final String testDataActual = localeIOService.read();
        assertEquals(testData,testDataActual);
    }

    @DisplayName("должен корректно выводить сообщения")
    @Test
    void shouldCorrectWriteMessage() {
        final String arg1 = "ARG1";
        final String testMessage = String.format("%s_%s",testData,arg1);
        when(localeService.getMessage(testData,arg1))
            .thenReturn(testMessage);
        doAnswer(invocation -> {
            assertEquals(testMessage,invocation.getArgument(0));
            return null;
        })
            .when(ioService)
            .write(testMessage);
        localeIOService.writeMessage(testData,arg1);
    }
}