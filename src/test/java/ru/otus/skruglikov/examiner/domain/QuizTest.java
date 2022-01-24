package ru.otus.skruglikov.examiner.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Quiz")
class QuizTest {
    public static final Question QUESTION = new Question(1,100, "TestQuiz");
    public static final Question QUESTION2 = new Question(2,200, "TestQuiz2");
    public static final Question QUESTION3 = new Question(3,300, "TestQuiz3");
    public static final Set<Answer> ANSWERS = Set.of(new Answer("TestAnswer",false), new Answer("TestAnswerRight",true));

    @DisplayName("корректно создается конструктором")
    @Test
    void shouldHaveCorrectConstructor() {
        final Quiz quiz = new Quiz(QUESTION,ANSWERS);
        assertAll("quizConstructor",
            () -> assertEquals(QUESTION,quiz.getQuestion()),
            () -> assertEquals(ANSWERS,quiz.getAnswers())
        );
    }

    @DisplayName("корректно сравниваются на равенство")
    @Test
    void shouldHaveCorrectEquals() {
        assertEquals(new Quiz(QUESTION,ANSWERS),
            new Quiz(QUESTION,ANSWERS));

    }

    @DisplayName("корректно сравниваются на не равенство")
    @Test
    void shouldHaveCorrectNotEquals() {
        final Quiz quiz = new Quiz(QUESTION,ANSWERS);
        assertAll("quizNotEquals",
            () -> assertNotEquals(quiz, new Quiz(new Question(QUESTION.getNumberOrder(),QUESTION.getWeight()+1,
                QUESTION.getText()),ANSWERS)),
            () -> assertNotEquals(quiz, new Quiz(QUESTION, Collections.emptySet()))
        );
    }

    @DisplayName("корректно сравниваются на больше меньше ")
    @Test
    void shouldHaveCorrectCompareTo() {
        final Quiz quiz1 = new Quiz(QUESTION,ANSWERS);
        final Quiz quiz2 = new Quiz(QUESTION2,ANSWERS);
        final Quiz quiz3 = new Quiz(QUESTION3,ANSWERS);
        final List<Quiz> quizList = Arrays.asList(quiz2,quiz3,quiz1);
        quizList.sort(Quiz::compareTo);
        assertThat(quizList)
            .containsExactly(quiz1,quiz2,quiz3);
    }
}