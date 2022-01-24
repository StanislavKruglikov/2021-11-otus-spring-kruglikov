package ru.otus.skruglikov.examiner.service;

import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.dao.StudentDao;
import ru.otus.skruglikov.examiner.domain.Student;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.provider.InputProvider;

import java.io.InputStream;
import java.util.Scanner;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final StudentDao studentDao;
    private LocaleService localeService;
    private InputProvider<InputStream> ioProvider;

    public RegistrationServiceImpl(final StudentDao studentDao,final LocaleService localeService,
                                   final InputProvider<InputStream> ioProvider) {
        this.studentDao = studentDao;
        this.localeService = localeService;
        this.ioProvider = ioProvider;
    }

    @Override
    public Student register() throws ExaminerException {
        final Scanner scanner = new Scanner(ioProvider.getInput());
        final String firstName = getStudentProperty(scanner,  "firstname");
        final String lastName = getStudentProperty(scanner,  "lastname");
        return studentDao.createStudent(firstName,lastName);
    }

    private String getStudentProperty(final Scanner scanner, final String propertyName)
        throws ExaminerException {
        localeService.output("examiner.createStudent.prompt."+propertyName);
        final String result = scanner.nextLine();
        if (result == null || result.trim().length() < 1) {
            throw new ExaminerException(
                String.format("registration error: not specified student properties %s",propertyName));
        }
        return result;
    }
}
