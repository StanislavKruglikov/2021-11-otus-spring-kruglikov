package ru.otus.skruglikov.examiner.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Question")
class QuestionTest {

    private final static String QUESTION_TEXT = "TestQuestion";
    private final static int QUESTION_WEIGHT = 100;
    private final static int QUESTION_ORDER = 100;

    @DisplayName("корректно создается конструктором")
    @Test
    void shouldHaveCorrectConstructor() {
        final Question question = new Question(QUESTION_ORDER, QUESTION_WEIGHT, QUESTION_TEXT);
        assertAll("questionConstructor",
            () -> assertEquals(QUESTION_TEXT, question.getText()),
            () -> assertEquals(QUESTION_WEIGHT, question.getWeight()),
            () -> assertEquals(QUESTION_ORDER, question.getNumberOrder())
        );

    }

    @DisplayName("корректно сравниваются на равенство")
    @Test
    void shouldHaveCorrectEquals() {
        final Question question = new Question(QUESTION_ORDER, QUESTION_WEIGHT, QUESTION_TEXT);
        final Question question2 = new Question(QUESTION_ORDER, QUESTION_WEIGHT, QUESTION_TEXT);
        assertEquals(question, question2);
    }

    @DisplayName("корректно сравниваются на не равенство")
    @Test
    void shouldHaveCorrectNotEquals() {
        final Question question = new Question(QUESTION_ORDER, QUESTION_WEIGHT, QUESTION_TEXT);
        assertAll("questionNotEquals",
            () -> assertNotEquals(question, new Question(QUESTION_ORDER,QUESTION_WEIGHT + 1, QUESTION_TEXT)),
            () -> assertNotEquals(question, new Question(QUESTION_ORDER,QUESTION_WEIGHT, QUESTION_TEXT + "!")),
            () -> assertNotEquals(question, new Question(QUESTION_ORDER+1,QUESTION_WEIGHT, QUESTION_TEXT + "!"))
        );
    }

    @DisplayName("корректно сравниваются на больше меньше ")
    @Test
    void shouldHaveCorrectCompareTo() {
        final Question question1 = new Question(QUESTION_ORDER-1, QUESTION_WEIGHT, QUESTION_TEXT);
        final Question question2 = new Question(QUESTION_ORDER, QUESTION_WEIGHT, QUESTION_TEXT);
        final Question question3 = new Question(QUESTION_ORDER+1, QUESTION_WEIGHT, QUESTION_TEXT);
        final List<Question> questionList = Arrays.asList(question2,question1,question3);
        questionList.sort(Question::compareTo);
        assertThat(questionList)
            .containsExactly(question1,question2,question3);
    }
}