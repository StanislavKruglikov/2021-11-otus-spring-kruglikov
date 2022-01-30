package ru.otus.skruglikov.examiner.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.skruglikov.examiner.domain.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Класс JournalEntryMemoryDaoImpl")
@SpringBootTest
class JournalEntryDaoTest {

    @Autowired
    private JournalEntryDaoImpl journalEntryDao;

    @DisplayName("корректно создает запись в журнале")
    @Test
    void shouldCorrectCreateJournalEntity() {
        final Student studentPattern = new Student("TestFirst","TestLast");
        final Exam examPattern = new Exam("test",Collections.emptyList());
        final JournalEntry journalEntryActual =  journalEntryDao.create(studentPattern,
                examPattern);
            assertAll("testJournalEntry",
                ()-> assertEquals(studentPattern,journalEntryActual.getStudent()),
                ()-> assertEquals(examPattern,journalEntryActual.getExam()),
                ()-> assertEquals(0,journalEntryActual.getExamScore())
            );
    }

    @DisplayName("корректно назначает баллы за экзамен")
    @Test
    void shouldCorrectSetScoreJournalEntity() {
        final Student studentPattern = new Student("TestFirst","TestLast");
        final Exam examPattern = new Exam("test",Collections.emptyList());
        final JournalEntry journalEntryActual =  journalEntryDao.create(studentPattern,
                examPattern);
        journalEntryActual.setExamScore(33);
        assertEquals(33,journalEntryActual.getExamScore());
    }
}