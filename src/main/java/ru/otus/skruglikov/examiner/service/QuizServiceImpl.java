package ru.otus.skruglikov.examiner.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.domain.Answer;
import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.ExaminerAssumedAnswerException;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.provider.InputOutputProvider;
import ru.otus.skruglikov.examiner.provider.LocaleProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
public class QuizServiceImpl extends ServiceMessageAwareAbstract implements QuizService {

    public QuizServiceImpl(final MessageSource messageSource,final  LocaleProvider localeProvider,
                           final InputOutputProvider ioProvider) {
        super(messageSource, localeProvider, ioProvider);
    }

    @Override
    public void showQuestion(final Quiz quiz) {
        ioProvider.getPrintStream().println(quiz.getQuestion().getText());
    }

    @Override
    public Map<Integer,Answer> showAnswers(final Quiz quiz) {
        final Map<Integer,Answer> result = new HashMap<>();
        quiz.getAnswers().forEach(answer -> {
            int ind = result.size()+1;
            ioProvider.getPrintStream().println(String.format("%s - %s",ind,answer.getText()));
            result.put(ind,answer);
        });
        return result;
    }

    @Override
    public Answer getCorrectAnswer(final Quiz quiz)  throws ExaminerException {
        return quiz.getAnswers()
            .stream()
            .filter(Answer::isCorrect)
            .findAny()
            .orElseThrow(() -> new ExaminerException("среди вариантов ответов нет корректного"));
    }

    @Override
    public Integer askAnswer(final Map<Integer,Answer> answersShowMap) throws ExaminerAssumedAnswerException {
        ioProvider.getPrintStream().println(messageSource.getMessage("examiner.askAnswer", null, locale));
        final String answer = new Scanner(ioProvider.getInputStream()).next();
        final Integer inputNumber = Integer.valueOf(answer);
        if(!answersShowMap.containsKey(inputNumber)) {
            throw new ExaminerAssumedAnswerException("right answer not found");
        }
        return inputNumber;
    }
}
