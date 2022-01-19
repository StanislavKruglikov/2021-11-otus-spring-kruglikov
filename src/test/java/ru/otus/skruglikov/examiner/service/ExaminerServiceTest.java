package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import ru.otus.skruglikov.examiner.config.ExaminerConfig;
import ru.otus.skruglikov.examiner.dao.ExamDaoDefaultImpl;
import ru.otus.skruglikov.examiner.dao.JournalEntryDaoMemoryImpl;
import ru.otus.skruglikov.examiner.domain.Exam;
import ru.otus.skruglikov.examiner.domain.JournalEntry;
import ru.otus.skruglikov.examiner.domain.Student;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.provider.InputOutputProviderConsoleImpl;
import ru.otus.skruglikov.examiner.provider.LocaleProviderAppConfigImpl;

import java.io.*;
import java.util.Collections;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Класс ExaminerServiceImpl")
@SpringBootTest
class ExaminerServiceTest {

    @Value("classpath:testdata.csv")
    private Resource testDataResource;

    @Mock
    private ExaminerConfig examinerConfig;
    @Mock
    private MessageSource messageSource;
    @Mock
    private InputOutputProviderConsoleImpl ioProvider;
    @Mock
    private LocaleProviderAppConfigImpl localeProviderAppConfig;
    @Mock
    private RegistrationServiceImpl registrationService;
    @Mock
    private ExamDaoDefaultImpl examDao;
    @Mock
    private JournalEntryDaoMemoryImpl journalEntryDao;
    @Mock
    private ExamServiceImpl examService;

    @InjectMocks
    private ExaminerServiceImpl examinerService;

    private static Student student;
    private static Exam exam;

    @BeforeAll
    static void beforeAll() {
        student = new Student("FirstName","LastName");
        exam = new Exam("Exam Name", Collections.EMPTY_LIST);
    }

    @DisplayName("метод должен регестировать студента и запускать экзамен")
    @Test
    void takeExam() throws ExaminerException, IOException {
        try(final OutputStream outputStream = new ByteArrayOutputStream();
            final InputStream is = testDataResource.getInputStream()) {
            final JournalEntry journalEntryExpected = new JournalEntry(student,exam);
            when(registrationService.register())
                .thenReturn(student);
            when(examDao.create(anyString()))
                .thenReturn(exam);
            when(journalEntryDao.create(any(),any()))
                .thenReturn(journalEntryExpected);
            when(examService.startExam(any(Exam.class)))
                .thenReturn(101);
            doCallRealMethod()
                .when(journalEntryDao)
                .setExamScore(any(JournalEntry.class),eq(101));
            doNothing()
                .when(examService)
                .showExamResult(any());
            final JournalEntry journalEntryActual = examinerService.takeExam(exam.getName());
            journalEntryExpected.setExamScore(101);
            assertEquals(journalEntryExpected,journalEntryActual);
        }
    }

}