package ru.otus.skruglikov.examiner.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.config.ExaminerConfig;
import ru.otus.skruglikov.examiner.domain.*;
import ru.otus.skruglikov.examiner.exception.ExaminerAssumedAnswerException;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.provider.InputOutputProvider;
import ru.otus.skruglikov.examiner.provider.LocaleProvider;

import java.io.PrintStream;
import java.util.Map;

@Service
public class ExamServiceImpl extends ServiceMessageAwareAbstract implements ExamService {

    private final ExaminerConfig examinerConfig;
    private final QuizService quizService;

    public ExamServiceImpl(final MessageSource messageSource, final LocaleProvider localeProvider,
                           final InputOutputProvider ioProvider, final ExaminerConfig examinerConfig,
                           final QuizService quizService) {
        super(messageSource, localeProvider, ioProvider);
        this.examinerConfig = examinerConfig;
        this.quizService = quizService;
    }

    public int startExam(final Exam exam) throws ExaminerException {
        final PrintStream ps = ioProvider.getPrintStream();
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
                    ps.println(messageSource.getMessage("examiner.incorrectAssumedAnswer",
                        new String[]{ variants }, locale));
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
        final PrintStream ps = ioProvider.getPrintStream();
        int examScore = journalEntry.getExamScore();
        int passLevel = examinerConfig.getScoreExamPass();
        ps.println(messageSource
            .getMessage("examiner.summary",new String[] { String.valueOf(examScore) }, locale));
        if(examScore >= passLevel) {
            ps.println(messageSource
                .getMessage("examiner.resultSuccess",new String[] { String.valueOf(passLevel) }, locale));
        } else {
            ps.println(messageSource.getMessage("examiner.resultFailed",
                new String[] { String.valueOf(passLevel), String.valueOf(passLevel) }, locale));
        }
    }
}
