package ru.otus.skruglikov.examiner.domain;

import lombok.Data;

import java.util.Set;

@Data
public class Quiz implements Comparable<Quiz> {
    private final Question question;
    private final Set<Answer> answers;

    @Override
    public int compareTo(Quiz o) {
        return question.compareTo(o.getQuestion());
    }
}
