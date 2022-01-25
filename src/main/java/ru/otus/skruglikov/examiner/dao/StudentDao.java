package ru.otus.skruglikov.examiner.dao;

import ru.otus.skruglikov.examiner.domain.Student;

public interface StudentDao {
    Student createStudent(String firstName,String lastName);
}
