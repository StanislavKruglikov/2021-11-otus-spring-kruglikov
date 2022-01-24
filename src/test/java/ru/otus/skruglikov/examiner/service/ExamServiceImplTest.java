package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import ru.otus.skruglikov.examiner.config.ExaminerConfig;
import ru.otus.skruglikov.examiner.domain.*;
import ru.otus.skruglikov.examiner.exception.ExaminerAssumedAnswerException;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.provider.InputOutputProviderConsoleImpl;
import ru.otus.skruglikov.examiner.provider.LocaleProviderAppConfigImpl;

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

    @Mock
    private ExaminerConfig examinerConfig;
    @Mock
    private MessageSource messageSource;
    @Mock
    private InputOutputProviderConsoleImpl ioProvider;
    @Mock
    private LocaleProviderAppConfigImpl localeProviderAppConfig;
    @Mock
    private QuizServiceImpl quizService;
    @Mock
    private LocaleServiceStreamImpl localeService;

    @InjectMocks
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
        when(examinerConfig.getScoreExamPass())
            .thenReturn(20);
    }

    @DisplayName("должен корректно подсчитывать и возвращать набранные верными ответами баллы")
    @Test
    void shouldCorrectAccountAndReturnExamScore() throws ExaminerAssumedAnswerException, ExaminerException, IOException {
        doNothing()
            .when(quizService)
            .showQuestion(any());
        when(quizService.showAnswers(any()))
            .thenReturn(answersShowMap);
        when(quizService.askAnswer(any()))
            .thenReturn(1);
        when(quizService.getCorrectAnswer(any()))
            .thenReturn(rightAnswer);
        try(final OutputStream outputStream = new ByteArrayOutputStream()) {
            when(ioProvider.getOutput())
                .thenReturn(new PrintStream(outputStream));
            int examScour = examService.startExam(exam);
            assertEquals(20,examScour);
        }
    }

    @DisplayName("должен отображать сообщение об успешном прохождении экзамена")
    @Test
    void showExamSuccessResult() throws IOException {
        try(final OutputStream outputStream = new ByteArrayOutputStream()) {
            doAnswer(inv -> { new PrintStream(outputStream).println("SUCCESS");
                return null; })
                .when(localeService)
                .output(eq("examiner.resultSuccess"),any());
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
                .when(localeService)
                .output(eq("examiner.resultFailed"),any());
            final JournalEntry entry = new JournalEntry(new Student("",""), exam);
            entry.setExamScore(19);
            examService.showExamResult(entry);
            assertThat(outputStream.toString()).contains("FAILED");
        }
    }

}