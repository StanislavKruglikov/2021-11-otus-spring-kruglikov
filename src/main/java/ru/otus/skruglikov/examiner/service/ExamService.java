package ru.otus.skruglikov.examiner.service;

import ru.otus.skruglikov.examiner.domain.Exam;
import ru.otus.skruglikov.examiner.domain.JournalEntry;
import ru.otus.skruglikov.examiner.exception.ExaminerException;

public interface ExamService {
    int startExam(final Exam exam) throws ExaminerException;
    void showExamResult(JournalEntry journalEntry);
}
