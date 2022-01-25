package ru.otus.skruglikov.examiner.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс JournalEntry")
class JournalEntryTest {
    private final Exam exam = new Exam("", Collections.emptyList());
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
        final JournalEntry actual = new JournalEntry(student, exam);
        actual.setExamScore(101);
        final JournalEntry expected = new JournalEntry(student, exam);
        expected.setExamScore(101);
        assertEquals(expected,actual);

    }

    @DisplayName("корректно сравниваются на не равенство")
    @Test
    void shouldHaveCorrectNotEquals() {
        final JournalEntry journalEntry = new JournalEntry(student, exam);
        journalEntry.setExamScore(101);
        final Student student2 = new Student(student.getFirstName()+"!",student.getLastName());
        final Exam exam2 = new Exam(exam.getName()+"!",exam.getQuizList());
        final JournalEntry journalEntryActual1 = new JournalEntry(student2, exam);
        journalEntryActual1.setExamScore(101);
        final JournalEntry journalEntryActual2 = new JournalEntry(student, exam2);
        journalEntryActual2.setExamScore(101);
        final JournalEntry journalEntryActual3 = new JournalEntry(student, exam);
        journalEntryActual3.setExamScore(99);
        assertAll("answerNotEquals",
            () -> assertNotEquals(journalEntry, journalEntryActual1),
            () -> assertNotEquals(journalEntry, journalEntryActual2),
            () -> assertNotEquals(journalEntry, journalEntryActual3)
        );

    }
}