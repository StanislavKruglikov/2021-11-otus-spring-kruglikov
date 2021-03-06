package ru.otus.skruglikov.examiner.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.skruglikov.examiner.domain.Answer;
import ru.otus.skruglikov.examiner.domain.Exam;
import ru.otus.skruglikov.examiner.domain.Question;
import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.EmptyResultException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("Класс QuizDaoCSVImpl")
@SpringBootTest
class ExamDaoTest {
    private static final String TEST_EXAM_NAME = "Test exam name";

    @MockBean
    private QuizDao quizDao;

    @Autowired
    private ExamDaoDefaultImpl examDao;
    private Quiz lastQuiz;

    @BeforeEach
    void setUp() {
        lastQuiz = new Quiz(new Question(3,10,"TEST_QUESTION_B"),
            Set.of(new Answer("ANSWER_B_1",false),new Answer("ANSWER_B_2",false),
                new Answer("ANSWER_B_3",true),new Answer("ANSWER_B_4",false)));
    }

    @DisplayName("корректно создает экзамен")
    @Test
    void shouldCorrectCreateExam() throws Exception {
            List<Quiz> quizList = List.of(lastQuiz);
            when(quizDao.readAllSortedQuizzes(TEST_EXAM_NAME))
                .thenReturn(quizList);
            final Exam exam = examDao.findByName(TEST_EXAM_NAME);
            assertAll("testExam",
                ()-> assertEquals(TEST_EXAM_NAME,exam.getName()),
                ()-> assertEquals(exam.getQuizList(),quizList)
            );
    }

    @DisplayName("выбрасывает исключение для пустого списка вопросов по экзамену")
    @Test
    void shouldThrowExceptionOnEmptyListQuestion() {
        List<Quiz> quizList = List.of(lastQuiz);
        when(quizDao.readAllSortedQuizzes(""))
            .thenReturn(quizList);
        assertThatThrownBy(()-> examDao.findByName(TEST_EXAM_NAME))
            .isInstanceOf(EmptyResultException.class)
            .hasMessageContaining("empty quiz list");
    }
}