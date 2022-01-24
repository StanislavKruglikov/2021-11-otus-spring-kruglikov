package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import ru.otus.skruglikov.examiner.config.ExaminerConfig;
import ru.otus.skruglikov.examiner.domain.JournalEntry;
import ru.otus.skruglikov.examiner.domain.Student;
import ru.otus.skruglikov.examiner.provider.InputOutputProviderConsoleImpl;
import ru.otus.skruglikov.examiner.provider.LocaleProviderAppConfigImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName("класс LocaleServiceStreamImplTest")
@SpringBootTest
class LocaleServiceStreamImplTest {

    @Mock
    private ExaminerConfig examinerConfig;
    @Mock
    private MessageSource messageSource;
    @Mock
    private InputOutputProviderConsoleImpl ioProvider;
    @Mock
    private LocaleProviderAppConfigImpl localeProviderAppConfig;

    @InjectMocks
    private LocaleServiceStreamImpl localeServiceStream;

    @DisplayName("должен корреткно отображать сообщения")
    @Test
    void shouldCorrectOutputMessage() throws IOException {
        try(final OutputStream outputStream = new ByteArrayOutputStream()) {
            final String testMessage = "TEST_MESSAGE";
            final String[] args = { "ARG1", "ARG2" };
            final String expectedMessage = String.format("%s - %s,%s",testMessage,args[0],args[1]);

            when(ioProvider.getOutput())
                .thenReturn(new PrintStream(outputStream));
            when(localeProviderAppConfig.getLocale())
                .thenReturn(Locale.getDefault());
            when(messageSource.getMessage(eq(testMessage),eq(args),eq(Locale.getDefault())))
                .thenReturn(expectedMessage);

            localeServiceStream.output(testMessage,args);
            assertEquals(expectedMessage+"\n",outputStream.toString());
        }
    }
}