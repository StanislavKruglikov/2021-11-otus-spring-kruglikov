package ru.otus.skruglikov.examiner.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс ExaminerConfig")
class ExaminerConfigTest {

    @DisplayName("должен корректно задавать уровень прохождения экзамена")
    @Test
    void shouldCorrectSet() {
        final ExaminerConfig examinerConfigTest = new ExaminerConfig();
        examinerConfigTest.setScoreExamPass(100);
        assertEquals(100, examinerConfigTest.getScoreExamPass());
    }

    @DisplayName("должен корректно формировать путь к файлу данных с заданной локалью")
    @Test
    void shouldCreateQuizFilePathByLocalWithType() {
        final ExaminerConfig examinerConfigTest = new ExaminerConfig();
        examinerConfigTest.setLocale("ca-ES");
        examinerConfigTest.setExamDataPath("data/exam");
        assertEquals("data/exam_ca_ES",examinerConfigTest.getExamDataPath());
    }

    @DisplayName("должен корректно формировать путь к файлу данных c незаданной локалью")
    @Test
    void shouldCreateQuizFilePathByNoneLocaleAndType() {
        final ExaminerConfig examinerConfigTest = new ExaminerConfig();
        examinerConfigTest.setExamDataPath("data/exam");
        assertEquals("data/exam",examinerConfigTest.getExamDataPath());
    }
}