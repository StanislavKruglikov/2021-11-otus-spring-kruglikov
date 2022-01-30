package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.skruglikov.examiner.dao.StudentDaoImpl;
import ru.otus.skruglikov.examiner.domain.Student;
import ru.otus.skruglikov.examiner.exception.RegistrationException;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Класс RegistrationService")
@SpringBootTest
class RegistrationServiceImplTest {

    public static final String FIRST_NAME = "TestFirstName";
    public static final String LAST_NAME = "TestLastName";

    @MockBean
    private StudentDaoImpl studentDao;
    @MockBean
    private LocaleIOServiceImpl localeIOService;

    @Autowired
    private RegistrationServiceImpl registrationService;

    @DisplayName("в результате регистрации корректно сздается объект Student")
    @Test
    void shouldCorrectRegisterStudent() throws Exception {
        when(localeIOService.read())
            .thenReturn(FIRST_NAME)
            .thenReturn(LAST_NAME);
        doNothing()
            .when(localeIOService)
            .writeMessage(any(),any());
        when(studentDao.createStudent(eq(FIRST_NAME),eq(LAST_NAME)))
            .thenReturn(new Student(FIRST_NAME,LAST_NAME));
        Student studentActual = registrationService.register();
        assertEquals(new Student(FIRST_NAME, LAST_NAME), studentActual);
    }

    @DisplayName("корректно выводится приглашение для ввода данных студента")
    @Test
    void shouldCorrectPrintPromptForCreateStudent() throws Exception {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            when(localeIOService.read())
                .thenReturn(String.format("%s\n%s\n", FIRST_NAME, LAST_NAME));
            doAnswer(inv-> {
                outputStream.write("TEST_FIRST_NAME\n".getBytes());
                return null;
            })
                .when(localeIOService)
                .writeMessage(eq("examiner.createStudent.prompt.firstname"),any());
            doAnswer(inv-> {
                outputStream.write("TEST_LAST_NAME\n".getBytes());
                return null;
            })
                .when(localeIOService)
                .writeMessage(eq("examiner.createStudent.prompt.lastname"),any());

            registrationService.register();
            assertEquals("TEST_FIRST_NAME\nTEST_LAST_NAME\n", outputStream.toString());
        }
    }

    @DisplayName("поднимает исключение если не все данные о студенте указаны")
    @Test
    void shouldThrowExceptionIfEmptyStudentProperty() {
        doNothing()
            .when(localeIOService)
            .writeMessage(any(),any());
        when(localeIOService.read())
            .thenReturn("")
            .thenReturn(FIRST_NAME)
            .thenReturn("");
        assertAll("throwCreateStudentTest",
            () -> assertThrowsExactly(RegistrationException.class, () -> registrationService.register()),
            () -> assertThrowsExactly(RegistrationException.class, () -> registrationService.register())
        );
    }
}