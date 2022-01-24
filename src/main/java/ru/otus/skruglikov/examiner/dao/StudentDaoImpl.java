package ru.otus.skruglikov.examiner.dao;


import org.springframework.stereotype.Repository;
import ru.otus.skruglikov.examiner.domain.Student;

@Repository

public class StudentDaoImpl implements StudentDao {

    @Override
    public Student createStudent(final String firstName,final String lastName) {
        return new Student(firstName, lastName);
    }

}
