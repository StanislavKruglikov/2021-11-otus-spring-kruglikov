package ru.otus.skruglikov.examiner.dao;

import com.opencsv.exceptions.CsvException;
import ru.otus.skruglikov.examiner.domain.Exam;
import ru.otus.skruglikov.examiner.exception.EmptyResultException;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.exception.MismatchLineFormatException;

import java.io.IOException;

public interface ExamDao {
    Exam findByName(String examName) throws EmptyResultException, ExaminerException, MismatchLineFormatException,
        CsvException, IOException;
}
