package ru.otus.skruglikov.examiner.dao;

import com.opencsv.CSVReader;
import com.opencsv.bean.*;
import com.opencsv.exceptions.*;
import lombok.Data;
import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.stereotype.Repository;
import ru.otus.skruglikov.examiner.domain.Answer;
import ru.otus.skruglikov.examiner.domain.Question;
import ru.otus.skruglikov.examiner.domain.Quiz;
import ru.otus.skruglikov.examiner.exception.LineHasBlockMarkException;
import ru.otus.skruglikov.examiner.exception.MismatchLineFormatException;
import ru.otus.skruglikov.examiner.provider.DatasourceProvider;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class QuizDaoCSVImpl implements QuizDao {

    private final DatasourceProvider datasourceProvider;

    public QuizDaoCSVImpl(DatasourceProvider datasourceProvider) {
        this.datasourceProvider = datasourceProvider;
    }

    public List<Quiz> readAllSortedQuizzes(final String examName) throws MismatchLineFormatException, IOException,
        CsvException {
        final List<Quiz> quizzes = new ArrayList<>();
        try(final CSVReader csvReader = new CSVReader(new InputStreamReader(datasourceProvider.getInputStream()))) {
            final ColumnPositionMappingStrategy<QuizCSVMappingStrategyBean> mappingStrategy =
                new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(QuizCSVMappingStrategyBean.class);
            CsvToBean<QuizCSVMappingStrategyBean> csvToBean = new CsvToBean<>();
            csvToBean.setCsvReader(csvReader);
            csvToBean.setMappingStrategy(mappingStrategy);
            String[] csvLineData;
            boolean examStartFound;
            do {
                csvLineData = csvReader.readNext();
                examStartFound = csvLineData != null && csvLineData.length == 1 && ("#"+examName).equals(csvLineData[0]);
            } while(csvLineData != null && !examStartFound);

            if(examStartFound) {
                int row = 0;
                final Iterator<QuizCSVMappingStrategyBean> iterator = csvToBean.iterator();
                while (iterator.hasNext()) {
                    try {
                        row++;
                        final QuizCSVMappingStrategyBean strategyBean = iterator.next();
                        final Question question = strategyBean.getQuestion();
                        final Set<Answer> answerSet = strategyBean.getAnswer();
                        if (answerSet.size() < 2) {
                            throw new MismatchLineFormatException(
                                String.format("the line %s should contain at least two answers", row));
                        } else if (answerSet.stream().noneMatch(Answer::isCorrect)) {
                            throw new MismatchLineFormatException(
                                String.format("the line %s should contain a one RIGHT answer", row));
                        }
                        quizzes.add(new Quiz(question,answerSet));
                    } catch (LineHasBlockMarkException e) {
                        break;
                    }
                }
            }
        }
        return quizzes
            .stream()
            .sorted()
            .collect(Collectors.toList());
    }

    public static class QuizCSVMappingStrategyBean {
        @CsvBindAndJoinByPosition(position = "0-2",elementType = Object.class)
        private MultiValuedMap<Integer,String> questionFields;
        private Question question;
        @CsvBindAndJoinByPosition(position = "3-",converter = AnswerBindConverter.class,elementType = Answer.class)
        private MultiValuedMap<Integer,Answer> answers;

        public Question getQuestion() throws MismatchLineFormatException, LineHasBlockMarkException {
            int ind = 0;
            try {
                String numOrderString = questionFields.get(ind)
                    .stream()
                    .findFirst()
                    .orElseThrow(()-> new MismatchLineFormatException("empty value field numOrder"));
                if(numOrderString.startsWith("#")) {
                    throw new LineHasBlockMarkException();
                }
                String weightString = questionFields.get(++ind)
                    .stream()
                    .findFirst()
                    .orElseThrow(()-> new MismatchLineFormatException("empty value field weight"));
                String text = questionFields.get(++ind)
                    .stream()
                    .findFirst()
                    .orElseThrow(()->new MismatchLineFormatException("empty value field text"));
                return new Question(Integer.parseInt(numOrderString),Integer.parseInt(weightString),text);
            } catch (IllegalArgumentException e) {
                throw new MismatchLineFormatException(e.getMessage());
            }
        }

        public Set<Answer> getAnswer() {
            return new HashSet<>(answers.values());
        }
    }

    public static class AnswerBindConverter extends AbstractCsvConverter {
        @Override
        public Answer convertToRead(String s) {
            final boolean isRight = s.endsWith("*");
            return new Answer(isRight ? s.substring(0,s.length()-1) : s ,isRight);
        }
    }
}
