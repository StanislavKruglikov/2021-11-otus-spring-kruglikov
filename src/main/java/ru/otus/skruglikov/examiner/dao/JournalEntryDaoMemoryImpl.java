package ru.otus.skruglikov.examiner.dao;

import org.springframework.stereotype.Repository;
import ru.otus.skruglikov.examiner.domain.Exam;
import ru.otus.skruglikov.examiner.domain.JournalEntry;
import ru.otus.skruglikov.examiner.domain.Student;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JournalEntryDaoMemoryImpl implements JournalEntryDao {

    private final List<JournalEntry> journalEntries = new ArrayList<>();

    @Override
    public JournalEntry create(final Student student,final Exam exam) {
        return new JournalEntry(student, exam);
    }

    @Override
    public void setExamScore(final JournalEntry journalEntry, final int score) {
        journalEntry.setExamScore(score);
    }

    @Override
    public void addEntry(final JournalEntry journalEntry) {
        journalEntries.add(journalEntry);
    }

    @Override
    public List<JournalEntry> readAll() {
        return this.journalEntries;
    }
}
