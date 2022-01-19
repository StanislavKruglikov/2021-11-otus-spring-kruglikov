package ru.otus.skruglikov.examiner.dao;

import ru.otus.skruglikov.examiner.domain.Exam;
import ru.otus.skruglikov.examiner.domain.JournalEntry;
import ru.otus.skruglikov.examiner.domain.Student;

import java.util.List;

public interface JournalEntryDao {

    JournalEntry create(Student student, Exam exam);

    void setExamScore(JournalEntry journalEntry, int examScore);

    void addEntry(final JournalEntry journalEntry);

    List<JournalEntry> readAll();
}
