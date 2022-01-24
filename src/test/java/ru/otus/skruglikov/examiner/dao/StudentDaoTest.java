package ru.otus.skruglikov.examiner.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.skruglikov.examiner.domain.Student;


import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс StudentDaoImpl")
@SpringBootTest
class StudentDaoTest {

    public static final String FIRST_NAME = "TestFirstName";
    public static final String LAST_NAME = "TestLastName";

    @Autowired
    private StudentDaoImpl studentDao;

    @DisplayName("метод createStudent должен корректно создавать объект Student")
    @Test
    void shouldCorrectCreateStudent() {
        final Student studentExpected = new Student(FIRST_NAME,LAST_NAME);
        assertEquals(studentExpected,studentDao.createStudent(FIRST_NAME,LAST_NAME));
    }
}