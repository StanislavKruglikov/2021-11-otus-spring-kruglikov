package ru.otus.skruglikov.examiner.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import ru.otus.skruglikov.examiner.domain.Student;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.provider.InputOutputProvider;
import ru.otus.skruglikov.examiner.provider.LocaleProvider;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("Класс StudentDaoIOStreamImpl")
@SpringBootTest
class StudentDaoConsoleTest {

    public static final String FIRST_NAME = "TestFirstName";
    public static final String LAST_NAME = "TestLastName";

    @Mock
    private InputOutputProvider ioProvider;
    @Mock
    private MessageSource messageSource;
    @Mock
    private LocaleProvider localeProvider;

    @InjectMocks
    private StudentDaoIOStreamImpl studentDao;


    @DisplayName("корректно сздается пользователь")
    @Test
    void shouldCorrectCreateStudent() throws Exception {
        final byte[] inputDataMock = String.format("%s\n%s\n", FIRST_NAME, LAST_NAME).getBytes(StandardCharsets.UTF_8);
        try (final OutputStream outputStream = new ByteArrayOutputStream();
             final InputStream inputStream = new ByteArrayInputStream(inputDataMock)) {
            when(messageSource.getMessage(anyString(), any(String[].class), any()))
                .thenReturn("");
            when(ioProvider.getInputStream())
                .thenReturn(inputStream);
            when(ioProvider.getPrintStream())
                .thenReturn(new PrintStream(outputStream));
            final Student student = studentDao.createStudent();
            assertEquals(new Student(FIRST_NAME, LAST_NAME), student);
        }
    }

    @DisplayName("корректно выводится приглашение для ввода данных студента")
    @Test
    void shouldCorrectPrintPromptForCreateStudent() throws Exception {
        final byte[] inputDataMock = String.format("%s\n%s\n", FIRST_NAME, LAST_NAME).getBytes(StandardCharsets.UTF_8);
        try (
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final InputStream inputStream = new ByteArrayInputStream(inputDataMock)) {
            when(messageSource.getMessage(eq("examiner.createStudent.prompt.firstname"), any(), any()))
                .thenReturn("firstname");
            when(messageSource.getMessage(eq("examiner.createStudent.prompt.lastname"), any(), any()))
                .thenReturn("lastname");
            when(messageSource.getMessage(eq("examiner.createStudent.prompt"), eq(new String[]{"firstname"}), any()))
                .thenReturn("TEST_FIRST_NAME");
            when(messageSource.getMessage(eq("examiner.createStudent.prompt"), eq(new String[]{"lastname"}), any()))
                .thenReturn("TEST_LAST_NAME");
            when(ioProvider.getInputStream())
                .thenReturn(inputStream);
            when(ioProvider.getPrintStream())
                .thenReturn(new PrintStream(byteArrayOutputStream));
            studentDao.createStudent();
            final String resultOutput = new String(byteArrayOutputStream.toByteArray());
            assertEquals("TEST_FIRST_NAME\nTEST_LAST_NAME\n", resultOutput);
        }
    }

    @DisplayName("поднимает исключение если не все данные о студенте указаны")
    @Test
    void shouldThrowExceptionIfEmptyStudentProperty() throws Exception {
        when(messageSource.getMessage(anyString(), any(String[].class), any()))
            .thenReturn("");
        try (
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final InputStream isFirstName =
                new ByteArrayInputStream((FIRST_NAME + "\n\n").getBytes(StandardCharsets.UTF_8));
            final InputStream isLastName =
                new ByteArrayInputStream(("\n" + LAST_NAME + "\n").getBytes(StandardCharsets.UTF_8))
        ) {
            when(ioProvider.getPrintStream())
                .thenReturn(new PrintStream(byteArrayOutputStream));
            when(ioProvider.getInputStream())
                .thenReturn(isFirstName)
                .thenReturn(isLastName);
            assertAll("throwCreateStudentTest",
                () -> assertThrowsExactly(ExaminerException.class, () -> studentDao.createStudent()),
                () -> assertThrowsExactly(ExaminerException.class, () -> studentDao.createStudent())
            );
        }
    }
}