package ru.otus.skruglikov.examiner.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс ExaminerConfig")
class ExaminerConfigTest {

    @DisplayName("должен корректно задавать уровень прохождения экзамена")
    @Test
    void shouldCorrectSetScore() {
        final ExaminerConfig examinerConfigTest = new ExaminerConfig();
        examinerConfigTest.setExamScorePass(100);
        assertEquals(100, examinerConfigTest.getExamScorePass());
    }

    @DisplayName("должен корректно задавать путь к данным экзамена")
    @Test
    void shouldCorrectSetDataPath() {
        final ExaminerConfig examinerConfigTest = new ExaminerConfig();
        examinerConfigTest.setExamDataPath("data/exam");
        assertEquals("data/exam",examinerConfigTest.getDataPath());
    }

    @DisplayName("должен корректно задавать локаль")
    @Test
    void shouldCorrectSetLocale() {
        final ExaminerConfig examinerConfigTest = new ExaminerConfig();
        final Locale localeExpected = Locale.forLanguageTag("ca-ES");
        examinerConfigTest.setLocale(localeExpected.toLanguageTag());
        assertEquals(localeExpected,examinerConfigTest.getLocale());
    }
}