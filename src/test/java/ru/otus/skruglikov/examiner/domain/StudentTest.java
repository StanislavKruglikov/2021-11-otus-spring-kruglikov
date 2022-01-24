package ru.otus.skruglikov.examiner.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Student")
class StudentTest {
    private final static String STUDENT_FIRST_NAME = "TestFirstName";
    private final static String STUDENT_LAST_NAME = "TestLastName";

    @DisplayName("корректно создается конструктором")
    @Test
    void shouldHaveCorrectConstructor() {

        final Student student = new Student(STUDENT_FIRST_NAME, STUDENT_LAST_NAME);
        assertAll("studentConstructor",
            () -> assertEquals(STUDENT_FIRST_NAME, student.getFirstName()),
            () -> assertEquals(STUDENT_LAST_NAME, student.getLastName())
        );
    }

    @DisplayName("корректно сравниваются на равенство")
    @Test
    void shouldHaveCorrectEquals() {
        assertEquals(new Student(STUDENT_FIRST_NAME, STUDENT_LAST_NAME),
            new Student(STUDENT_FIRST_NAME, STUDENT_LAST_NAME));

    }

    @DisplayName("корректно сравниваются на не равенство")
    @Test
    void shouldHaveCorrectNotEquals() {
        final Student student = new Student(STUDENT_FIRST_NAME, STUDENT_LAST_NAME);
        assertAll("studentNotEquals",
            () -> assertNotEquals(student, new Student(STUDENT_FIRST_NAME, STUDENT_LAST_NAME+"!")),
            () -> assertNotEquals(student, new Student(STUDENT_FIRST_NAME + "!", STUDENT_LAST_NAME))
        );

    }
}