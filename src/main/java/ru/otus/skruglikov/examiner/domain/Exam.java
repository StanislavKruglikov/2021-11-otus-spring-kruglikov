package ru.otus.skruglikov.examiner.domain;

import lombok.Data;
import java.util.List;

@Data
public class Exam {
    private final String name;
    private final List<Quiz> quizList;
}
