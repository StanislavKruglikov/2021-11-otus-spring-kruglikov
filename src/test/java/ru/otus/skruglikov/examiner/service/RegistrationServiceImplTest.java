package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.skruglikov.examiner.dao.StudentDaoImpl;
import ru.otus.skruglikov.examiner.domain.Student;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.provider.InputOutputProviderConsoleImpl;
import ru.otus.skruglikov.examiner.provider.InputProvider;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Класс RegistrationService")
@SpringBootTest
class RegistrationServiceImplTest {

    public static final String FIRST_NAME = "TestFirstName";
    public static final String LAST_NAME = "TestLastName";

    @Mock
    private StudentDaoImpl studentDao;
    @Mock
    private LocaleServiceStreamImpl localeService;
    @Mock
    private InputOutputProviderConsoleImpl ioProvider;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @DisplayName("в результате регистрации корректно сздается объект Student")
    @Test
    void shouldCorrectRegisterStudent() throws Exception {
        final byte[] inputDataMock = String.format("%s\n%s\n", FIRST_NAME, LAST_NAME).getBytes(StandardCharsets.UTF_8);
        try (final InputStream inputStream = new ByteArrayInputStream(inputDataMock)) {
            when(ioProvider.getInput())
                .thenReturn(inputStream);
            doNothing()
                .when(localeService)
                .output(any(),any());
            when(studentDao.createStudent(eq(FIRST_NAME),eq(LAST_NAME)))
                .thenReturn(new Student(FIRST_NAME,LAST_NAME));
            Student studentActual = registrationService.register();
            assertEquals(new Student(FIRST_NAME, LAST_NAME), studentActual);

        }
    }

    @DisplayName("корректно выводится приглашение для ввода данных студента")
    @Test
    void shouldCorrectPrintPromptForCreateStudent() throws Exception {
        final byte[] inputDataMock = String.format("%s\n%s\n", FIRST_NAME, LAST_NAME).getBytes(StandardCharsets.UTF_8);
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final InputStream inputStream = new ByteArrayInputStream(inputDataMock)) {
            when(ioProvider.getInput())
                .thenReturn(inputStream);
            doAnswer(inv-> {
                outputStream.write("TEST_FIRST_NAME\n".getBytes());
                return null;
            })
                .when(localeService)
                .output(eq("examiner.createStudent.prompt.firstname"),any());
            doAnswer(inv-> {
                outputStream.write("TEST_LAST_NAME\n".getBytes());
                return null;
            })
                .when(localeService)
                .output(eq("examiner.createStudent.prompt.lastname"),any());

            when(ioProvider.getInput())
                .thenReturn(inputStream);
            when(ioProvider.getOutput())
                .thenReturn(new PrintStream(outputStream));
            Student studentActual = registrationService.register();
            assertEquals("TEST_FIRST_NAME\nTEST_LAST_NAME\n", outputStream.toString());
        }
    }

    @DisplayName("поднимает исключение если не все данные о студенте указаны")
    @Test
    void shouldThrowExceptionIfEmptyStudentProperty() throws IOException {
        try (
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final InputStream isFirstName =
                new ByteArrayInputStream((FIRST_NAME + "\n\n").getBytes(StandardCharsets.UTF_8));
            final InputStream isLastName =
                new ByteArrayInputStream(("\n" + LAST_NAME + "\n").getBytes(StandardCharsets.UTF_8))
        ) {
            doNothing()
                .when(localeService)
                .output(any(),any());
            when(ioProvider.getInput())
                .thenReturn(isFirstName)
                .thenReturn(isLastName);
            assertAll("throwCreateStudentTest",
                () -> assertThrowsExactly(ExaminerException.class, () -> registrationService.register()),
                () -> assertThrowsExactly(ExaminerException.class, () -> registrationService.register())
            );
        }
    }
}