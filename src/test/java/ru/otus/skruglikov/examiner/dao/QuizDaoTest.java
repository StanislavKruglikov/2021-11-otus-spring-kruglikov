package ru.otus.skruglikov.examiner.dao;

import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import ru.otus.skruglikov.examiner.domain.Answer;
import ru.otus.skruglikov.examiner.domain.Question;
import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.exception.MismatchLineFormatException;
import ru.otus.skruglikov.examiner.provider.DatasourceProviderCSVImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@DisplayName("Класс QuizDaoCSVImpl")
@SpringBootTest
class QuizDaoTest {
    private static final String TEST_EXAM_NAME = "Test exam name";

    @Mock
    private DatasourceProviderCSVImpl datasourceProvider;
    @InjectMocks
    private QuizDaoCSVImpl quizDao;
    @Value("classpath:testdata.csv")
    private Resource testDataResource;

    private Quiz quiz1;
    private Quiz quiz2;
    private Quiz quiz3;

    @BeforeEach
    void setUp() {
        quiz1 = new Quiz(new Question(1,10,"TEST_QUESTION_A"),
            Set.of(new Answer("ANSWER_A_1",false),new Answer("ANSWER_A_2",true)));
        quiz2 = new Quiz(new Question(2,20,"TEST_QUESTION_C"),
                Set.of(new Answer("ANSWER_C_1",true),new Answer("ANSWER_C_2",false)));
        quiz3 = new Quiz(new Question(3,10,"TEST_QUESTION_B"),
            Set.of(new Answer("ANSWER_B_1",false),new Answer("ANSWER_B_2",false)
                ,new Answer("ANSWER_B_3",true),new Answer("ANSWER_B_4",false)));
    }

    @DisplayName("корректно начитывает все тесты экзамена")
    @Test
    void shouldCorrectReadAllQuizzesForExam() throws Exception {
        try(final InputStream is = testDataResource.getInputStream()) {
            when(datasourceProvider.getInputStream())
                .thenReturn(is);

            assertThat(quizDao.readAllSortedQuizzes(TEST_EXAM_NAME))
                .hasSize(3)
                .contains(quiz3,quiz1);
        }
    }

    @DisplayName("корректно начитывает пустой список для неизвестного экзамена")
    @Test
    void shouldReadEmptyListQuizzesForUnknownExam() throws Exception {
        try(final InputStream is = testDataResource.getInputStream()) {
            when(datasourceProvider.getInputStream())
                .thenReturn(is);

            assertThat(quizDao.readAllSortedQuizzes(TEST_EXAM_NAME+" any other"))
                .hasSize(0);
        }
    }


    @DisplayName("возвращает упорядоченный список все тестов экзамена")
    @Test
    void shouldCorrectOrderReadAllQuizzes() throws Exception {
        try(final InputStream is = testDataResource.getInputStream()){
            when(datasourceProvider.getInputStream())
                .thenReturn(is);

            assertThat(quizDao.readAllSortedQuizzes(TEST_EXAM_NAME))
                .hasSize(3)
                .containsSubsequence(quiz1,quiz3);
        }
    }

    @DisplayName("выбрасывает исключение при не корректном формате строки теста")
    @Test
    void shouldCorrectThrowExceptionOnMismatchFormatLine() throws Exception {
        try(final InputStream is = testDataResource.getInputStream()){
            when(datasourceProvider.getInputStream())
                .thenReturn(is);

            assertThatExceptionOfType(MismatchLineFormatException.class).isThrownBy(()-> {
                quizDao.readAllSortedQuizzes("Test exam name wrong format line");
            });

        }
    }
}