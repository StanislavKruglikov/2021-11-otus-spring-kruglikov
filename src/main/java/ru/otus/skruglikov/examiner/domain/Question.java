package ru.otus.skruglikov.examiner.domain;

import lombok.Data;

@Data
public class Question implements Comparable<Question> {
    private final int numberOrder;
    private final int weight;
    private final String text;

    @Override
    public int compareTo(Question o) {
        return this.numberOrder - o.numberOrder;
    }
}
