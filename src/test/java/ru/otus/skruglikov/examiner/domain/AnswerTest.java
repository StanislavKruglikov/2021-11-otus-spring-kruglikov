package ru.otus.skruglikov.examiner.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Answer")
class AnswerTest {

    private final static String ANSWER_TEXT = "TestAnswer";
    private final static boolean ANSWER_CORRECT_SW = true;

    @DisplayName("корректно создается конструктором")
    @Test
    void shouldHaveCorrectConstructor() {

        final boolean answerCorrectSw = true;
        final Answer answer = new Answer(ANSWER_TEXT, answerCorrectSw);
        assertAll("answerConstructor",
            () -> assertEquals(ANSWER_TEXT, answer.getText()),
            () -> assertEquals(ANSWER_CORRECT_SW, answer.isCorrect())
        );

    }

    @DisplayName("корректно сравниваются на равенство")
    @Test
    void shouldHaveCorrectEquals() {
        assertEquals(new Answer(ANSWER_TEXT, ANSWER_CORRECT_SW),
            new Answer(ANSWER_TEXT, ANSWER_CORRECT_SW));

    }

    @DisplayName("корректно сравниваются на не равенство")
    @Test
    void shouldHaveCorrectNotEquals() {
        final Answer answer = new Answer(ANSWER_TEXT, ANSWER_CORRECT_SW);
        assertAll("answerNotEquals",
            () -> assertNotEquals(answer, new Answer(ANSWER_TEXT, !ANSWER_CORRECT_SW)),
            () -> assertNotEquals(answer, new Answer(ANSWER_TEXT + "!", ANSWER_CORRECT_SW))
        );

    }
}