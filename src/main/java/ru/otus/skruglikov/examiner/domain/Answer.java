package ru.otus.skruglikov.examiner.domain;

import lombok.Data;

@Data
public class Answer {
    private final String text;
    private final boolean correct;
}
