package ru.otus.skruglikov.examiner.dao;

import ru.otus.skruglikov.examiner.domain.Exam;
import ru.otus.skruglikov.examiner.exception.EmptyResultException;

public interface ExamDao {
    Exam findByName(String examName) throws EmptyResultException;
}
