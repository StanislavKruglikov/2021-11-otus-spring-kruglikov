package ru.otus.skruglikov.examiner.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.dao.ExamDao;
import ru.otus.skruglikov.examiner.dao.JournalEntryDao;
import ru.otus.skruglikov.examiner.domain.*;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.provider.InputOutputProvider;
import ru.otus.skruglikov.examiner.provider.LocaleProvider;

import java.io.PrintStream;

@Service
public class ExaminerServiceImpl extends ServiceMessageAwareAbstract implements ExaminerService {

    private final ExamDao examDao;
    private final JournalEntryDao journalEntryDao;

    private final RegistrationService registrationService;
    private final ExamService examService;

    public ExaminerServiceImpl(final MessageSource messageSource, final LocaleProvider localeProvider,
                               final RegistrationService registrationService,final ExamDao examDao,
                               final InputOutputProvider ioProvider, final JournalEntryDao journalEntryDao,
                               final ExamService examService) {
        super(messageSource, localeProvider, ioProvider);
        this.registrationService = registrationService;
        this.examService = examService;
        this.examDao = examDao;
        this.journalEntryDao = journalEntryDao;
    }

    @Override
    public JournalEntry takeExam(final String examName) {
        final PrintStream ps = ioProvider.getPrintStream();
        JournalEntry journalEntry = null;
        try {
            final Student student = registrationService.register();
            final Exam exam = examDao.create(examName);
            journalEntry = journalEntryDao.create(student,exam);
            journalEntryDao.setExamScore(journalEntry, examService.startExam(exam));
            examService.showExamResult(journalEntry);
        } catch (ExaminerException e) {
            ps.println(e.getMessage());
        }
        return journalEntry;
    }
}
