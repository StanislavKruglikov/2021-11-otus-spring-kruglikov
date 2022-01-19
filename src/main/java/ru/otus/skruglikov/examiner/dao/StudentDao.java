package ru.otus.skruglikov.examiner.dao;

import ru.otus.skruglikov.examiner.domain.Student;
import ru.otus.skruglikov.examiner.exception.ExaminerException;

public interface StudentDao {
    Student createStudent() throws ExaminerException;
}
