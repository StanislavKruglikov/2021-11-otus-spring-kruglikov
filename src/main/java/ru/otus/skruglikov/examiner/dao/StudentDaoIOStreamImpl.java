package ru.otus.skruglikov.examiner.dao;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;
import ru.otus.skruglikov.examiner.domain.Student;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.provider.InputOutputProvider;
import ru.otus.skruglikov.examiner.provider.LocaleProvider;

import java.util.Locale;
import java.util.Scanner;

@Repository

public class StudentDaoIOStreamImpl implements StudentDao {

    private final InputOutputProvider ioProvider;
    private final MessageSource messageSource;
    private final Locale locale;

    public StudentDaoIOStreamImpl(final InputOutputProvider ioProvider,final  MessageSource messageSource,
                                  final LocaleProvider localeProvider) {
        this.ioProvider = ioProvider;
        this.messageSource = messageSource;
        this.locale = localeProvider.getLocale();
    }

    @Override
    public Student createStudent() throws ExaminerException {
        final Scanner scanner = new Scanner(ioProvider.getInputStream());
        final String firstName = getStudentProperty(scanner,  "firstname");
        final String lastName = getStudentProperty(scanner,  "lastname");
        return new Student(firstName, lastName);
    }

    private String getStudentProperty(final Scanner scanner, final String propertyName)
        throws ExaminerException {
        final String propertyTitle = messageSource.getMessage("examiner.createStudent.prompt."+propertyName,null,locale);
        ioProvider.getPrintStream().println(messageSource.getMessage("examiner.createStudent.prompt",
            new String[]{ propertyTitle }, locale));
        final String result = scanner.nextLine();
        if (result == null || result.trim().length() < 1) {
            throw new ExaminerException(
                String.format("registration error: not specified student properties %s",propertyName));
        }
        return result;
    }
}
