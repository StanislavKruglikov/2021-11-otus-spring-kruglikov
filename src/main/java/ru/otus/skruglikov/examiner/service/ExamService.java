package ru.otus.skruglikov.examiner.service;

import ru.otus.skruglikov.examiner.domain.Exam;
import ru.otus.skruglikov.examiner.domain.JournalEntry;

public interface ExamService {
    int startExam(final Exam exam);
    void showExamResult(JournalEntry journalEntry);
}
