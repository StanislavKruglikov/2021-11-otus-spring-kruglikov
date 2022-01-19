package ru.otus.skruglikov.examiner.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.skruglikov.examiner.domain.Answer;
import ru.otus.skruglikov.examiner.domain.Question;
import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.ExaminerException;
import ru.otus.skruglikov.examiner.provider.DatasourceProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class QuizDaoCSVImpl implements QuizDao {

    private final DatasourceProvider datasourceProvider;

    public List<Quiz> readAllQuizzes(final String examName) throws ExaminerException {
        final List<Quiz> quizzes = new ArrayList<>();
            try(final InputStream ioStream = datasourceProvider.getInputStream()) {
                final Scanner scanner = new Scanner(ioStream);
                while(scanner.hasNext() && !("#"+examName).equals(scanner.nextLine()) ) { }
                int row = 0;
                while(scanner.hasNext()) {
                    String dataLine = scanner.nextLine();
                    if(!dataLine.startsWith("#")) {
                        quizzes.add(loadQuiz(++row, dataLine));
                    }
                }
            } catch(IOException e) {
                throw new ExaminerException(e.getMessage());
            }
        return quizzes;
    }

    public List<Quiz> readAllSortedQuizzes(final String examName) throws ExaminerException {
        return readAllQuizzes(examName).stream().sorted().collect(Collectors.toList());
    }

    private Quiz loadQuiz(final int row, final String csvLine) throws ExaminerException {
        final String[] csvLineParts = csvLine.split(",");
        return new Quiz(loadQuestion(row,csvLineParts),loadAnswers(row,csvLineParts));
    }

    private Question loadQuestion(final int row, final String[] csvLineParts) throws ExaminerException {
        if(csvLineParts.length < 4) {
            throw new ExaminerException(
                String.format("the line %s should contain over %s part divided by delimiter",row, 4));
        }
        return new Question(Integer.parseInt(csvLineParts[0]),Integer.parseInt(csvLineParts[1]),csvLineParts[2]);
    }

    private Set<Answer> loadAnswers(final int row, final String[] csvLineParts) throws ExaminerException {
        final Set<Answer> answerSet = new HashSet<>();
        Arrays.stream(csvLineParts)
            .skip(3)
            .forEach(p -> {
                final boolean isRight = p.endsWith("#");
                answerSet.add(new Answer(isRight ? p.substring(0,p.length()-1) : p ,isRight));
            });
        if(answerSet.size() < 1) {
            throw new ExaminerException(String.format("the line %s should contain at least two answers", row));
        }
        if(answerSet.stream().filter(Answer::isCorrect).count() != 1) {
            throw new ExaminerException(String.format("the line %s should contain a one RIGHT answer", row));
        }
        return answerSet;
    }
}
