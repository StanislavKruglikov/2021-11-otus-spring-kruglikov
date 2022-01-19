package ru.otus.skruglikov.examiner.service;

import ru.otus.skruglikov.examiner.domain.JournalEntry;

public interface ExaminerService {
    JournalEntry takeExam(final String examName);
}
