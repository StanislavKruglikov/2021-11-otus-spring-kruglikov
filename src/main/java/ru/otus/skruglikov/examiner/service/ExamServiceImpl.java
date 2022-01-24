package ru.otus.skruglikov.examiner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.config.ExaminerScoreExamPassConfig;
import ru.otus.skruglikov.examiner.domain.*;
import ru.otus.skruglikov.examiner.exception.ExaminerAssumedAnswerException;
import ru.otus.skruglikov.examiner.exception.ExaminerException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final LocaleService localeService;
    private final ExaminerScoreExamPassConfig examinerScoreExamPassConfig;
    private final QuizService quizService;


    public int startExam(final Exam exam) throws ExaminerException {
        int examScore = 0;
        for(final Quiz quiz : exam.getQuizList()) {
            quizService.showQuestion(quiz);
            final Map<Integer,Answer> answersShowMap = quizService.showAnswers(quiz);
            Integer assumedAnswerNumber = null;
            do {
                try {
                    assumedAnswerNumber = quizService.askAnswer(answersShowMap);
                } catch (ExaminerAssumedAnswerException e) {
                    final String variants = answersShowMap.keySet().toString();
                    localeService.output("examiner.incorrectAssumedAnswer",variants);
                }
            } while(assumedAnswerNumber == null);
            final Answer assumedAnswer = answersShowMap.get(assumedAnswerNumber);
            if (assumedAnswer.equals(quizService.getCorrectAnswer(quiz))) {
                examScore += quiz.getQuestion().getWeight();
            }
        }
        return examScore;
    }

    @Override
    public void showExamResult(final JournalEntry journalEntry) {
        int examScore = journalEntry.getExamScore();
        int passLevel = examinerScoreExamPassConfig.getScoreExamPass();
        final Student student = journalEntry.getStudent();
        final String studentInfo = String.format("%s %s",student.getLastName(),student.getFirstName());
        localeService.output("examiner.summary",studentInfo,String.valueOf(examScore));
        if(examScore >= passLevel) {
            localeService.output("examiner.resultSuccess",String.valueOf(passLevel));
        } else {
            localeService.output("examiner.resultFailed",String.valueOf(passLevel));
        }
    }
}
