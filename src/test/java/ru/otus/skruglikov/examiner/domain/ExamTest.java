package ru.otus.skruglikov.examiner.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс Exam")
class ExamTest {

    private final static Question QUESTION = new Question(1,100, "TestQuestionText");
    private final static Set<Answer> ANSWERS = Collections.singleton(new Answer("TestAnswerText", true));
    private final static String EXAM_NAME = "TestExamName";
    private final static List<Quiz> QUIZ_LIST = Collections.singletonList(new Quiz(QUESTION, ANSWERS));

    @DisplayName("корректно создается конструктором")
    @Test
    void shouldHaveCorrectConstructor() {
        final Exam exam = new Exam(EXAM_NAME, QUIZ_LIST);
        assertAll("exam",
            () -> assertEquals(EXAM_NAME,exam.getName()),
            () -> assertEquals(QUIZ_LIST,exam.getQuizList())
        );
    }

    @DisplayName("корректно сравниваются на равенство")
    @Test
    void shouldHaveCorrectEquals() {
        assertEquals(new Exam(EXAM_NAME, QUIZ_LIST),
            new Exam(EXAM_NAME, QUIZ_LIST));

    }

    @DisplayName("корректно сравниваются на не равенство")
    @Test
    void shouldHaveCorrectNotEquals() {
        final Exam exam = new Exam(EXAM_NAME, QUIZ_LIST);
        assertAll("examNotEquals",
            () -> assertNotEquals(exam, new Exam("", QUIZ_LIST)),
            () -> assertNotEquals(exam, new Exam(EXAM_NAME, Collections.emptyList()))
        );

    }
}