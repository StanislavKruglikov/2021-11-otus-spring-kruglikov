package ru.otus.skruglikov.examiner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.dao.ExamDao;
import ru.otus.skruglikov.examiner.dao.JournalEntryDao;
import ru.otus.skruglikov.examiner.domain.*;

@Service
@RequiredArgsConstructor
public class ExaminerServiceImpl implements ExaminerService {

    private final IOService ioService;
    private final ExamDao examDao;
    private final JournalEntryDao journalEntryDao;

    private final RegistrationService registrationService;
    private final ExamService examService;

    @Override
    public void takeExam(final String examName) {
        JournalEntry journalEntry;
        try {
            final Student student = registrationService.register();
            final Exam exam = examDao.findByName(examName);
            journalEntry = journalEntryDao.create(student,exam);
            journalEntryDao.setExamScore(journalEntry, examService.startExam(exam));
            examService.showExamResult(journalEntry);
        } catch (Exception e) {
            ioService.write(e.getMessage());
            e.printStackTrace();
        }
    }
}
