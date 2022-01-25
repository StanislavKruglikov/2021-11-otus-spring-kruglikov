package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import ru.otus.skruglikov.examiner.provider.LocaleProviderAppConfigImpl;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName("класс LocaleServiceImpl")
@SpringBootTest
class LocaleServiceImplTest {

    @Mock
    private MessageSource messageSource;
    @Mock
    private LocaleProviderAppConfigImpl localeProvider;

    @InjectMocks
    private LocaleServiceImpl localeService;

    @DisplayName("должен корреткно отображать сообщения")
    @Test
    void shouldCorrectOutputMessage() {
        final String testMessageCanada = "TEST_MESSAGE_CN";
        final String testMessageEnglish = "TEST_MESSAGE_EN";
        final String[] args = { "ARG1", "ARG2" };
        final String expectedMessageCanada = String.format("%s - %s,%s",testMessageCanada,args[0],args[1]);
        final String expectedMessageEnglish = String.format("%s - %s,%s",testMessageEnglish,args[0],args[1]);
        when(messageSource.getMessage(eq(testMessageCanada),eq(args),eq(Locale.CANADA)))
            .thenReturn(expectedMessageCanada);
        when(messageSource.getMessage(eq(testMessageEnglish),eq(args),eq(Locale.ENGLISH)))
            .thenReturn(expectedMessageEnglish);
        when(localeProvider.getLocale())
            .thenReturn(Locale.CANADA)
            .thenReturn(Locale.ENGLISH);
        final String actualMessageCanada = localeService.getMessage(testMessageCanada,args[0],args[1]);
        final String actualMessageEnglish = localeService.getMessage(testMessageEnglish,args[0],args[1]);
        assertEquals(expectedMessageCanada,actualMessageCanada);
        assertEquals(expectedMessageEnglish,actualMessageEnglish);
    }
}