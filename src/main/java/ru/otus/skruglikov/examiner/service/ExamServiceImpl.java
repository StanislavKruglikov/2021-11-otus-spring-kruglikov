package ru.otus.skruglikov.examiner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.config.ExamScorePassConfig;
import ru.otus.skruglikov.examiner.domain.*;
import ru.otus.skruglikov.examiner.exception.ExaminerAssumedAnswerException;
import ru.otus.skruglikov.examiner.exception.ExaminerRightAnswerAbsentException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final LocaleIOService localeIOService;
    private final ExamScorePassConfig examScorePassConfig;
    private final QuizService quizService;


    public int startExam(final Exam exam) {
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
                    localeIOService.writeMessage("examiner.incorrectAssumedAnswer",variants);
                }
            } while(assumedAnswerNumber == null);
            final Answer assumedAnswer = answersShowMap.get(assumedAnswerNumber);
            try {
                if (assumedAnswer.equals(quizService.getCorrectAnswer(quiz))) {
                    examScore += quiz.getQuestion().getWeight();
                }
            } catch (ExaminerRightAnswerAbsentException e) {
              throw new RuntimeException("quiz has no right answer",e);
            }
        }
        return examScore;
    }

    @Override
    public void showExamResult(final JournalEntry journalEntry) {
        int examScore = journalEntry.getExamScore();
        int passLevel = examScorePassConfig.getExamScorePass();
        final Student student = journalEntry.getStudent();
        final String studentInfo = String.format("%s %s",student.getLastName(),student.getFirstName());
        localeIOService.writeMessage("examiner.summary",studentInfo,String.valueOf(examScore));
        if(examScore >= passLevel) {
            localeIOService.writeMessage("examiner.resultSuccess",String.valueOf(passLevel));
        } else {
            localeIOService.writeMessage("examiner.resultFailed",String.valueOf(passLevel));
        }
    }
}
