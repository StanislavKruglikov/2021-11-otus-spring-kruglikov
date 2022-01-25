package ru.otus.skruglikov.examiner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.domain.Answer;
import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.ExaminerAssumedAnswerException;
import ru.otus.skruglikov.examiner.exception.ExaminerRightAnswerAbsentException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final LocaleIOService localeIOService;

    @Override
    public void showQuestion(final Quiz quiz) {
        localeIOService.write(quiz.getQuestion().getText());
    }

    @Override
    public Map<Integer,Answer> showAnswers(final Quiz quiz) {
        final Map<Integer,Answer> result = new HashMap<>();
        quiz.getAnswers().forEach(answer -> {
            int ind = result.size()+1;
            localeIOService.write(String.format("%s - %s",ind,answer.getText()));
            result.put(ind,answer);
        });
        return result;
    }

    @Override
    public Answer getCorrectAnswer(final Quiz quiz) throws ExaminerRightAnswerAbsentException {
        return quiz.getAnswers()
            .stream()
            .filter(Answer::isCorrect)
            .findAny()
            .orElseThrow(ExaminerRightAnswerAbsentException::new);
    }

    @Override
    public int askAnswer(final Map<Integer,Answer> answersShowMap) throws ExaminerAssumedAnswerException {
        localeIOService.writeMessage("examiner.askAnswer");
        final String answer = localeIOService.read();
        final int inputNumber = Integer.valueOf(answer);
        if(!answersShowMap.containsKey(inputNumber)) {
            throw new ExaminerAssumedAnswerException();
        }
        return inputNumber;
    }
}
