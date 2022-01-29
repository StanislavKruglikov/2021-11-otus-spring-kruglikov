package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.skruglikov.examiner.config.ExaminerConfig;
import ru.otus.skruglikov.examiner.domain.*;
import ru.otus.skruglikov.examiner.exception.ExaminerAssumedAnswerException;
import ru.otus.skruglikov.examiner.exception.ExaminerRightAnswerAbsentException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("класс ExamServiceImpl")
@SpringBootTest
class ExamServiceImplTest {

    @MockBean
    private ExaminerConfig examinerConfig;
    @MockBean
    private LocaleIOServiceImpl localeIOService;
    @MockBean
    private QuizServiceImpl quizService;

    @Autowired
    private ExamServiceImpl examService;

    private static Answer rightAnswer;
    private static Question question;
    private static Set<Answer> answers;
    private static Quiz quiz;
    private static Map<Integer,Answer> answersShowMap;

    private static Exam exam;

    @BeforeAll
    static void beforeAll() {
        rightAnswer = new Answer("TEST_ANSWER2",true);
        question = new Question(1,10,"TEST QUESTION");
        answers = Collections.singleton(rightAnswer);
        quiz = new Quiz(question, answers);
        exam = new Exam("TEST", List.of(quiz,quiz));
        answersShowMap = new HashMap<>() {{ this.put(1,rightAnswer); }};
    }

    @BeforeEach
    void setUp() {
        when(examinerConfig.getExamScorePass())
            .thenReturn(20);
    }

    @DisplayName("должен корректно подсчитывать и возвращать набранные верными ответами баллы")
    @Test
    void shouldCorrectAccountAndReturnExamScore()
        throws ExaminerAssumedAnswerException, ExaminerRightAnswerAbsentException {
        doNothing()
            .when(quizService)
            .showQuestion(any());
        when(quizService.showAnswers(any()))
            .thenReturn(answersShowMap);
        when(quizService.askAnswer(any()))
            .thenReturn(1);
        when(quizService.getCorrectAnswer(any()))
            .thenReturn(rightAnswer);
        doNothing()
            .when(localeIOService)
            .writeMessage(any(),any());
        int examScour = examService.startExam(exam);
        assertEquals(20,examScour);
    }

    @DisplayName("должен отображать сообщение об успешном прохождении экзамена")
    @Test
    void showExamSuccessResult() throws IOException {
        try(final OutputStream outputStream = new ByteArrayOutputStream()) {
            doAnswer(inv -> { new PrintStream(outputStream).println("SUCCESS");
                return null; })
                .when(localeIOService)
                .writeMessage(eq("examiner.resultSuccess"),any());
            final JournalEntry entry = new JournalEntry(new Student("",""), exam);
            entry.setExamScore(21);
            examService.showExamResult(entry);
            assertThat(outputStream.toString()).contains("SUCCESS");
        }
    }

    @DisplayName("должен отображать сообщение о не прохождении экзамена")
    @Test
    void showExamFailedResult() throws IOException {
        try(final OutputStream outputStream = new ByteArrayOutputStream()) {
            doAnswer(inv -> { new PrintStream(outputStream).println("FAILED");
                return null; })
                .when(localeIOService)
                .writeMessage(eq("examiner.resultFailed"),any());
            final JournalEntry entry = new JournalEntry(new Student("",""), exam);
            entry.setExamScore(19);
            examService.showExamResult(entry);
            assertThat(outputStream.toString()).contains("FAILED");
        }
    }

}