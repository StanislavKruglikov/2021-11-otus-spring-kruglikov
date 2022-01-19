package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import ru.otus.skruglikov.examiner.config.ExaminerConfig;
import ru.otus.skruglikov.examiner.domain.Answer;
import ru.otus.skruglikov.examiner.domain.Question;
import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.ExaminerAssumedAnswerException;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.provider.InputOutputProviderConsoleImpl;
import ru.otus.skruglikov.examiner.provider.LocaleProviderAppConfigImpl;

import java.io.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("класс QuizServiceImpl")
@SpringBootTest
class QuizServiceImplTest {

    @Mock
    private ExaminerConfig examinerConfig;
    @Mock
    private MessageSource messageSource;
    @Mock
    private InputOutputProviderConsoleImpl ioProvider;
    @Mock
    private LocaleProviderAppConfigImpl localeProviderAppConfig;
    @InjectMocks
    private QuizServiceImpl quizService;

    private static Answer rightAnswer;
    private static Question question;
    private static Set<Answer> answers;
    private static Quiz quiz;

    @BeforeAll
    static void beforeAll() {
        rightAnswer = new Answer("TEST_ANSWER2",true);
        question = new Question(1,1,"TEST QUESTION");
        answers = Set.of(new Answer("TEST_ANSWER1",false),rightAnswer);
        quiz = new Quiz(question, answers);
    }

    @DisplayName("должен корректно отображать вопрос")
    @Test
    void shouldCorrectShowQuestion() throws IOException {
        try(final OutputStream outputStream = new ByteArrayOutputStream()) {
            when(ioProvider.getPrintStream())
                .thenReturn(new PrintStream(outputStream));
            quizService.showQuestion(quiz);
            final String outputStr = outputStream.toString();
            assertThat(outputStr).contains(quiz.getQuestion().getText());
        }
    }

    @DisplayName("должен корректно отображать список ответов")
    @Test
    void shouldCorrectShowAnswers() throws IOException {
        try(final OutputStream outputStream = new ByteArrayOutputStream()) {
            when(ioProvider.getPrintStream())
                .thenReturn(new PrintStream(outputStream));
            quizService.showAnswers(quiz);
            final String outputStr = outputStream.toString();
            Iterator<Answer> answers = quiz.getAnswers().iterator();
            assertThat(outputStr)
                .contains(answers.next().getText())
                .contains(answers.next().getText());
        }
    }

    @DisplayName("должен корректно вернуть Map<Integer,Answer> отображенных ответов")
    @Test
    void shouldCorrectReturnMapAnswers() throws IOException {
        try(final OutputStream outputStream = new ByteArrayOutputStream()) {
            when(ioProvider.getPrintStream())
                .thenReturn(new PrintStream(outputStream));
            final Map<Integer,Answer> showAnswersMap = quizService.showAnswers(quiz);
            final Iterator<Answer> answerIterator = answers.iterator();
            assertThat(showAnswersMap)
                .containsValues(answerIterator.next(),answerIterator.next());
        }
    }

    @DisplayName("должен вернуть правильный вариант ответа")
    @Test
    void shouldGettingCorrectAnswer() throws  ExaminerException {
        assertEquals(quizService.getCorrectAnswer(quiz),rightAnswer);
    }

    @DisplayName("должен выборосить исключение если не найден корректный ответ")
    @Test
    void shouldThrowExceptionOnRightAnswerNotFound() {
        final Set<Answer> wrongAnswersSet =
            Set.of(new Answer("TEST_ANSWER1",false),new Answer("TEST_ANSWER2",false));
        final Quiz quiz = new Quiz(question,wrongAnswersSet);
        assertThrowsExactly(ExaminerException.class,() -> quizService.getCorrectAnswer(quiz));
    }

    @DisplayName("должен корректно вернуть указанный вариант ответа")
    @Test
    void shouldCorrectAskAnswer() throws IOException,ExaminerAssumedAnswerException {
        try(final InputStream is = new ByteArrayInputStream("1".getBytes());
            final OutputStream outputStream = new ByteArrayOutputStream()) {
            when(ioProvider.getInputStream())
                .thenReturn(is);
            when(ioProvider.getPrintStream())
                .thenReturn(new PrintStream(outputStream));
            when(messageSource.getMessage(anyString(),any(String[].class),any(Locale.class)))
                .thenReturn("");
            final Map<Integer,Answer> showAnswersMap = new HashMap<>() {{ this.put(1,rightAnswer); }};
            final Integer possibleAnswer = quizService.askAnswer(showAnswersMap);
            assertEquals(Integer.valueOf(1),possibleAnswer);
        }
    }

    @DisplayName("должен выборосить исключение если указан не допустимый вариант ответа")
    @Test
    void shouldThrowAskAnswer() throws IOException {
        try(
            final InputStream overNumber = new ByteArrayInputStream("2".getBytes());
            final InputStream lessNumber = new ByteArrayInputStream("0".getBytes());
            final OutputStream outputStream = new ByteArrayOutputStream()
        ) {
            when(messageSource.getMessage(anyString(),any(String[].class),any(Locale.class)))
                .thenReturn("");
            when(ioProvider.getInputStream())
                .thenReturn(overNumber)
                .thenReturn(lessNumber);
            when(ioProvider.getPrintStream())
                .thenReturn(new PrintStream(outputStream));
            final Map<Integer,Answer> showAnswersMap = new HashMap<>() {{ this.put(1,rightAnswer); }};
            assertThrowsExactly(ExaminerAssumedAnswerException.class,() -> quizService.askAnswer(showAnswersMap));
            assertThrowsExactly(ExaminerAssumedAnswerException.class,() -> quizService.askAnswer(showAnswersMap));
        }
    }
}