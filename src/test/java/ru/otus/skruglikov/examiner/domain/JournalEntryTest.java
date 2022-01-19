package ru.otus.skruglikov.examiner.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс JournalEntry")
class JournalEntryTest {
    private final Exam exam = new Exam("", Collections.EMPTY_LIST);
    private final Student student = new Student("", "");

    @DisplayName("корректно создается конструктором")
    @Test
    void shouldHaveCorrectConstructor() {
        final JournalEntry journalEntry = new JournalEntry(student, exam);
        assertAll("journalEntryConstructor",
            () -> assertEquals(exam, journalEntry.getExam()),
            () -> assertEquals(student, journalEntry.getStudent()),
            () -> assertEquals(0, journalEntry.getExamScore())
        );

    }

    @DisplayName("корректно сравниваются на равенство")
    @Test
    void shouldHaveCorrectEquals() {
        assertEquals(new JournalEntry(student, exam),
                new JournalEntry(student, exam));

    }

    @DisplayName("корректно сравниваются на не равенство")
    @Test
    void shouldHaveCorrectNotEquals() {
        final JournalEntry journalEntry = new JournalEntry(student, exam);
        final Student student2 = new Student(student.getFirstName()+"!",student.getLastName());
        final Exam exam2 = new Exam(exam.getName()+"!",exam.getQuizList());
        assertAll("answerNotEquals",
            () -> assertNotEquals(journalEntry, new JournalEntry(student2, exam)),
            () -> assertNotEquals(journalEntry, new JournalEntry(student, exam2))
        );

    }
}