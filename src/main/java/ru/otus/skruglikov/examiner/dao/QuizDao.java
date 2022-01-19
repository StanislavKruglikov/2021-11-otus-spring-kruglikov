package ru.otus.skruglikov.examiner.dao;

import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.ExaminerException;

import java.util.List;


public interface QuizDao {
    List<Quiz> readAllQuizzes(String examName) throws ExaminerException;
    List<Quiz> readAllSortedQuizzes(final String examName) throws ExaminerException;
}
