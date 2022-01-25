package ru.otus.skruglikov.examiner.service;

import ru.otus.skruglikov.examiner.domain.Answer;
import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.ExaminerAssumedAnswerException;
import ru.otus.skruglikov.examiner.exception.ExaminerRightAnswerAbsentException;

import java.util.Map;


public interface QuizService {

    void showQuestion(Quiz quiz);

    Map<Integer,Answer> showAnswers(Quiz quiz);

    Answer getCorrectAnswer(Quiz quiz) throws ExaminerRightAnswerAbsentException;

    int askAnswer(final Map<Integer,Answer> answersShowMap) throws ExaminerAssumedAnswerException;
}
