package ru.otus.skruglikov.examiner.provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс InputOutputProviderConsoleImplTest")
@SpringBootTest
class InputOutputProviderConsoleImplTest {

    @Autowired
    private InputOutputProviderConsoleImpl inputOutputProviderConsole;

    @DisplayName("объект корректно инициализируется в конструкторе")
    @Test
    void shouldCorrectInitializeByConstructor() {
        assertAll("providerConsoleText",
                () -> assertEquals(System.in,inputOutputProviderConsole.getInputStream()),
                () -> assertEquals(System.out,inputOutputProviderConsole.getPrintStream())
        );
    }

}