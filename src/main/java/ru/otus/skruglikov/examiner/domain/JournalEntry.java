package ru.otus.skruglikov.examiner.domain;

import lombok.Data;

@Data
public class JournalEntry {
    private final Student student;
    private final Exam exam;
    private int examScore;

}
