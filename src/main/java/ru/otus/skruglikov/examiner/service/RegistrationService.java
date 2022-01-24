package ru.otus.skruglikov.examiner.service;

import ru.otus.skruglikov.examiner.domain.Student;
import ru.otus.skruglikov.examiner.exception.ExaminerException;

public interface RegistrationService {
    Student register() throws ExaminerException;
}
