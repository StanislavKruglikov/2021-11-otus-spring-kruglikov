package ru.otus.skruglikov.examiner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.domain.Answer;
import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.ExaminerAssumedAnswerException;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.provider.InputOutputProviderConsoleImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final InputOutputProviderConsoleImpl ioProvider;
    private final LocaleService localeService;

    @Override
    public void showQuestion(final Quiz quiz) {
        ioProvider.getOutput().println(quiz.getQuestion().getText());
    }

    @Override
    public Map<Integer,Answer> showAnswers(final Quiz quiz) {
        final Map<Integer,Answer> result = new HashMap<>();
        quiz.getAnswers().forEach(answer -> {
            int ind = result.size()+1;
            ioProvider.getOutput().println(String.format("%s - %s",ind,answer.getText()));
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
        localeService.output("examiner.askAnswer");
        final String answer = new Scanner(ioProvider.getInput()).next();
        final Integer inputNumber = Integer.valueOf(answer);
        if(!answersShowMap.containsKey(inputNumber)) {
            throw new ExaminerAssumedAnswerException("right answer not found");
        }
        return inputNumber;
    }
}
