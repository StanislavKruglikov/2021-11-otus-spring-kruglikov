package ru.otus.skruglikov.examiner.dao;

import ru.otus.skruglikov.examiner.domain.Exam;
import ru.otus.skruglikov.examiner.exception.ExaminerException;

public interface ExamDao {
    Exam create(String examName) throws ExaminerException;
}
