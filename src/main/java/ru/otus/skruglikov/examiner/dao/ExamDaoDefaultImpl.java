package ru.otus.skruglikov.examiner.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.skruglikov.examiner.domain.Exam;
import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.EmptyResultException;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExamDaoDefaultImpl implements ExamDao {

    private final QuizDao quizDao;

    @Override
    public Exam findByName(final String examName) throws EmptyResultException {
        final List<Quiz> quizDaoList = quizDao.readAllSortedQuizzes(examName);
        if(quizDaoList.isEmpty()) {
            throw new EmptyResultException(String.format("empty quiz list for exam:\"%s\"",examName));
        }
        return new Exam(examName,quizDaoList);
    }
}
