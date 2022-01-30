package ru.otus.skruglikov.examiner.service;

import ru.otus.skruglikov.examiner.domain.Student;
import ru.otus.skruglikov.examiner.exception.RegistrationException;

public interface RegistrationService {
    Student register() throws RegistrationException;
}
