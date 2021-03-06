package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import ru.otus.skruglikov.examiner.config.ExaminerConfig;
import ru.otus.skruglikov.examiner.dao.ExamDaoDefaultImpl;
import ru.otus.skruglikov.examiner.dao.JournalEntryDaoImpl;
import ru.otus.skruglikov.examiner.domain.Exam;
import ru.otus.skruglikov.examiner.domain.JournalEntry;
import ru.otus.skruglikov.examiner.domain.Student;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Класс ExaminerServiceImpl")
@SpringBootTest
class ExaminerServiceTest {

    @Value("classpath:testdata.csv")
    private Resource testDataResource;

    @MockBean
    private ExaminerConfig examinerConfig;
    @MockBean
    private RegistrationServiceImpl registrationService;
    @MockBean
    private ExamDaoDefaultImpl examDao;
    @MockBean
    private JournalEntryDaoImpl journalEntryDao;
    @MockBean
    private ExamServiceImpl examService;

    @Autowired
    private ExaminerServiceImpl examinerService;

    private static Student student;
    private static Exam exam;

    @BeforeAll
    static void beforeAll() {
        student = new Student("FirstName","LastName");
        exam = new Exam("Exam Name", Collections.emptyList());
    }

    @DisplayName("метод должен регестировать студента и запускать экзамен")
    @Test
    void takeExam() throws Exception {
            final JournalEntry journalEntryExpected = new JournalEntry(student,exam);
            when(registrationService.register())
                .thenReturn(student);
            when(examDao.findByName(anyString()))
                .thenReturn(exam);
            when(journalEntryDao.create(any(),any()))
                .thenReturn(journalEntryExpected);
            when(examService.startExam(any(Exam.class)))
                .thenReturn(101);
            doCallRealMethod()
                .when(journalEntryDao)
                .setExamScore(any(JournalEntry.class),eq(101));
            doAnswer(invocation -> {
                assertEquals(journalEntryExpected,invocation.getArgument(0));
                return null;
            }).when(examService)
                .showExamResult(any());
            examinerService.takeExam(exam.getName());
    }
}