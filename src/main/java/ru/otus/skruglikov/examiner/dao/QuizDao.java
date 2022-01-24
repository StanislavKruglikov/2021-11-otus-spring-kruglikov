package ru.otus.skruglikov.examiner.dao;

import com.opencsv.exceptions.CsvException;
import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.exception.MismatchLineFormatException;

import java.io.IOException;
import java.util.List;


public interface QuizDao {
    List<Quiz> readAllSortedQuizzes(final String examName) throws MismatchLineFormatException, ExaminerException,
        IOException, CsvException;
}
