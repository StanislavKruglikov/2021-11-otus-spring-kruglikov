package ru.otus.skruglikov.examiner.dao;

import org.springframework.stereotype.Repository;
import ru.otus.skruglikov.examiner.domain.Exam;
import ru.otus.skruglikov.examiner.domain.JournalEntry;
import ru.otus.skruglikov.examiner.domain.Student;

@Repository
public class JournalEntryDaoImpl implements JournalEntryDao {

    @Override
    public JournalEntry create(final Student student,final Exam exam) {
        return new JournalEntry(student, exam);
    }

    @Override
    public void setExamScore(final JournalEntry journalEntry, final int score) {
        journalEntry.setExamScore(score);
    }

}
