package ru.otus.skruglikov.examiner.dao;

import ru.otus.skruglikov.examiner.domain.Quiz;

import java.util.List;

public interface QuizDao {
    List<Quiz> readAllSortedQuizzes(final String examName);
}
