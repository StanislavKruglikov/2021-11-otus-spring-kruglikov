package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.skruglikov.examiner.domain.Answer;
import ru.otus.skruglikov.examiner.domain.Question;
import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.ExaminerAssumedAnswerException;
import ru.otus.skruglikov.examiner.exception.ExaminerRightAnswerAbsentException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("класс QuizServiceImpl")
@SpringBootTest
class QuizServiceImplTest {

    @MockBean
    private LocaleIOServiceImpl localeIOService;
    @Autowired
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
        try(final OutputStream os = new ByteArrayOutputStream()) {
            doAnswer(invocation -> {
                os.write(invocation.<String>getArgument(0).getBytes(StandardCharsets.UTF_8));
                return null;
            })
                .when(localeIOService)
                .write(eq(quiz.getQuestion().getText()));
            quizService.showQuestion(quiz);
            assertEquals(os.toString(),quiz.getQuestion().getText());
        }
    }

    @DisplayName("должен корректно отображать список ответов")
    @Test
    void shouldCorrectShowAnswers() throws IOException {
        try(final OutputStream os = new ByteArrayOutputStream()) {
            doAnswer(invocation -> {
                os.write(invocation.<String>getArgument(0).getBytes(StandardCharsets.UTF_8));
                return null;
            })
                .when(localeIOService)
                .write(any());
            quizService.showAnswers(quiz);
            Iterator<Answer> answersIterator = quiz.getAnswers().iterator();
            assertThat(os.toString())
                .contains(answersIterator.next().getText())
                .contains(answersIterator.next().getText());
        }
    }

    @DisplayName("должен корректно вернуть Map<Integer,Answer> отображенных ответов")
    @Test
    void shouldCorrectReturnMapAnswers() throws IOException {
        try(final OutputStream os = new ByteArrayOutputStream()) {
            doAnswer(invocation -> {
                new PrintStream(os).println(invocation.<String>getArgument(0));
                return null;
            })
                .when(localeIOService)
                .write(any());
            final Map<Integer,Answer> showAnswersMap = quizService.showAnswers(quiz);
            final Iterator<Answer> answerIterator = answers.iterator();
            assertThat(showAnswersMap)
                .containsValues(answerIterator.next(),answerIterator.next());
        }
    }

    @DisplayName("должен вернуть правильный вариант ответа")
    @Test
    void shouldGettingCorrectAnswer() throws ExaminerRightAnswerAbsentException {
        assertEquals(quizService.getCorrectAnswer(quiz),rightAnswer);
    }

    @DisplayName("должен выборосить исключение если не найден корректный ответ")
    @Test
    void shouldThrowExceptionOnRightAnswerNotFound() {
        final Set<Answer> wrongAnswersSet =
            Set.of(new Answer("TEST_ANSWER1",false),new Answer("TEST_ANSWER2",false));
        final Quiz quiz = new Quiz(question,wrongAnswersSet);
        assertThrowsExactly(ExaminerRightAnswerAbsentException.class,() -> quizService.getCorrectAnswer(quiz));
    }

    @DisplayName("должен корректно вернуть указанный вариант ответа")
    @Test
    void shouldCorrectAskAnswer() throws IOException,ExaminerAssumedAnswerException {
        try(final OutputStream os = new ByteArrayOutputStream()) {
            when(localeIOService.read())
                .thenReturn("1");
            doAnswer(invocation -> {
                new PrintStream(os).println(invocation.<String>getArgument(0));
                return null;
            })
                .when(localeIOService)
                .write(any());
            doNothing()
                .when(localeIOService)
                .writeMessage(any(),any());
            final Map<Integer,Answer> showAnswersMap = new HashMap<>() {{ this.put(1,rightAnswer); }};
            final int possibleAnswer = quizService.askAnswer(showAnswersMap);
            assertEquals(1,possibleAnswer);
        }
    }

    @DisplayName("должен выборосить исключение если указан не допустимый вариант ответа")
    @Test
    void shouldThrowAskAnswer() throws IOException {
        try(final OutputStream os = new ByteArrayOutputStream()) {
            when(localeIOService.read())
                .thenReturn("2")
                .thenReturn("0");
            doAnswer(invocation -> {
                os.write(invocation.<String>getArgument(0).getBytes(StandardCharsets.UTF_8));
                return null;
            })
                .when(localeIOService)
                .write(any());
            final Map<Integer,Answer> showAnswersMap = new HashMap<>() {{ this.put(1,rightAnswer); }};
            assertThrowsExactly(ExaminerAssumedAnswerException.class,() -> quizService.askAnswer(showAnswersMap));
            assertThrowsExactly(ExaminerAssumedAnswerException.class,() -> quizService.askAnswer(showAnswersMap));
        }
    }
}